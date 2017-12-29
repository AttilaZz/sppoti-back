package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.ErrorMessageEnum;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.TeamStatus;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.MemberNotInAdminTeamException;
import com.fr.commons.exception.NotAdminException;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.service.NotificationBusinessService;
import com.fr.service.TeamBusinessService;
import com.fr.transformers.SppotiTransformer;
import com.fr.transformers.impl.TeamTransformerImpl;
import com.fr.transformers.impl.UserTransformerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.fr.commons.enumeration.notification.NotificationObjectType.TEAM;

/**
 * Created by djenanewail on 1/22/17.
 */

@Component
class TeamBusinessServiceImpl extends CommonControllerServiceImpl implements TeamBusinessService
{
	private final Logger LOGGER = LoggerFactory.getLogger(TeamBusinessServiceImpl.class);
	
	@Autowired
	private UserTransformerImpl userTransformer;
	@Autowired
	private TeamTransformerImpl teamTransformer;
	@Autowired
	private SppotiTransformer sppotiTransformer;
	@Autowired
	private NotificationBusinessService notificationService;
	
	@Value("${key.teamsPerPage}")
	private int teamPageSize;
	
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
		
		final TeamEntity teamToSave = this.teamTransformer.dtoToModel(team);
		
		teamToSave.setTeamMembers(getTeamMembersEntityFromDto(team.getMembers(), teamToSave, null));
		
		final TeamEntity addedTeam = this.teamRepository.save(teamToSave);
		
		//Send email and notify the invited members.
		addedTeam.getTeamMembers().forEach(m -> {
			if (!m.getUser().getId().equals(getConnectedUser().getId())) {
				//Email
				sendJoinTeamEmail(addedTeam, getUserById(m.getUser().getId()), this.teamMembersRepository
						.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(addedTeam.getUuid(),
								SppotiUtils.statusToFilter()));
				//Notification
				final TeamMemberEntity admin = this.teamMembersRepository
						.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(addedTeam.getUuid(),
								SppotiUtils.statusToFilter());
				
				this.notificationService.saveAndSendNotificationToUsers(admin.getUser(), m.getUser(), TEAM,
						NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM, admin.getTeam());
			}
		});
		
