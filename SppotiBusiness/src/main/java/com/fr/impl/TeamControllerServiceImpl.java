package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.MemberNotInAdminTeamException;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.*;
import com.fr.service.TeamControllerService;
import com.fr.transformers.SppotiTransformer;
import com.fr.transformers.impl.TeamTransformerImpl;
import com.fr.transformers.impl.UserTransformerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 1/22/17.
 */

@Component
class TeamControllerServiceImpl extends AbstractControllerServiceImpl implements TeamControllerService
{
	
	/** User transformer. */
	private final UserTransformerImpl userTransformer;
	/** Team transformer. */
	private final TeamTransformerImpl teamTransformer;
	/** Sppoti transformer. */
	private final SppotiTransformer sppotiTransformer;
	/** Team list size. */
	@Value("${key.teamsPerPage}")
	private int teamPageSize;
	
	/** Init dependencies. */
	@Autowired
	public TeamControllerServiceImpl(final UserTransformerImpl userTransformer,
									 final TeamTransformerImpl teamTransformer,
									 final SppotiTransformer sppotiTransformer)
	{
		this.userTransformer = userTransformer;
		this.teamTransformer = teamTransformer;
		this.sppotiTransformer = sppotiTransformer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public TeamDTO saveTeam(final TeamDTO team, final Long adminId)
	{
		
		final SportEntity sportEntity = this.sportRepository.findOne(team.getSportId());
		
		if (sportEntity == null) {
			throw new EntityNotFoundException("SportEntity id not found");
		}
		
		final TeamEntity teamToSave = new TeamEntity();
		teamToSave.setName(team.getName());
		teamToSave.setCoverPath(team.getCoverPath());
		teamToSave.setLogoPath(team.getLogoPath());
		teamToSave.setSport(sportEntity);
		teamToSave.setTeamMembers(getTeamMembersEntityFromDto(team.getMembers(), teamToSave, null));
		
		final TeamEntity addedTeam = this.teamRepository.save(teamToSave);
		
		//Send email and notify the invited members.
		addedTeam.getTeamMembers().forEach(m -> {
			if (!m.getUser().getId().equals(getConnectedUser().getId())) {
				//Email
				sendJoinTeamEmail(addedTeam, getUserById(m.getUser().getId()),
						this.teamMembersRepository.findByTeamUuidAndAdminTrue(addedTeam.getUuid()));
				//Notification
				final TeamMemberEntity admin = this.teamMembersRepository
						.findByTeamUuidAndAdminTrue(addedTeam.getUuid());
				addNotification(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM, admin.getUser(), m.getUser(),
						admin.getTeam(), null);
			}
		});
		
		return fillTeamResponse(addedTeam, null);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateTeamMembers(final TeamDTO TeamDTO, final int memberId, final int teamId)
	{
		
		final TeamMemberEntity usersTeam = this.teamMembersRepository.findByUserUuidAndTeamUuid(memberId, teamId);
		
		if (usersTeam == null) {
			throw new EntityNotFoundException("Member not found");
		}
		
		if (TeamDTO.getStatus() != null && !TeamDTO.getStatus().equals(0)) {
			for (final GlobalAppStatusEnum status : GlobalAppStatusEnum.values()) {
				if (status.getValue() == TeamDTO.getStatus()) {
					usersTeam.setStatus(status);
				}
			}
		}
		
		if (TeamDTO.getxPosition() != null && TeamDTO.getyPosition() != null) {
			usersTeam.setyPosition(TeamDTO.getyPosition());
			usersTeam.setxPosition(TeamDTO.getxPosition());
		}
		
		this.teamMembersRepository.save(usersTeam);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TeamDTO getTeamById(final int teamId)
	{
		
		final List<TeamEntity> team = this.teamRepository.findByUuidAndDeletedFalse(teamId);
		
		if (team == null || team.isEmpty()) {
			throw new EntityNotFoundException("TeamEntity id not found");
		}
		
		return fillTeamResponse(team.get(0), null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> getAllTeamsByUserId(final int userId, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		final List<TeamMemberEntity> myTeams = this.teamMembersRepository.findByUserUuidAndAdminTrue(userId, pageable);
		
		return myTeams.stream().map(team -> fillTeamResponse(team.getTeam(), null)).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void acceptTeam(final int teamId, final int userId)
	{
		
		final TeamMemberEntity teamMembers = this.teamMembersRepository.findByUserUuidAndTeamUuid(userId, teamId);
		
		if (teamMembers == null) {
			throw new EntityNotFoundException("TeamEntity not found");
		}
		
		teamMembers.setStatus(GlobalAppStatusEnum.CONFIRMED);
		
		if (this.teamMembersRepository.save(teamMembers) != null) {
			addNotification(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION, teamMembers.getUser(),
					this.teamMembersRepository.findByTeamUuidAndAdminTrue(teamId).getUser(), teamMembers.getTeam(),
					null);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void refuseTeam(final int teamId, final int userId)
	{
		
		final Optional<TeamMemberEntity> teamMembers = Optional
				.ofNullable(this.teamMembersRepository.findByUserUuidAndTeamUuid(userId, teamId));
		
		teamMembers.orElseThrow(() -> new EntityNotFoundException("TeamEntity not found"));
		
		teamMembers.ifPresent(t -> {
			t.setStatus(GlobalAppStatusEnum.REFUSED);
			
			if (this.teamMembersRepository.save(t) != null) {
				addNotification(NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION, t.getUser(),
						this.teamMembersRepository.findByTeamUuidAndAdminTrue(teamId).getUser(), t.getTeam(), null);
			}
			
		});
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deleteMemberFromTeam(final int teamId, final int memberId)
	{
		final UserEntity admin = getConnectedUser();

		//Check if connected user is team admin
		final TeamMemberEntity adminTeamMembers = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndAdminTrue(admin.getUuid(), teamId);
		
		if (adminTeamMembers == null) {
			throw new NotAdminException("Only team admin can delete members");
		}
		
		//find target member to delete.
		final TeamMemberEntity targetTeamMember = this.teamMembersRepository
				.findByUserUuidAndTeamUuid(memberId, teamId);
		
		if (targetTeamMember == null) {
			throw new EntityNotFoundException("Member to delete not found");
		}

		//If member is admin - deny delete
		if (targetTeamMember.getAdmin()) {
			throw new BusinessGlobalException("Delete admin is forbidden");
		}
		
		//Admin and member to delete are in the same team
		if (adminTeamMembers.getTeam().getId().equals(targetTeamMember.getTeam().getId())) {

			this.teamMembersRepository.delete(targetTeamMember);

		} else {
			throw new MemberNotInAdminTeamException(
					"permission denied for admin with id(" + admin.getUuid() + ") to delete the memeber with id (" +
							memberId + ")");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deleteTeam(final int id)
	{
		
		final List<TeamEntity> team = this.teamRepository.findByUuidAndDeletedFalse(id);
		
		if (team == null || team.isEmpty()) {
			throw new EntityNotFoundException("TeamEntity not found");
		}
		
		team.get(0).setDeleted(true);
		this.teamRepository.save(team);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public UserDTO addMember(final int teamId, final UserDTO userParam)
	{
		
		final UserEntity teamMemberAsUser = getUserByUuId(userParam.getId());
		
		if (teamMemberAsUser == null) {
			throw new EntityNotFoundException("UserDTO with id (" + userParam.getId() + ") Not found");
		}
		
		final List<TeamEntity> teamList = this.teamRepository.findByUuidAndDeletedFalse(teamId);
		
		//Check if team id exist
		if (StringUtils.isEmpty(teamList)) {
			throw new EntityNotFoundException("TeamEntity with id (" + teamId + ") Not found");
		}
		final TeamEntity team = teamList.get(0);
		
		//Check if connected user is team admin
		final TeamMemberEntity teamAdmin = this.teamMembersRepository.findByTeamUuidAndAdminTrue(teamId);
		if (!teamAdmin.getUser().getId().equals(getConnectedUser().getId())) {
			//NOT TEAM ADMIN.
			throw new NotAdminException("You must be the team admin to access this service");
		}
		
		//Check if the user is not already a member
		if (team.getTeamMembers().stream().anyMatch(tm -> tm.getUser().getUuid() == userParam.getId())) {
			throw new BusinessGlobalException("User is already a member in this team.");
		}
		
		//Add new member to the team
		final TeamMemberEntity teamMembers = new TeamMemberEntity();
		teamMembers.setTeam(team);
		teamMembers.setUser(teamMemberAsUser);
		
		if (StringUtils.isEmpty(userParam.getxPosition())) {
			teamMembers.setxPosition(userParam.getxPosition());
		}
		
		if (StringUtils.isEmpty(userParam.getyPosition())) {
			teamMembers.setyPosition(userParam.getyPosition());
		}
		
		//save new member.
		this.teamMembersRepository.save(teamMembers);
		
		//Notify added member
		addNotification(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM, teamAdmin.getUser(), teamMemberAsUser,
				team, null);
		
		//Send email to the new team member.
		sendJoinTeamEmail(team, teamMemberAsUser, teamAdmin);
		
		//return new member.
		return this.userTransformer.modelToDto(teamMemberAsUser);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public TeamDTO updateTeam(final int connectedUserId, final TeamDTO TeamDTO)
	{
		
		final TeamMemberEntity teamMemberEntity = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndAdminTrue(connectedUserId, TeamDTO.getId());
		
		if (teamMemberEntity == null) {
			throw new NotAdminException("You must be the team admin to access this service");
		}
		
		final TeamEntity teamEntity = teamMemberEntity.getTeam();
		
		if (StringUtils.hasText(TeamDTO.getName())) {
			teamEntity.setName(TeamDTO.getName());
		}
		
		if (StringUtils.hasText(TeamDTO.getLogoPath())) {
			teamEntity.setLogoPath(TeamDTO.getLogoPath());
		}
		
		if (StringUtils.hasText(TeamDTO.getCoverPath())) {
			teamEntity.setCoverPath(TeamDTO.getCoverPath());
		}
		
		if (StringUtils.hasText(TeamDTO.getColor())) {
			teamEntity.setColor(TeamDTO.getColor());
		}
		
		return this.teamTransformer.modelToDto(this.teamRepository.save(teamEntity));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateTeamCaptain(final int teamId, final int memberId, final int connectedUserId)
	{
		
		checkTeamAdminAccess(teamId, connectedUserId);
		
		final List<TeamMemberEntity> teamMemberEntity = this.teamMembersRepository.findByTeamUuid(teamId);
		
		teamMemberEntity.forEach(t -> {
			if (t.getUuid() == memberId || t.getUser().getUuid() == memberId) {
				t.setTeamCaptain(true);
			} else {
				t.setTeamCaptain(false);
			}
		});
		this.teamMembersRepository.save(teamMemberEntity);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> getAllJoinedTeamsByUserId(final int userId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		final Predicate<TeamMemberEntity> filter;
		if (getConnectedUser().getUuid() == userId) {
			filter = t -> t.getStatus().name().equals(GlobalAppStatusEnum.CONFIRMED.name()) ||
					t.getStatus().name().equals(GlobalAppStatusEnum.PENDING.name()) ||
					t.getStatus().name().equals(GlobalAppStatusEnum.REFUSED.name());
		} else {
			filter = t -> t.getStatus().name().equals(GlobalAppStatusEnum.CONFIRMED.name());
		}
		
		return this.teamMembersRepository.findByUserUuidAndAdminFalse(userId, pageable).stream().filter(filter)
				.map(t -> this.teamTransformer.modelToDto(t.getTeam())).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> getAllDeletedTeamsByUserId(final int userId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		final List<TeamMemberEntity> myTeams = this.teamMembersRepository
				.findByUserUuidAndTeamDeletedFalse(getConnectedUser().getUuid(), pageable);
		
		return myTeams.stream().map(t -> fillTeamResponse(t.getTeam(), null)).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public TeamDTO responseToSppotiAdminChallenge(final SppotiDTO dto, final int teamId)
	{
		
		checkTeamAdminAccess(teamId, getConnectedUser().getUuid());
		
		final Optional<SppotiEntity> optional = Optional.ofNullable(this.sppotiRepository.findByUuid(dto.getId()));
		
		if (optional.isPresent()) {
			final SppotiEntity sp = optional.get();
			//Check if sppoti has already a CONFIRMED adverse team in the adverse team list.
			if (sp.getAdverseTeams() != null &&
					sp.getAdverseTeams().stream().anyMatch(t -> t.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))) {
				throw new BusinessGlobalException("This sppoti has already an adverse team");
			}
			
			//Check if team exists.
			final TeamEntity team = getTeamEntityIfExist(teamId);
			
			//Check if team is not already accepted as challenger of this sppoti.
			final Optional<SppotiAdverseEntity> sppotiAdverseEntityOptional = sp.getAdverseTeams().stream()
					.filter(t -> t.getTeam().getId().equals(team.getId())).findAny();
			
			//Team not found in sppoti adverse team list.
			sppotiAdverseEntityOptional
					.orElseThrow(() -> new BusinessGlobalException("This team has no challenge to respond."));
			
			//Team found in awaiting list.
			if (sppotiAdverseEntityOptional.isPresent()) {
				final SppotiAdverseEntity t = sppotiAdverseEntityOptional.get();
				//answer to sppoti admin challenge request
				if (t.getFromSppotiAdmin().equals(Boolean.TRUE)) {
					//Team already CONFIRMED.
					if (t.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
						throw new BusinessGlobalException("This team is already selected to challenge the host team");
					}
					//Team already REFUSED.
					else if (t.getStatus().equals(GlobalAppStatusEnum.REFUSED)) {
						throw new BusinessGlobalException("This team was already refused to challenge the host team");
					} else {
						//Team exist in adverse team list in PENDING mode.
						if (dto.getTeamAdverseStatus().equals(GlobalAppStatusEnum.CONFIRMED.name())) {
							t.setStatus(GlobalAppStatusEnum.valueOf(dto.getTeamAdverseStatus()));
							
							//Convert team members to sppoters if status equals to confirmed
							final Set<SppoterEntity> sppotiMembers = convertAdverseTeamMembersToSppoters(t.getTeam(),
									sp);
							sp.setSppotiMembers(sppotiMembers);
							this.sppotiRepository.save(sp);
							
							//Set other challenge status to REFUSED
							sp.getAdverseTeams().forEach(a -> {
								if (!a.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
									a.setStatus(GlobalAppStatusEnum.REFUSED);
									this.sppotiAdverseRepository.save(a);
								}
							});
							
							//send notification to sppoti admin.
							addNotification(NotificationTypeEnum.TEAM_ADMIN_ACCEPTED_YOUR_CHALLENGE, null,
									sp.getUserSppoti(), t.getTeam(), sp);

							//save changes and return team.
							return this.teamTransformer.modelToDto(this.sppotiAdverseRepository.save(t).getTeam());
						} else {
							//Challenge refused -> Delete row from database.
							sp.getAdverseTeams().remove(t);
							this.sppotiRepository.save(sp);

							//send notification to sppoti admin.
							addNotification(NotificationTypeEnum.TEAM_ADMIN_REFUSED_YOUR_CHALLENGE, null,
									sp.getUserSppoti(), t.getTeam(), sp);

							return new TeamDTO();
						}
					}
				}

				//Cancel my challenge request.
				else {
					if (dto.getTeamAdverseStatus().equals(GlobalAppStatusEnum.REFUSED.name())) {
						sp.getAdverseTeams().remove(t);
						this.sppotiRepository.save(sp);

						//send notification to sppoti admin.
						addNotification(NotificationTypeEnum.TEAM_ADMIN_CANCELED_HIS_CHALLENGE, null,
								sp.getUserSppoti(), t.getTeam(), sp);

						return new TeamDTO();
					} else {
						throw new BusinessGlobalException("Status unauthorized in the request");
					}
				}
			}
		}
		
		throw new EntityNotFoundException("Sppoti id (" + dto.getId() + ") not found");
	}
	
	/**
	 * @param teamId
	 * 		team id.
	 *
	 * @return team entity if exist.
	 */
	private TeamEntity getTeamEntityIfExist(final int teamId)
	{
		//Check if team exists.
		final List<TeamEntity> teamEntityList = this.teamRepository.findByUuidAndDeletedFalse(teamId);
		if (teamEntityList.isEmpty()) {
			throw new EntityNotFoundException("Team id (" + teamId + ") not found");
		}
		return teamEntityList.get(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SppotiDTO> getAllPendingChallenges(final int teamId, final int page)
	{
		
		//Check if user is team admin.
		checkTeamAdminAccess(teamId, getConnectedUser().getUuid());
		
		//Check if team exists.
		getTeamEntityIfExist(teamId);
		
		final List<SppotiAdverseEntity> sppotiAdverseEntities = this.sppotiAdverseRepository
				.findByTeamUuidAndFromSppotiAdminTrue(teamId);
		
		return sppotiAdverseEntities.stream().filter(v -> v.getStatus().equals(GlobalAppStatusEnum.PENDING))
				.map(ad -> this.sppotiTransformer.modelToDto(ad.getSppoti())).collect(Collectors.toList());
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> findAllAdverseTeamsAllowedToBeChallengedBySppotiAdmin(final int sppotiId, final String team,
																			   final int page)
	{
		final Optional<SppotiEntity> sppotiEntity = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		
		if (sppotiEntity.isPresent()) {
			final SppotiEntity sppoti = sppotiEntity.get();
			//Only sppoti admin can access this service.
			if (!getConnectedUser().getId().equals(sppoti.getUserSppoti().getId())) {
				throw new NotAdminException("You have to be a sppoti admin to access this service.");
			}
			
			//Get current adverse teams.
			final Set<SppotiAdverseEntity> sppotiAdverseEntities = sppoti.getAdverseTeams();
			
			//Find all teams by sppoti sport.
			final Pageable pageable = new PageRequest(page, this.teamPageSize);
			
			final List<TeamEntity> myTeams = this.teamRepository
					.findAllAllowedTeamsToChallengeAsSppotiAdmin(sppoti.getSport().getId(), team,
							sppoti.getUserSppoti().getId(), pageable);
			
			final List<TeamDTO> teams = myTeams.stream().map(t -> fillTeamResponse(t, null))
					.collect(Collectors.toList());
			final List<TeamDTO> result = new ArrayList<>();
			result.addAll(teams);
			
			//remove current adverse teams from the final result.
			teams.forEach(t -> sppotiAdverseEntities.forEach(a -> {
				if (t.getId().equals(a.getTeam().getUuid()) ||
						t.getId().equals(sppotiEntity.get().getTeamHostEntity().getUuid())) {
					result.remove(t);
				}
			}));
			
			return result;
		}
		
		throw new EntityNotFoundException("Sppoti not found");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> getAllAllowedTeamsToChallengeSppoti(final Long userId, final Integer sppotiId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		final Optional<SppotiEntity> optional = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		
		if (optional.isPresent()) {
			final SppotiEntity sppoti = optional.get();
			//Sppoti adverse teams.
			final List<TeamDTO> sppotiAdverseTeams = sppoti.getAdverseTeams().stream()
					.map(ad -> this.teamTransformer.modelToDto(ad.getTeam())).collect(Collectors.toList());
			
			//all user teams.
			final List<TeamDTO> teamDTOList = this.teamMembersRepository
					.findByTeamSportIdAndUserIdAndAdminTrue(sppoti.getSport().getId(), userId, pageable).stream()
					.map(t -> this.teamTransformer.modelToDto(t.getTeam())).collect(Collectors.toList());
			
			//Do not return teams that are already in sppoti adverse team list.
			final List<TeamDTO> result = new ArrayList<>();
			result.addAll(teamDTOList);
			teamDTOList.forEach(t -> sppotiAdverseTeams.forEach(ad -> {
				if (t.getId().equals(ad.getId())) {
					result.remove(t);
				}
			}));
			
			return result;
		}
		
		throw new EntityNotFoundException("Sppoti not found");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> getAllTeamsBySportType(final Long sportId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		return this.teamMembersRepository
				.findByTeamSportIdAndUserIdAndAdminTrue(sportId, getConnectedUser().getId(), pageable).stream()
				.map(t -> this.teamTransformer.modelToDto(t.getTeam())).collect(Collectors.toList());
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> findAllMyTeams(final String team, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		final List<TeamMemberEntity> myTeams = this.teamMembersRepository
				.findByUserUuidAndTeamNameContaining(getConnectedUser().getUuid(), team, pageable);
		
		return myTeams.stream().map(t -> fillTeamResponse(t.getTeam(), null)).collect(Collectors.toList());
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> findAllTeams(final String team, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize);
		
		final List<TeamEntity> myTeams = this.teamRepository.findByNameContainingAndDeletedFalse(team, pageable);
		
		return myTeams.stream().map(t -> fillTeamResponse(t, null)).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> findAllMyTeamsBySport(final String team, final Long sport, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize);
		
		final List<TeamMemberEntity> myTeams = this.teamMembersRepository
				.findByTeamSportIdAndUserUuidAndTeamNameContaining(sport, getConnectedUser().getUuid(), team, pageable);
		
		return myTeams.stream().map(t -> fillTeamResponse(t.getTeam(), null)).collect(Collectors.toList());
	}
	
	
	/**
	 * Allow access only for team admin.
	 *
	 * @param teamId
	 * 		team id.
	 * @param connectedUserId
	 * 		user id.
	 */
	private void checkTeamAdminAccess(final int teamId, final int connectedUserId)
	{
		if (this.teamMembersRepository.findByUserUuidAndTeamUuidAndAdminTrue(connectedUserId, teamId) == null) {
			throw new NotAdminException("You must be the team admin to access this service");
		}
	}
}
