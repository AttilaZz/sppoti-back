package com.fr.impl;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.*;
import com.fr.mail.SppotiMailer;
import com.fr.service.SppotiControllerService;
import com.fr.transformers.SppotiTransformer;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.SportTransformer;
import com.fr.transformers.impl.TeamMemberTransformer;
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
import java.util.stream.Collectors;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
class SppotiControllerServiceImpl extends AbstractControllerServiceImpl implements SppotiControllerService
{
	
	/**
	 * Returned sppoti list size.
	 */
	@Value("${key.sppotiesPerPage}")
	private int sppotiSize;
	
	/**
	 * {@link SportEntity} transformer.
	 */
	private final SportTransformer sportTransformer;
	
	/**
	 * {@link SppotiEntity} transformer.
	 */
	private final SppotiTransformer sppotiTransformer;
	
	/**
	 * {@link TeamMemberEntity} transformer.
	 */
	private final TeamMemberTransformer teamMemberTransformer;
	
	/**
	 * {@link UserEntity} transformer
	 */
	private final UserTransformer userTransformer;
	
	/**
	 * Sppoti mailer.
	 */
	private final SppotiMailer sppotiMailer;
	
	/**
	 * Init services.
	 */
	@Autowired
	public SppotiControllerServiceImpl(final SportTransformer sportTransformer,
									   final SppotiTransformer sppotiTransformer,
									   final TeamMemberTransformer teamMemberTransformer,
									   final UserTransformer userTransformer, final SppotiMailer sppotiMailer)
	{
		this.sportTransformer = sportTransformer;
		this.sppotiTransformer = sppotiTransformer;
		this.teamMemberTransformer = teamMemberTransformer;
		this.userTransformer = userTransformer;
		this.sppotiMailer = sppotiMailer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public SppotiDTO saveSppoti(final SppotiDTO newSppoti)
	{
		
		TeamEntity hostTeam = new TeamEntity();
		final SppotiEntity sppoti = this.sppotiTransformer.dtoToModel(newSppoti);
		sppoti.setUserSppoti(getConnectedUser());
		final Set<SppotiEntity> sppotiEntities = new HashSet<>();
		sppotiEntities.add(sppoti);
		hostTeam.setSppotiEntity(sppotiEntities);
		
		final TeamDTO teamDTO = newSppoti.getTeamHost();
		if (teamDTO != null) {
			
			if (StringUtils.hasText(teamDTO.getName())) {
				hostTeam.setName(teamDTO.getName());
			}
			
			if (StringUtils.hasText(teamDTO.getName())) {
				hostTeam.setLogoPath(teamDTO.getLogoPath());
			}
			
			if (StringUtils.hasText(teamDTO.getName())) {
				hostTeam.setCoverPath(teamDTO.getCoverPath());
			}
			
			hostTeam.setTeamMembers(getTeamMembersEntityFromDto(teamDTO.getMembers(), hostTeam, sppoti));
			hostTeam.setSport(sppoti.getSport());
			//            teamRepository.save(hostTeam);
			
		} else if (newSppoti.getMyTeamId() != 0) {
			
			final List<TeamEntity> tempTeams = this.teamRepository.findByUuidAndDeletedFalse(newSppoti.getMyTeamId());
			
			if (tempTeams == null || tempTeams.isEmpty()) {
				throw new EntityNotFoundException("Host team not found in the request");
			}
			hostTeam = tempTeams.get(0);
			
			if (!hostTeam.getSport().equals(sppoti.getSport())) {
				throw new BusinessGlobalException("Host team sport not as same as sppoti sport !");
			}
			
			//Convert team members to sppoters.
			final Set<SppoterEntity> sppotiMembers = hostTeam.getTeamMembers().stream().map(sm -> {
						SppoterEntity sppotiMember = new SppoterEntity();
						sppotiMember.setTeamMember(sm);
						sppotiMember.setSppoti(sppoti);
						if (getConnectedUser().getId().equals(sm.getUsers().getId())) {
							sppotiMember.setStatus(GlobalAppStatusEnum.CONFIRMED);
						}
						return sppotiMember;
					}
			
			).collect(Collectors.toSet());
			sppoti.setSppotiMembers(sppotiMembers);
			
		} else {
			throw new EntityNotFoundException("Host team not found in the request");
		}
		
		sppoti.setTeamHostEntity(hostTeam);
		
		final SppotiEntity savedSppoti = this.sppotiRepository.save(sppoti);
		savedSppoti.setConnectedUserId(getConnectedUser().getId());
		//If team has been saved with the sppoti, send email to its members.
		if (teamDTO != null) {
			final TeamEntity team = savedSppoti.getTeamHostEntity();
			team.getTeamMembers().forEach(m -> {
				if (!m.getAdmin().equals(Boolean.TRUE)) {
					sendJoinTeamEmail(team, getUserById(m.getUsers().getId()),
							this.teamMembersRepository.findByTeamUuidAndAdminTrue(team.getUuid()));
				}
			});
		}
		final SppotiDTO sppotiDTO = this.sppotiTransformer.modelToDto(savedSppoti);
		//send email to sppoters.
		sppoti.getTeamHostEntity().getTeamMembers().forEach(m -> {
			//exclude sppoti admin from the email.
			if (!m.getUsers().getId().equals(sppoti.getUserSppoti().getId())) {
				new Thread(() -> this.sppotiMailer
						.sendJoinSppotiEmail(sppotiDTO, this.userTransformer.modelToDto(m.getUsers()),
								this.userTransformer.modelToDto(sppoti.getUserSppoti()))).start();
			}
		});
		return sppotiDTO;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SppotiDTO getSppotiByUuid(final Integer uuid)
	{
		
		final SppotiEntity sppoti = this.sppotiRepository.findByUuid(uuid);
		
		if (sppoti != null && sppoti.isDeleted()) {
			throw new EntityNotFoundException("Trying to get a deleted sppoti");
		}
		
		return getSppotiResponse(sppoti);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deleteSppoti(final int id)
	{
		
		final Optional<SppotiEntity> sppoti = Optional.ofNullable(this.sppotiRepository.findByUuid(id));
		
		sppoti.ifPresent(s -> {
			s.setDeleted(true);
			this.sppotiRepository.save(s);
		});
		
		sppoti.orElseThrow(() -> new EntityNotFoundException("Trying to delete non existing sppoti"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	@Transactional
	@Override
	public SppotiDTO updateSppoti(final SppotiDTO sppotiRequest, final int id)
	{
		
		final SppotiEntity sppoti = this.sppotiRepository.findByUuid(id);
		if (sppoti == null)
			throw new EntityNotFoundException("SppotiEntity not found with id: " + id);
		
		if (StringUtils.hasText(sppotiRequest.getTags())) {
			sppoti.setTags(sppotiRequest.getTags());
		}
		
		if (StringUtils.hasText(sppotiRequest.getDescription())) {
			sppoti.setDescription(sppotiRequest.getDescription());
		}
		
		if (sppotiRequest.getDateTimeStart() != null) {
			sppoti.setDateTimeStart(sppotiRequest.getDateTimeStart());
		}
		
		if (StringUtils.hasText(sppotiRequest.getName())) {
			sppoti.setName(sppotiRequest.getName());
		}
		
		if (StringUtils.hasText(sppotiRequest.getLocation())) {
			sppoti.setLocation(sppotiRequest.getLocation());
		}
		
		if (sppotiRequest.getMaxTeamCount() != null && sppotiRequest.getMaxTeamCount() != 0) {
			sppoti.setMaxTeamCount(sppotiRequest.getMaxTeamCount());
		}
		
		if (sppotiRequest.getVsTeam() != null && sppotiRequest.getVsTeam() != 0) {
			final List<TeamEntity> adverseTeam = this.teamRepository.findByUuidAndDeletedFalse(sppotiRequest.getVsTeam());
			
			//check if adverse team exist
			if (adverseTeam.isEmpty()) {
				throw new EntityNotFoundException("TeamEntity id not found: " + sppotiRequest.getVsTeam());
			}
			final TeamEntity team = adverseTeam.get(0);
			
			if (!team.getSport().getId().equals(sppoti.getSport().getId())) {
				throw new BusinessGlobalException("Adverse team sport not as same as sppoti sport !");
			}
			
			//check if adverse team already exist.
			if (sppoti.getAdverseTeams().stream().anyMatch(a -> a.getTeam().getId().equals(team.getId()))) {
				throw new EntityExistsException("This team was already challenged.");
			}
			
			//check if adverse team members are not in conflict with team host members
			sppoti.getTeamHostEntity().getTeamMembers()
					.forEach(hostMember -> team.getTeamMembers().forEach(adverseMember -> {
						if (hostMember.getUsers().getId().equals(adverseMember.getUsers().getId())) {
							throw new BusinessGlobalException(
									"Conflict found between host team members and adverse team members");
						}
					}));
			
			// Add team in sppoti adverse teams list
			final SppotiAdverseEntity adverse = new SppotiAdverseEntity();
			adverse.setSppoti(sppoti);
			adverse.setTeam(team);
			adverse.setFromSppotiAdmin(Boolean.TRUE);
			sppoti.getAdverseTeams().add(adverse);
		}
		
		final SppotiEntity updatedSppoti = this.sppotiRepository.save(sppoti);
		
		return getSppotiResponse(updatedSppoti);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void acceptSppoti(final int sppotiId, final int userId)
	{
		
		final Optional<SppoterEntity> optional = Optional
				.ofNullable(this.sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(userId, sppotiId));
		
		optional.ifPresent(sm -> {
			//update sppoter status.
			sm.setStatus(GlobalAppStatusEnum.CONFIRMED);
			final SppoterEntity updatedSppoter = this.sppotiMembersRepository.save(sm);
			
			//Send notification to sppoti admin.
			if (updatedSppoter != null) {
				addNotification(NotificationTypeEnum.X_ACCEPTED_YOUR_SPPOTI_INVITATION, sm.getTeamMember().getUsers(),
						sm.getSppoti().getUserSppoti(), null, null);
			}
			
			//update team member status.
			final TeamMemberEntity teamMembers = sm.getTeamMember();
			if (!teamMembers.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
				teamMembers.setStatus(GlobalAppStatusEnum.CONFIRMED);
			}
			
			final TeamMemberEntity updatedTeamMember = this.teamMembersRepository.save(teamMembers);
			
			//Send notification to team admin.
			if (updatedTeamMember != null && !teamMembers.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
				
				final UserEntity teamAdmin = this.teamMembersRepository
						.findByTeamUuidAndAdminTrue(teamMembers.getTeam().getUuid()).getUsers();
				
				addNotification(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION, sm.getTeamMember().getUsers(),
						teamAdmin, teamMembers.getTeam(), null);
			}
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Sppoter not found"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void refuseSppoti(final int sppotiId, final int userId)
	{
		
		final SppoterEntity sppotiMembers = this.sppotiMembersRepository
				.findByTeamMemberUsersUuidAndSppotiUuid(sppotiId, userId);
		
		if (sppotiMembers == null) {
			throw new EntityNotFoundException("Sppoter not found");
		}
		
		sppotiMembers.setStatus(GlobalAppStatusEnum.REFUSED);
		final SppoterEntity updatedSppoter = this.sppotiMembersRepository.save(sppotiMembers);
		
		//Send notification to sppoti admin.
		if (updatedSppoter != null) {
			addNotification(NotificationTypeEnum.X_REFUSED_YOUR_SPPOTI_INVITATION,
					sppotiMembers.getTeamMember().getUsers(), sppotiMembers.getSppoti().getUserSppoti(), null, null);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SppotiDTO> getAllUserSppoties(final Integer id, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "datetimeCreated");
		
		final List<SppotiEntity> sppoties = this.sppotiRepository.findByUserSppotiUuid(id, pageable);
		
		return sppoties.stream()
				//.filter(s -> !s.getTeamAdverseStatusEnum().equals(GlobalAppStatusEnum.REFUSED))
				.map(this::getSppotiResponse).collect(Collectors.toList());
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public SppotiDTO sendChallenge(final int sppotiId, final int teamId, final Long connectedUserId)
	{
		//Check if team exist.
		final List<TeamEntity> teamEntities = this.teamRepository.findByUuidAndDeletedFalse(teamId);
		if (teamEntities.isEmpty()) {
			throw new EntityNotFoundException("Team not found (" + teamId + ")");
		}
		final TeamEntity challengeTeam = teamEntities.get(0);
		
		//check if user has rights to send challenge. (team adverse admin)
		if (!connectedUserId.equals(this.teamMembersRepository.findByTeamUuidAndAdminTrue(teamId).getUsers().getId())) {
			throw new NotAdminException("You have to be the admin of the team to send challenge");
		}
		
		//check if sppoti exists.
		final SppotiEntity sppotiEntity = this.sppotiRepository.findByUuid(sppotiId);
		if (sppotiEntity == null) {
			throw new EntityNotFoundException("Sppoti not found (" + sppotiId + ")");
		}
		
		//check if team sport is as same as sppoti sport.
		if (!challengeTeam.getSport().getId().equals(sppotiEntity.getSport().getId())) {
			throw new BusinessGlobalException("challenged team sport not as same as sppoti sport !");
		}
		
		//Check if sppoti has already a CONFIRMED adverse team in the adverse team list.
		if (sppotiEntity.getAdverseTeams() != null && sppotiEntity.getAdverseTeams().stream()
				.anyMatch(t -> t.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))) {
			throw new BusinessGlobalException("This sppoti has already an adverse team");
		}
		
		//Check if team adverse was not already challenged.
		if (sppotiEntity.getAdverseTeams().stream().anyMatch(t -> Integer.compare(t.getTeam().getUuid(), teamId) == 0 &&
				t.getStatus().equals(GlobalAppStatusEnum.PENDING))) {
			throw new EntityExistsException("Challenge already sent to this team");
		}
		
		//check if adverse team members are not in conflict with team host members
		sppotiEntity.getTeamHostEntity().getTeamMembers()
				.forEach(hostMember -> challengeTeam.getTeamMembers().forEach(adverseMember -> {
					if (hostMember.getUsers().getId().equals(adverseMember.getUsers().getId())) {
						throw new BusinessGlobalException(
								"Conflict found between host team members and adverse team members");
					}
				}));
		
		final SppotiAdverseEntity adverse = new SppotiAdverseEntity();
		adverse.setSppoti(sppotiEntity);
		adverse.setTeam(challengeTeam);
		sppotiEntity.getAdverseTeams().add(adverse);
		
		//update sppoti.
		final SppotiEntity savedSppoti = this.sppotiRepository.save(sppotiEntity);
		
		return getSppotiResponse(savedSppoti);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void chooseOneAdverseTeamFromAllRequests(final int sppotiId, final TeamDTO teamDTO)
	{
		
		//Check if sppoti exist and has no confirmed adverse team yet.
		final Optional<SppotiEntity> sppotiEntityOptional = Optional
				.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		sppotiEntityOptional.orElseThrow(() -> new EntityNotFoundException("Sppoti (" + sppotiId + ") not found !"));
		
		sppotiEntityOptional.ifPresent(sp -> {
			//check if sppoti has already an adverse team.
			if (sp.getAdverseTeams().stream().anyMatch(ad -> ad.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))) {
				throw new BusinessGlobalException("This sppoti has already an adverse team");
			}
			
			//check if team exist, and it's already pending id sppoti adverse teams.
			final List<TeamEntity> teamEntities = this.teamRepository.findByUuidAndDeletedFalse(teamDTO.getId());
			if (teamEntities.isEmpty()) {
				throw new EntityNotFoundException("Team (" + teamDTO.getId() + ") not found");
			}
			teamEntities.stream().findFirst().ifPresent(tad -> {
				//check if connected user has rights to answer the challenge.
				if (!this.getConnectedUser().getId().equals(sp.getUserSppoti().getId())) {
					throw new NotAdminException("Only sppoti admin can answer to this challenge!");
				}
				
				//get selected adverse team from all requests.
				sp.getAdverseTeams().stream().filter(a -> a.getTeam().getId().equals(tad.getId())).findFirst()
						.ifPresent(teamAdverse -> {
							//update team adverse status if CONFIRMED, delete row if REFUSED
							if (teamDTO.getTeamAdverseStatus().equals(GlobalAppStatusEnum.REFUSED.name())) {
								sp.getAdverseTeams().remove(teamAdverse);
								this.sppotiRepository.save(sp);
							} else {
								teamAdverse.setStatus(GlobalAppStatusEnum.valueOf(teamDTO.getTeamAdverseStatus()));
								this.sppotiRepository.save(sp);
								
								//get team adverse admin.
								final UserEntity teamAdverseAdmin = tad.getTeamMembers().stream()
										.filter(t -> t.getAdmin().equals(true) &&
												t.getTeam().getUuid() == teamDTO.getId()).findFirst().get().getUsers();
								
								//notify team adverse admin.
								addNotification(NotificationTypeEnum.X_ACCEPTED_YOUR_SPPOTI_INVITATION,
										sp.getUserSppoti(), teamAdverseAdmin, null, sp);
								
								//Convert team members to sppoters if status equals to confirmed
								if (GlobalAppStatusEnum.valueOf(teamDTO.getTeamAdverseStatus())
										.equals(GlobalAppStatusEnum.CONFIRMED)) {
									final Set<SppoterEntity> sppotiMembers = convertAdverseTeamMembersToSppoters(
											teamAdverse.getTeam(), sp, true);
									sp.setSppotiMembers(sppotiMembers);
									this.sppotiRepository.save(sp);
								}
							}
						});
			});
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<UserDTO> rateSppoters(final List<SppotiRatingDTO> sppotiRatingDTO, final int sppotiId)
	{
		
		final Optional<SppotiEntity> sppotiEntity = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		if (sppotiEntity.isPresent()) {
			final SppotiEntity se = sppotiEntity.get();
			
			return sppotiRatingDTO.stream().map(sppoter -> {
				Optional<SppoterEntity> ratedSppoter = Optional.ofNullable(this.sppotiMembersRepository
						.findByTeamMemberUsersUuidAndSppotiUuid(sppoter.getSppoterRatedId(), se.getUuid()));
				ratedSppoter.orElseThrow(
						() -> new EntityNotFoundException("Sppoter (" + sppoter.getSppoterRatedId() + ") not found"));
				
				SppoterEntity rs = ratedSppoter.get();
				//Deny rating of a sppoter who hasn't accepted sppoti yet !
				if (rs.getStatus().equals(GlobalAppStatusEnum.PENDING)) {
					throw new BusinessGlobalException("Sppoter (" + sppoter.getId() + ") hasn't accepted sppoti yet");
				}
				
				UserEntity connectUser = getConnectedUser();
				
				//FIXME: create transformer for rating
				SppotiRatingEntity sppotiRatingEntity = new SppotiRatingEntity();
				sppotiRatingEntity.setSppotiEntity(se);
				sppotiRatingEntity.setRatedSppoter(rs.getTeamMember().getUsers());
				sppotiRatingEntity.setRatingDate(new Date());
				sppotiRatingEntity.setRaterSppoter(connectUser);
				sppotiRatingEntity.setStarsCount(sppoter.getStars());
				this.ratingRepository.save(sppotiRatingEntity);
				
				//Flag rated sppoter to true.
				//This means that sppoti admin has rate this sppoter
				SppoterEntity sppoterRaterEntity = this.sppotiMembersRepository
						.findByTeamMemberUsersUuidAndSppotiUuid(connectUser.getUuid(), sppotiId);
				sppoterRaterEntity.setHasRateOtherSppoter(Boolean.TRUE);
				this.sppotiMembersRepository.save(sppoterRaterEntity);
				
				//Get RATED SPPOTER.
				SppoterEntity sppoterRatedEntity = this.sppotiMembersRepository
						.findByTeamMemberUsersUuidAndSppotiUuid(sppoter.getSppoterRatedId(), sppotiId);
				return this.teamMemberTransformer.modelToDto(sppoterRatedEntity.getTeamMember(), se);
				
			}).collect(Collectors.toList());
		}
		
		throw new EntityNotFoundException("Sppoti not found");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void isSppotiAdmin(final int sppotiId, final Long userId)
	{
		final SppotiEntity sppotiEntity = this.sppotiRepository.findByUuid(sppotiId);
		if (sppotiEntity == null) {
			throw new EntityNotFoundException("Sppoti (" + sppotiId + ") not found !");
		}
		
		if (!sppotiEntity.getUserSppoti().getId().equals(userId)) {
			throw new NotAdminException("You must be the sppoti admin to continue");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SppotiDTO> getAllJoinedSppoties(final int userId, final int page)
	{
		
		CheckConnectedUserAccessPrivileges(userId);
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "invitationDate");
		
		final List<SppoterEntity> sppotiMembers = this.sppotiMembersRepository
				.findByTeamMemberUsersUuid(userId, pageable);
		
		return sppotiMembers.stream().filter(s -> s.getSppoti().getUserSppoti().getUuid() != userId)
				.map(s -> getSppotiResponse(s.getSppoti())).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<SppotiDTO> getAllConfirmedSppoties(final int userId, final int page)
	{
		
		CheckConnectedUserAccessPrivileges(userId);
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "invitationDate");
		
		return this.sppotiMembersRepository.findByTeamMemberUsersUuid(userId, pageable).stream()
				.filter(m -> m.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))
				.map(s -> getSppotiResponse(s.getSppoti())).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<SppotiDTO> getAllRefusedSppoties(final int userId, final int page)
	{
		
		CheckConnectedUserAccessPrivileges(userId);
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "invitationDate");
		
		return this.sppotiMembersRepository.findByTeamMemberUsersUuid(userId, pageable).stream()
				.filter(m -> m.getStatus().equals(GlobalAppStatusEnum.REFUSED))
				.map(s -> getSppotiResponse(s.getSppoti())).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SppotiDTO> getAllUpcomingSppoties(final int userId, final int page)
	{
		
		CheckConnectedUserAccessPrivileges(userId);
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "datetimeCreated");
		
		return this.sppotiRepository.findByUserSppotiUuid(userId, pageable).stream().filter(s ->
				s.getAdverseTeams().stream().anyMatch(
						t -> t.getStatus().name().equals(GlobalAppStatusEnum.CONFIRMED.name()) ||
								(t.getStatus().name().equals(GlobalAppStatusEnum.CONFIRMED.name()) &&
										t.getTeam().getTeamMembers().stream().anyMatch(
												am -> Integer.compare(am.getUsers().getUuid(), userId) == 0))) ||
						s.getTeamHostEntity().getTeamMembers().stream().anyMatch(t -> t.getUsers().getUuid() == userId))
				.map(this.sppotiTransformer::modelToDto).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<SppotiDTO> getAllPendingChallengeRequestSppoties(final int userId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "invitationDate");
		
		final List<TeamMemberEntity> teamMemberEntities = this.teamMembersRepository
				.findByUsersUuidAndAdminTrue(userId, pageable);
		
		final List<SppotiDTO> result = new ArrayList<>();
		
		teamMemberEntities.stream().map(t -> this.sppotiAdverseRepository.findByTeamUuid(t.getTeam().getUuid()))
				.forEach(k -> k.forEach(a -> {
					if (GlobalAppStatusEnum.PENDING.equals(a.getStatus())) {
						final SppotiEntity sp = a.getSppoti();
						final UserEntity connecteUser = getConnectedUser();
						sp.setConnectedUserId(connecteUser.getId());
						
						final SppotiDTO sppotiDTO = this.sppotiTransformer.modelToDto(sp);
						
						sppotiDTO.setTeamAdverse(
								sppotiDTO.getTeamAdverse().stream().filter(t -> t.getId().equals(a.getTeam().getUuid()))
										.collect(Collectors.toList()));
						result.add(sppotiDTO);
					}
				}));
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> findAllSppoterAllowedToJoinSppoti(final String prefix, final int sppotiId, final int page)
	{
		final Optional<SppotiEntity> optional = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		
		//sppoti exists.
		if (optional.isPresent()) {
			final SppotiEntity sp = optional.get();
			final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "invitationDate");
			
			return this.sppotiMembersRepository.findAllAllowedSppoter(prefix, pageable).stream()
					.map(s -> this.teamMemberTransformer.modelToDto(s.getTeamMember(), s.getSppoti()))
					.collect(Collectors.toList());
		}
		
		throw new EntityNotFoundException("Sppoti not found");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public UserDTO addSppoter(final int sppotiId, final UserDTO user)
	{
		
		final Optional<SppotiEntity> sppotiEntity = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		
		if (sppotiEntity.isPresent()) {
			
			final SppotiEntity userSppoti = sppotiEntity.get();
			final TeamEntity userTeam = userSppoti.getTeamHostEntity();
			final UserEntity userSppoter = getUserByUuId(user.getId());
			
			if (userSppoter == null) {
				throw new EntityNotFoundException("UserDTO with id (" + user.getId() + ") Not found");
			}
			
			if (!userSppoti.getUserSppoti().getId().equals(getConnectedUser().getId())) {
				throw new NotAdminException("You must be the sppoti admin to access this service");
			}
			
			final TeamMemberEntity teamMembers = new TeamMemberEntity();
			final SppoterEntity sppoter = new SppoterEntity();
			
			//team member.
			teamMembers.setTeam(userTeam);
			teamMembers.setUsers(userSppoter);
			
			//sppoter.
			sppoter.setTeamMember(teamMembers);
			sppoter.setSppoti(userSppoti);
			
			final Set<SppoterEntity> sppotiMembers = new HashSet<>();
			sppotiMembers.add(sppoter);
			
			//Link sppoter with team member.
			teamMembers.setSppotiMembers(sppotiMembers);
			
			//add coordinate if set.
			if (StringUtils.isEmpty(user.getxPosition())) {
				teamMembers.setxPosition(user.getxPosition());
				sppoter.setxPosition(user.getxPosition());
			}
			
			if (StringUtils.isEmpty(user.getyPosition())) {
				teamMembers.setyPosition(user.getyPosition());
				sppoter.setyPosition(user.getyPosition());
			}
			
			//save new member and sppoter.
			final TeamMemberEntity savedMember = this.teamMembersRepository.save(teamMembers);
			
			//Send email to the new team member.
			//sendJoinTeamEmail(team, sppoter, teamAdmin);
			
			//return new member.
			return this.teamMemberTransformer.modelToDto(savedMember, userSppoti);
		}
		
		throw new EntityNotFoundException("Sppoti (" + sppotiId + ") not found");
		
	}
	
	/**
	 * Map sppoti entity to DTO.
	 *
	 * @param sppoti
	 * 		sppoti to retuen.
	 *
	 * @return sppoti DTO.
	 */
	private SppotiDTO getSppotiResponse(final SppotiEntity sppoti)
	{
		
		if (sppoti == null) {
			throw new EntityNotFoundException("SppotiEntity not found");
		}
		
		sppoti.setConnectedUserId(getConnectedUser().getId());
		final SppotiDTO sppotiDTO = this.sppotiTransformer.modelToDto(sppoti);
		
		if (StringUtils.hasText(sppoti.getDescription())) {
			sppotiDTO.setDescription(sppoti.getDescription());
		}
		
		if (StringUtils.hasText(sppoti.getTags())) {
			sppotiDTO.setTags(sppoti.getTags());
		}
		
		final TeamDTO teamHostResponse = fillTeamResponse(sppoti.getTeamHostEntity(), sppoti);
		
		sppotiDTO.setTeamHost(teamHostResponse);
		sppotiDTO.setId(sppoti.getUuid());
		sppotiDTO.setRelatedSport(this.sportTransformer.modelToDto(sppoti.getSport()));
		
		final List<SppoterEntity> sppotiMembers = this.sppotiMembersRepository
				.findByTeamMemberUsersUuidAndSppotiSportId(sppoti.getUserSppoti().getUuid(), sppoti.getSport().getId());
		
		sppotiDTO.setSppotiCounter(sppotiMembers.size());
		sppotiDTO.setMySppoti(getConnectedUser().getUuid() == sppoti.getUserSppoti().getUuid());
		
		//if user is member of a team, get admin of the tem and other informations.
		final TeamMemberEntity teamAdmin = this.teamMembersRepository
				.findByUsersUuidAndTeamUuidAndAdminTrue(sppoti.getUserSppoti().getUuid(),
						sppoti.getTeamHostEntity().getUuid());
		if (teamAdmin != null) {
			sppotiDTO.setAdminTeamId(teamAdmin.getUuid());
			sppotiDTO.setAdminUserId(sppoti.getUserSppoti().getUuid());
			sppotiDTO.setConnectedUserId(getConnectedUser().getUuid());
		}
		
		return sppotiDTO;
	}
	
	/**
	 * @param challengeTeam
	 * 		adverse team.
	 * @param sppoti
	 * 		sppoti id.
	 *
	 * @return all adverse team as sppoters.
	 */
	private Set<SppoterEntity> convertAdverseTeamMembersToSppoters(final TeamEntity challengeTeam,
																   final SppotiEntity sppoti,
																   final boolean fromAdverseTeam)
	{
		return challengeTeam.getTeamMembers().stream().map(sm -> {
					SppoterEntity sppotiMember = new SppoterEntity();
					sppotiMember.setTeamMember(sm);
					sppotiMember.setSppoti(sppoti);
					if (fromAdverseTeam) {
						if (sm.getAdmin())
							sppotiMember.setStatus(GlobalAppStatusEnum.CONFIRMED);
					} else {
						sppotiMember.setStatus(GlobalAppStatusEnum.PENDING);
					}
					return sppotiMember;
				}
		
		).collect(Collectors.toSet());
	}
}