		return fillTeamResponse(addedTeam, null);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateTeamMembers(final TeamDTO TeamDTO, final String memberId, final String teamId)
	{
		
		final TeamMemberEntity usersTeam = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndTeamDeletedFalse(memberId, teamId,
						SppotiUtils.statusToFilter());
		
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
	public TeamDTO getTeamById(final String teamId)
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
	public List<TeamDTO> getAllTeamsByUserId(final String userId, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		final List<TeamMemberEntity> myTeams = this.teamMembersRepository
				.findByUserUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(userId, SppotiUtils.statusToFilter(),
						pageable);
		
		return myTeams.stream().map(team -> fillTeamResponse(team.getTeam(), null)).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void acceptTeamRequestSentFromTeamAdmin(final String teamId)
	{
		
		final TeamMemberEntity teamMembers = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndTeamDeletedFalse(getConnectedUserUuid(), teamId,
						SppotiUtils.statusToFilter());
		
		if (teamMembers == null) {
			throw new EntityNotFoundException("TeamEntity not found");
		}
		
		teamMembers.setStatus(GlobalAppStatusEnum.CONFIRMED);
		
		final TeamMemberEntity teamAdmin = this.teamMembersRepository
				.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(teamId, SppotiUtils.statusToFilter());
		
		if (this.teamMembersRepository.save(teamMembers) != null) {
			this.notificationService.saveAndSendNotificationToUsers(teamMembers.getUser(), teamAdmin.getUser(), TEAM,
					NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION, teamMembers.getTeam());
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void refuseTeamRequestSentFromTeamAdmin(final String teamId)
	{
		
		final Optional<TeamMemberEntity> teamMembers = Optional.ofNullable(this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndTeamDeletedFalse(getConnectedUserUuid(), teamId,
						SppotiUtils.statusToFilter()));
		
		teamMembers.orElseThrow(() -> new EntityNotFoundException("TeamEntity not found"));
		
		teamMembers.ifPresent(t -> {
			t.setStatus(GlobalAppStatusEnum.REFUSED);
			
			final TeamMemberEntity teamAdmin = this.teamMembersRepository
					.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(teamId, SppotiUtils.statusToFilter());
			
			if (this.teamMembersRepository.save(t) != null) {
				this.notificationService.saveAndSendNotificationToUsers(t.getUser(), teamAdmin.getUser(), TEAM,
						NotificationTypeEnum.X_REFUSED_YOUR_TEAM_INVITATION, t.getTeam());
			}
			
		});
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deleteMemberFromTeam(final String teamId, final String memberId)
	{
		final UserEntity admin = getConnectedUser();
		
		//Check if connected user is team admin
		final TeamMemberEntity adminTeamMembers = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(admin.getUuid(), teamId,
						SppotiUtils.statusToFilter());
		
		if (adminTeamMembers == null) {
			throw new NotAdminException("Only team admin can delete members");
		}
		
		//find target member to delete.
		final TeamMemberEntity targetTeamMember = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndTeamDeletedFalse(memberId, teamId,
						SppotiUtils.statusToFilter());
		
		if (targetTeamMember == null) {
			throw new EntityNotFoundException("Member to delete not found");
		}
		
		//If member is admin - deny delete
		if (targetTeamMember.getAdmin()) {
			throw new BusinessGlobalException("Delete admin is forbidden");
		}
		
		//Admin and member to delete are in the same team
		if (adminTeamMembers.getTeam().getId().equals(targetTeamMember.getTeam().getId())) {
			
			//Remove member.
			targetTeamMember.setStatus(GlobalAppStatusEnum.DELETED);
			targetTeamMember.getSppotiMembers().forEach(s -> s.setStatus(GlobalAppStatusEnum.DELETED));
			this.teamMembersRepository.save(targetTeamMember);
			
			//Notify him
			//TODO: notification (Remove team member)
			
			
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
	public void deleteTeam(final String teamId)
	{
		
		final List<TeamEntity> team = this.teamRepository.findByUuidAndDeletedFalse(teamId);
		
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
	public UserDTO addMember(final String teamId, final UserDTO userParam)
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
		final TeamMemberEntity teamAdmin = this.teamMembersRepository
				.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(teamId, SppotiUtils.statusToFilter());
		if (!teamAdmin.getUser().getId().equals(getConnectedUser().getId())) {
			//NOT TEAM ADMIN.
			throw new NotAdminException("You must be the team admin to access this service");
		}
		
		//Check if the user is not already a member
		if (team.getTeamMembers().stream().anyMatch(tm -> Objects.equals(tm.getUser().getUuid(), userParam.getId()) &&
				tm.getStatus().isNotPendingAndNotRefusedAndNotDeletedAndNotCancelled())) {
			throw new EntityExistsException("User is already a member in this team.");
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
		this.notificationService.saveAndSendNotificationToUsers(teamAdmin.getUser(), teamMemberAsUser, TEAM,
				NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM, team);
		
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
	public TeamDTO updateTeam(final String connectedUserId, final TeamDTO TeamDTO)
	{
		
		final TeamMemberEntity teamMemberEntity = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(connectedUserId,
						TeamDTO.getId(), SppotiUtils.statusToFilter());
		
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
		
		if (TeamDTO.getType() != null &&
				(TeamStatus.PRIVATE.equals(TeamDTO.getType()) || TeamStatus.PUBLIC.equals(TeamDTO.getType()))) {
			teamEntity.setType(TeamDTO.getType());
		}
		
		return this.teamTransformer.modelToDto(this.teamRepository.save(teamEntity));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateTeamCaptain(final String teamId, final String memberId)
	{
		
		checkTeamAdminAccess(teamId);
		
		final List<TeamMemberEntity> teamMemberEntity = this.teamMembersRepository
				.findByTeamUuidAndStatusNotInAndTeamDeletedFalse(teamId, SppotiUtils.statusToFilter());
		
		teamMemberEntity.forEach(t -> {
			if (t.getUuid().equals(memberId) || t.getUser().getUuid().equals(memberId)) {
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
	public List<TeamDTO> getAllJoinedTeamsByUserId(final String userId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		final Predicate<TeamMemberEntity> filter;
		if (Objects.equals(getConnectedUserUuid(), userId)) {
			filter = t -> t.getStatus().name().equals(GlobalAppStatusEnum.CONFIRMED.name()) ||
					t.getStatus().name().equals(GlobalAppStatusEnum.PENDING.name()) ||
					t.getStatus().name().equals(GlobalAppStatusEnum.REFUSED.name());
		} else {
			filter = t -> t.getStatus().name().equals(GlobalAppStatusEnum.CONFIRMED.name());
		}
		
		return this.teamMembersRepository
				.findByUserUuidAndStatusNotInAndAdminFalseAndTeamDeletedFalse(userId, SppotiUtils.statusToFilter(),
						pageable).stream().filter(filter).map(t -> this.teamTransformer.modelToDto(t.getTeam()))
				.collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> getAllDeletedTeamsByUserId(final String userId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		final List<TeamMemberEntity> myTeams = this.teamMembersRepository
				.findByUserUuidAndStatusNotInAndTeamDeletedFalseAndTeamDeletedFalse(getConnectedUserUuid(),
						SppotiUtils.statusToFilter(), pageable);
		
		return myTeams.stream().map(t -> fillTeamResponse(t.getTeam(), null)).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public TeamDTO responseToSppotiAdminChallenge(final SppotiDTO dto, final String teamId)
	{
		
		checkTeamAdminAccess(teamId);
		
		final Optional<SppotiEntity> optional = Optional.ofNullable(this.sppotiRepository.findByUuid(dto.getId()));
		
		if (optional.isPresent()) {
			final SppotiEntity sp = optional.get();
			//Check if sppoti has already a CONFIRMED adverse adverseTeam in the adverse adverseTeam list.
			if (sp.getAdverseTeams() != null &&
					sp.getAdverseTeams().stream().anyMatch(t -> t.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))) {
				throw new BusinessGlobalException("This sppoti has already an adverse adverseTeam");
			}
			
			//Check if adverseTeam exists.
			final TeamEntity adverseTeam = getTeamIfExists(teamId);
			
			//Check if adverseTeam is not already accepted as challenger of this sppoti.
			final Optional<SppotiAdverseEntity> sppotiAdverseEntityOptional = sp.getAdverseTeams().stream()
					.filter(t -> t.getTeam().getId().equals(adverseTeam.getId())).findAny();
			
			//Team not found in sppoti adverse adverseTeam list.
			sppotiAdverseEntityOptional
					.orElseThrow(() -> new BusinessGlobalException("This adverseTeam has no challenge to respond."));
			
			//Team found in awaiting list.
			if (sppotiAdverseEntityOptional.isPresent()) {
				final SppotiAdverseEntity t = sppotiAdverseEntityOptional.get();
				//answer to sppoti admin challenge request
				if (t.getFromSppotiAdmin().equals(Boolean.TRUE)) {
					//Team already CONFIRMED.
					if (t.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
						throw new BusinessGlobalException(
								"This adverseTeam is already selected to challenge the host adverseTeam");
					}
					//Team already REFUSED.
					else if (t.getStatus().equals(GlobalAppStatusEnum.REFUSED)) {
						throw new BusinessGlobalException(
								"This adverseTeam was already refused to challenge the host adverseTeam");
					} else {
						//Team exist in adverse adverseTeam list in PENDING mode.
						if (dto.getTeamAdverseStatus().equals(GlobalAppStatusEnum.CONFIRMED.name())) {
							t.setStatus(GlobalAppStatusEnum.valueOf(dto.getTeamAdverseStatus()));
							
							//Convert adverseTeam members to sppoters if status equals to confirmed
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
							this.notificationService.saveAndSendNotificationToUsers(null, sp.getUserSppoti(), TEAM,
									NotificationTypeEnum.TEAM_ADMIN_ACCEPTED_YOUR_CHALLENGE, t.getTeam(), sp);
							
							sendEmailOnChallengeAction(adverseTeam, sp.getTeamHostEntity(), sp, 3);
							
							//save changes and return adverseTeam.
							return this.teamTransformer.modelToDto(this.sppotiAdverseRepository.save(t).getTeam());
						} else {
							//Challenge refused -> Delete row from database.
							sp.getAdverseTeams().remove(t);
							this.sppotiRepository.save(sp);
							
							//send notification to sppoti admin.
							this.notificationService.saveAndSendNotificationToUsers(null, sp.getUserSppoti(), TEAM,
									NotificationTypeEnum.TEAM_ADMIN_REFUSED_YOUR_CHALLENGE, t.getTeam(), sp);
							
							sendEmailOnChallengeAction(adverseTeam, sp.getTeamHostEntity(), sp, 2);
							
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
						this.notificationService.saveAndSendNotificationToUsers(null, sp.getUserSppoti(), TEAM,
								NotificationTypeEnum.TEAM_ADMIN_CANCELED_HIS_CHALLENGE, t.getTeam(), sp);
						
						sendEmailOnChallengeAction(adverseTeam, sp.getTeamHostEntity(), sp, 4);
						
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
	 * {@inheritDoc}
	 */
	@Override
	public List<SppotiDTO> findAllPendingChallenges(final String teamId, final int page)
	{
		
		//Check if user is team admin.
		checkTeamAdminAccess(teamId);
		
		//Check if team exists.
		getTeamIfExists(teamId);
		
		final List<SppotiAdverseEntity> sppotiAdverseEntities = this.sppotiAdverseRepository
				.findByTeamUuidAndFromSppotiAdminTrue(teamId);
		
		return sppotiAdverseEntities.stream().filter(v -> v.getStatus().equals(GlobalAppStatusEnum.PENDING))
				.map(ad -> this.sppotiTransformer.modelToDto(ad.getSppoti())).collect(Collectors.toList());
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TeamDTO> findAllAdverseTeamsAllowedToBeChallengedBySppotiAdmin(final String sppotiId, final String team,
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
	public List<TeamDTO> findAllAllowedTeamsToChallengeSppoti(final Long userId, final String sppotiId, final int page)
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
					.findByTeamSportIdAndUserIdAndStatusNotInAndAdminTrueAndTeamDeletedFalse(sppoti.getSport().getId(),
							userId, SppotiUtils.statusToFilter(), pageable).stream()
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
	public List<TeamDTO> findAllTeamsBySportType(final Long sportId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.teamPageSize, Sort.Direction.DESC, "invitationDate");
		
		return this.teamMembersRepository
				.findByTeamSportIdAndUserIdAndStatusNotInAndAdminTrueAndTeamDeletedFalse(sportId,
						getConnectedUser().getId(), SppotiUtils.statusToFilter(), pageable).stream()
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
				.findByUserUuidAndTeamNameContainingAndStatusNotInAndTeamDeletedFalse(getConnectedUserUuid(), team,
						SppotiUtils.statusToFilter(), pageable);
		
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
				.findByTeamSportIdAndUserUuidAndTeamNameContainingAndStatusNotInAndTeamDeletedFalse(sport,
						getConnectedUserUuid(), team, SppotiUtils.statusToFilter(), pageable);
		
		return myTeams.stream().map(t -> fillTeamResponse(t.getTeam(), null)).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public TeamDTO updateTeamType(final String teamId, final TeamStatus type) {
		
		final TeamEntity teamEntity = getTeamIfExists(teamId);
		
		checkTeamAdminAccess(teamId);
		
		teamEntity.setType(type);
		return this.teamTransformer.modelToDto(this.teamRepository.save(teamEntity));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public TeamDTO sendRequestToJoinTeam(final String teamId) {
		
		final TeamEntity teamEntity = getTeamIfExists(teamId);
		
		final UserEntity entity = getConnectedUser();
		
		if (this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndTeamDeletedFalse(entity.getUuid(), teamId,
						SppotiUtils.statusToFilter()) != null) {
			throw new EntityExistsException("Already a member");
		}
		
		final TeamEntity t = teamEntity;
		final TeamMemberEntity m = new TeamMemberEntity();
		m.setStatus(GlobalAppStatusEnum.PENDING);
		m.setRequestSentFromUser(Boolean.TRUE);
		m.setUser(entity);
		m.setTeam(t);
		t.getTeamMembers().add(m);
		return this.teamTransformer.modelToDto(this.teamRepository.save(t));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void confirmTeamRequestSentFromUser(final String teamId, final UserDTO dto) {
		
		checkTeamAdminAccess(teamId);
		
		getTeamIfExists(teamId);
		
		final Optional<TeamMemberEntity> memberEntity = this.teamMembersRepository
				.findByTeamUuidAndUserUuidAndStatusAndRequestSentFromUserTrueAndTeamDeletedFalse(teamId, dto.getId(),
						GlobalAppStatusEnum.PENDING);
		
		memberEntity.ifPresent(m -> {
			m.setStatus(GlobalAppStatusEnum.CONFIRMED);
			this.teamMembersRepository.save(m);
		});
		
		memberEntity.orElseThrow(() -> new EntityNotFoundException("No request to confirm for the given parameters"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void refuseTeamRequestSentFromUser(final String teamId, final UserDTO dto) {
		
		checkTeamAdminAccess(teamId);
		
		getTeamIfExists(teamId);
		
		final Optional<TeamMemberEntity> memberEntity = this.teamMembersRepository
				.findByTeamUuidAndUserUuidAndStatusAndRequestSentFromUserTrueAndTeamDeletedFalse(teamId, dto.getId(),
						GlobalAppStatusEnum.PENDING);
		
		memberEntity.ifPresent(m -> {
			m.setStatus(GlobalAppStatusEnum.REFUSED);
			this.teamMembersRepository.save(m);
		});
		
		memberEntity.orElseThrow(() -> new EntityNotFoundException("No request to refuse for the given parameters."));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void leaveTeam(final String teamId) {
		
		getTeamIfExists(teamId);
		
		final Optional<TeamMemberEntity> optional = this.teamMembersRepository
				.findByTeamUuidAndUserUuidAndStatusAndTeamDeletedFalse(teamId, getConnectedUserUuid(),
						GlobalAppStatusEnum.CONFIRMED);
		
		optional.ifPresent(m -> {
			final TeamMemberEntity memberEntity = optional.get();
			memberEntity.setStatus(GlobalAppStatusEnum.LEFT);
			memberEntity.getSppotiMembers().stream().filter(s -> !s.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))
					.forEach(s -> s.setStatus(GlobalAppStatusEnum.CANCELED));
			this.teamMembersRepository.save(memberEntity);
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("You're not a member of this team."));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void cancelJoinTeamRequest(final String teamId) {
		
		getTeamIfExists(teamId);
		
		final Optional<TeamMemberEntity> memberEntity = this.teamMembersRepository
				.findByTeamUuidAndUserUuidAndStatusAndRequestSentFromUserTrueAndTeamDeletedFalse(teamId,
						getConnectedUserUuid(), GlobalAppStatusEnum.PENDING);
		
		memberEntity.ifPresent(m -> {
			m.setStatus(GlobalAppStatusEnum.DELETED);
			this.teamMembersRepository.save(m);
		});
		
		memberEntity.orElseThrow(() -> new EntityNotFoundException("You have no requests to cancel"));
	}
	
	/**
	 * Allow access only for team admin.
	 *
	 * @param teamId
	 * 		team id.
	 */
	private void checkTeamAdminAccess(final String teamId)
	{
		if (this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(getConnectedUserUuid(), teamId,
						SppotiUtils.statusToFilter()) == null) {
			throw new NotAdminException("You must be the team admin to access this service");
		}
	}
	
	/**
	 * Check if team exists or not, if exists return the team, if not throw a {@link BusinessGlobalException}
	 *
	 * @param teamId
	 * 		id of the team.
	 *
	 * @return the team.
	 */
	private TeamEntity getTeamIfExists(final String teamId) {
		final List<TeamEntity> entity = this.teamRepository.findByUuidAndDeletedFalse(teamId);
		
		if (entity.isEmpty()) {
			throw new EntityNotFoundException(ErrorMessageEnum.TEAM_NOT_FOUND.getMessage());
		}
		
		return entity.get(0);
	}
	
}
