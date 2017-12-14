package com.fr.impl;

import com.fr.commons.dto.RatingDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.ErrorMessageEnum;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.SppotiStatus;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NotAdminException;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.enums.SppotiResponse;
import com.fr.repositories.SppotiRequestRepository;
import com.fr.service.NotificationBusinessService;
import com.fr.service.SppotiBusinessService;
import com.fr.service.email.SppotiMailerService;
import com.fr.transformers.SppotiTransformer;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.SportTransformer;
import com.fr.transformers.impl.TeamMemberTransformer;
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
import java.util.stream.Collectors;

import static com.fr.commons.enumeration.notification.NotificationObjectType.CHALLENGE;
import static com.fr.commons.enumeration.notification.NotificationObjectType.SPPOTI;
import static com.fr.commons.enumeration.notification.NotificationTypeEnum.*;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
class SppotiBusinessServiceImpl extends CommonControllerServiceImpl implements SppotiBusinessService
{
	private final Logger LOGGER = LoggerFactory.getLogger(SppotiBusinessServiceImpl.class);
	
	@Autowired
	private SportTransformer sportTransformer;
	@Autowired
	private SppotiTransformer sppotiTransformer;
	@Autowired
	private TeamMemberTransformer teamMemberTransformer;
	@Autowired
	private UserTransformer userTransformer;
	@Autowired
	private SppotiRequestRepository sppotiRequestRepository;
	@Autowired
	private SppotiMailerService sppotiMailerService;
	@Autowired
	private NotificationBusinessService notificationService;
	
	@Value("${key.sppotiesPerPage}")
	private int sppotiSize;
	
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
			hostTeam.setTimeZone(sppoti.getTimeZone());
			//            teamRepository.save(hostTeam);
			
		} else if (!StringUtils.isEmpty(newSppoti.getMyTeamId())) {
			
			final List<TeamEntity> tempTeams = this.teamRepository.findByUuidAndDeletedFalse(newSppoti.getMyTeamId());
			
			if (Objects.isNull(tempTeams)) {
				this.LOGGER.error("Host team id is invalid, team not found in database");
				throw new BusinessGlobalException();
			}
			hostTeam = tempTeams.get(0);
			
			if (!hostTeam.getSport().equals(sppoti.getSport())) {
				throw new BusinessGlobalException("Host team sport {} not as same as sppoti sport {}",
						hostTeam.getSport(), sppoti.getSport());
			}
			
			//Convert team members to sppoters.
			final Set<SppoterEntity> sppotiMembers = hostTeam.getTeamMembers().stream()
					.filter(tm -> !tm.getStatus().equals(GlobalAppStatusEnum.DELETED)).map(tm -> {
						SppoterEntity sppotiMember = new SppoterEntity();
						sppotiMember.setTeamMember(tm);
						sppotiMember.setSppoti(sppoti);
						if (getConnectedUser().getId().equals(tm.getUser().getId())) {
							sppotiMember.setStatus(GlobalAppStatusEnum.CONFIRMED);
						}
						return sppotiMember;
					}).collect(Collectors.toSet());
			sppoti.setSppotiMembers(sppotiMembers);
			
		} else {
			throw new BusinessGlobalException("Host team not found in the request");
		}
		
		sppoti.setTeamHostEntity(hostTeam);
		
		final SppotiEntity savedSppoti = this.sppotiRepository.save(sppoti);
		savedSppoti.setConnectedUserId(getConnectedUser().getId());
		//If team has been saved with the sppoti, send email to its members.
		if (teamDTO != null) {
			final TeamEntity team = savedSppoti.getTeamHostEntity();
			team.getTeamMembers().forEach(m -> {
				if (!m.getAdmin().equals(Boolean.TRUE)) {
					sendJoinTeamEmail(team, getUserById(m.getUser().getId()), this.teamMembersRepository
							.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(team.getUuid(),
									SppotiUtils.statusToFilter()));
				}
			});
		}
		
		final SppotiDTO sppotiDTO = this.sppotiTransformer.modelToDto(savedSppoti);
		//send email and notification to sppoters.
		sppoti.getTeamHostEntity().getTeamMembers().forEach(m -> {
			//exclude sppoti admin from the email.
			if (!m.getUser().getId().equals(sppoti.getUserSppoti().getId())) {
				//Email
				this.sppotiMailerService
						.sendJoinSppotiEmailToSppoters(sppotiDTO, this.userTransformer.modelToDto(m.getUser()),
								this.userTransformer.modelToDto(sppoti.getUserSppoti()));
				//Notification
				this.notificationService
						.saveAndSendNotificationToUsers(savedSppoti.getUserSppoti(), m.getUser(), SPPOTI,
								X_INVITED_YOU_TO_JOIN_HIS_SPPOTI, savedSppoti);
				
			}
		});
		
		this.LOGGER.info("Sppoti has been saved: {}", sppotiDTO);
		return sppotiDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SppotiDTO getSppotiByUuid(final String uuid)
	{
		final SppotiEntity sppoti = this.sppotiRepository.findByUuid(uuid);
		
		if (sppoti != null && sppoti.isDeleted()) {
			this.LOGGER.info("This sppoti {}, is deleted");
			throw new BusinessGlobalException("Trying to get a deleted sppoti");
		}
		
		final SppotiDTO dto = getSppotiResponse(sppoti);
		this.LOGGER.info("Sppoti data has been returned: {}", dto);
		return dto;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deleteSppoti(final String id)
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
	public SppotiDTO updateSppoti(final SppotiDTO sppotiRequest, final String sppotiId)
	{
		
		final SppotiEntity sppoti = this.sppotiRepository.findByUuid(sppotiId);
		if (sppoti == null)
			throw new EntityNotFoundException("SppotiEntity not found with sppotiId: " + sppotiId);
		
		boolean editNotification = false;
		
		if (StringUtils.hasText(sppotiRequest.getTags())) {
			sppoti.setTags(sppotiRequest.getTags());
			editNotification = true;
		}
		
		sppoti.setDescription(sppotiRequest.getDescription());
		
		//		if (StringUtils.hasText(sppotiRequest.getDescription())) {
		//			sppoti.setDescription(sppotiRequest.getDescription());
		//			editNotification = true;
		//		}
		
		if (sppotiRequest.getDateTimeStart() != null) {
			sppoti.setDateTimeStart(sppotiRequest.getDateTimeStart());
			editNotification = true;
		}
		
		if (StringUtils.hasText(sppotiRequest.getName())) {
			sppoti.setName(sppotiRequest.getName());
			editNotification = true;
		}
		
		if (StringUtils.hasText(sppotiRequest.getLocation())) {
			sppoti.setLocation(sppotiRequest.getLocation());
			editNotification = true;
		}
		
		if (sppotiRequest.getMaxTeamCount() != null && sppotiRequest.getMaxTeamCount() != 0) {
			sppoti.setMaxTeamCount(sppotiRequest.getMaxTeamCount());
			editNotification = true;
		}
		
		if (sppotiRequest.getType() != null && (SppotiStatus.PRIVATE.equals(sppotiRequest.getType()) ||
				SppotiStatus.PUBLIC.equals(sppotiRequest.getType()))) {
			sppoti.setType(sppotiRequest.getType());
			editNotification = true;
		}
		
		if (sppotiRequest.getVsTeam() != null && !StringUtils.isEmpty(sppotiRequest.getVsTeam())) {
			final List<TeamEntity> adverseTeam = this.teamRepository
					.findByUuidAndDeletedFalse(sppotiRequest.getVsTeam());
			
			//check if adverse team exist
			if (adverseTeam.isEmpty()) {
				throw new EntityNotFoundException("TeamEntity sppotiId not found: " + sppotiRequest.getVsTeam());
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
						if (hostMember.getUser().getId().equals(adverseMember.getUser().getId())) {
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
			
			//Notify adverse team admins.
			//Sppoti admin who sent the challenge
			adverse.getTeam().getTeamMembers().forEach(hostMember -> {
				if (hostMember.getAdmin()) {
					this.notificationService
							.saveAndSendNotificationToUsers(sppoti.getUserSppoti(), hostMember.getUser(), SPPOTI,
									NotificationTypeEnum.SPPOTI_ADMIN_CHALLENGED_YOU, adverse.getTeam(), sppoti);
				}
			});
			
		}
		
		final SppotiEntity updatedSppoti = this.sppotiRepository.save(sppoti);
		
		//Notify sppoti members (host and adverse) about the changes
		final Set<TeamMemberEntity> usersToNotify = updatedSppoti.getTeamHostEntity().getTeamMembers();
		
		final List<TeamEntity> te = updatedSppoti.getAdverseTeams().stream()
				.filter(m -> m.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)).map(SppotiAdverseEntity::getTeam)
				.collect(Collectors.toList());
		
		if (!te.isEmpty()) {
			usersToNotify.addAll(te.get(0).getTeamMembers());
		}
		
		if (editNotification) {
			usersToNotify.stream().filter(m -> !m.getAdmin()).forEach(m -> this.notificationService
					.saveAndSendNotificationToUsers(updatedSppoti.getUserSppoti(), m.getUser(), SPPOTI,
							NotificationTypeEnum.SPPOTI_HAS_BEEN_EDITED, null, updatedSppoti));
		}
		
		return getSppotiResponse(updatedSppoti);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void acceptSppoti(final String sppotiId, final String userId)
	{
		
		final Optional<SppoterEntity> optional = Optional.ofNullable(this.sppoterRepository
				.findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(userId, sppotiId,
						SppotiUtils.statusToFilter()));
		
		optional.ifPresent(sm -> {
			//update sppoter status.
			sm.setStatus(GlobalAppStatusEnum.CONFIRMED);
			this.sppoterRepository.save(sm);
			
			//update team member status.
			final TeamMemberEntity teamMembers = sm.getTeamMember();
			teamMembers.setStatus(GlobalAppStatusEnum.CONFIRMED);
			this.teamMembersRepository.save(teamMembers);
			
			//Send notification to team admin.
			final UserEntity teamAdmin = this.teamMembersRepository
					.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(teamMembers.getTeam().getUuid(),
							SppotiUtils.statusToFilter()).getUser();
			
			this.notificationService.saveAndSendNotificationToUsers(sm.getTeamMember().getUser(), teamAdmin, SPPOTI,
					X_ACCEPTED_THE_SPPOTI_INVITATION, sm.getSppoti());
			
			sendSppotiJoinResponseEmail(sm.getSppoti(), SppotiResponse.ACCEPTED);
			
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Sppoter not found"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void refuseSppoti(final String sppotiId, final String userId)
	{
		
		final SppoterEntity sppoter = this.sppoterRepository
				.findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(sppotiId, userId,
						SppotiUtils.statusToFilter());
		
		if (sppoter == null) {
			throw new EntityNotFoundException("Sppoter not found");
		}
		
		sppoter.setStatus(GlobalAppStatusEnum.REFUSED);
		final SppoterEntity updatedSppoter = this.sppoterRepository.save(sppoter);
		
		this.notificationService
				.saveAndSendNotificationToUsers(sppoter.getTeamMember().getUser(), sppoter.getSppoti().getUserSppoti(),
						SPPOTI, X_REFUSED_YOUR_SPPOTI_INVITATION, updatedSppoter.getSppoti());
		
		sendSppotiJoinResponseEmail(sppoter.getSppoti(), SppotiResponse.REJECTED);
	}
	
	private void sendSppotiJoinResponseEmail(final SppotiEntity sppoti, final SppotiResponse response) {
		final UserDTO emailTo = this.userTransformer.modelToDto(sppoti.getUserSppoti());
		final UserDTO emailFrom = this.userTransformer.modelToDto(getConnectedUser());
		final SppotiDTO sppotiDTO = this.sppotiTransformer.modelToDto(sppoti);
		this.sppotiMailerService.sendSppotiJoinResponseEmail(sppotiDTO, emailTo, emailFrom, response);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SppotiDTO> getAllUserSppoties(final String id, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "datetimeCreated");
		final List<SppotiEntity> sppoties = this.sppotiRepository.findByUserSppotiUuidAndDeletedFalse(id, pageable);
		
		return sppoties.stream()
				//.filter(s -> !s.getTeamAdverseStatusEnum().equals(GlobalAppStatusEnum.REFUSED))
				.map(this::getSppotiResponse).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public SppotiDTO sendChallengeToSppotiHostTeam(final String sppotiId, final String teamId)
	{
		//Check if team exist.
		final List<TeamEntity> teamEntities = this.teamRepository.findByUuidAndDeletedFalse(teamId);
		if (teamEntities.isEmpty()) {
			throw new EntityNotFoundException("Team not found (" + teamId + ")");
		}
		final TeamEntity challengeTeam = teamEntities.get(0);
		
		//check if user has rights to send challenge. (team adverse admin)
		if (!getConnectedUserId().equals(this.teamMembersRepository
				.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(teamId, SppotiUtils.statusToFilter())
				.getUser().getId())) {
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
		
		//Check if sppoti has not a CONFIRMED adverse team in the adverse team list.
		if (sppotiEntity.getAdverseTeams() != null && sppotiEntity.getAdverseTeams().stream()
				.anyMatch(t -> t.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))) {
			throw new BusinessGlobalException("This sppoti has already an adverse team");
		}
		
		//Check if selected team adverse was not already challenged by this sppoti.
		if (sppotiEntity.getAdverseTeams().stream().anyMatch(
				t -> t.getTeam().getUuid().equals(teamId) && t.getStatus().equals(GlobalAppStatusEnum.PENDING))) {
			throw new EntityExistsException("Challenge already sent to this team");
		}
		
		//check if adverse team members are not in conflict with team host members
		sppotiEntity.getTeamHostEntity().getTeamMembers().stream()
				.filter(m -> !m.getStatus().isNotPendingAndNotRefusedAndNotDeletedAndNotCancelled()).forEach(
				hostMember -> challengeTeam.getTeamMembers().stream()
						.filter(m -> m.getStatus().isNotPendingAndNotRefusedAndNotDeletedAndNotCancelled())
						.forEach(adverseMember -> {
							if (hostMember.getUser().getId().equals(adverseMember.getUser().getId())) {
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
		
		//Send notification to sppoti admin
		this.notificationService
				.saveAndSendNotificationToUsers(getConnectedUser(), sppotiEntity.getUserSppoti(), SPPOTI,
						TEAM_ADMIN_SENT_YOU_A_CHALLENGE, adverse.getTeam(), sppotiEntity);
		
		return getSppotiResponse(savedSppoti);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void chooseOneAdverseTeamFromAllChallengeRequests(final String sppotiId, final TeamDTO teamDTO)
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
								
								//send notification to team adverse admins.
								if (!teamAdverse.getFromSppotiAdmin()) {
									teamAdverse.getTeam().getTeamMembers().forEach(m -> {
										if (m.getAdmin()) {
											this.notificationService
													.saveAndSendNotificationToUsers(sp.getUserSppoti(), m.getUser(),
															CHALLENGE, SPPOTI_ADMIN_REFUSED_YOUR_CHALLENGE,
															teamAdverse.getTeam(), sp);
										}
									});
								} else {
									//send notification to sppoti admin.
									teamAdverse.getTeam().getTeamMembers().forEach(m -> {
										if (m.getAdmin()) {
											this.notificationService
													.saveAndSendNotificationToUsers(sp.getUserSppoti(), m.getUser(),
															CHALLENGE, SPPOTI_ADMIN_CANCELED_HIS_CHALLENGE,
															teamAdverse.getTeam(), sp);
										}
									});
								}
							} else {
								teamAdverse.setStatus(GlobalAppStatusEnum.valueOf(teamDTO.getTeamAdverseStatus()));
								this.sppotiRepository.save(sp);
								
								//Convert team members to sppoters if status equals to confirmed
								if (GlobalAppStatusEnum.valueOf(teamDTO.getTeamAdverseStatus())
										.equals(GlobalAppStatusEnum.CONFIRMED)) {
									final Set<SppoterEntity> sppotiMembers = convertAdverseTeamMembersToSppoters(
											teamAdverse.getTeam(), sp);
									sp.setSppotiMembers(sppotiMembers);
									this.sppotiRepository.save(sp);
								}
								
								//Set other challenge status to REFUSED
								sp.getAdverseTeams().forEach(a -> {
									if (!a.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
										a.setStatus(GlobalAppStatusEnum.REFUSED);
										this.sppotiAdverseRepository.save(a);
									}
								});
								
								//Notify team members
								final List<TeamMemberEntity> usersToNotify = new ArrayList<>();
								usersToNotify.addAll(teamAdverse.getTeam().getTeamMembers());
								usersToNotify.addAll(sp.getTeamHostEntity().getTeamMembers());
								
								//Notify to all confirmed adverse team members.
								usersToNotify.forEach(m -> {
									if (!sp.getUserSppoti().getUuid().equals(m.getUser().getUuid())) {
										this.notificationService
												.saveAndSendNotificationToUsers(sp.getUserSppoti(), m.getUser(),
														CHALLENGE, SPPOTI_ADMIN_ACCEPTED_THE_CHALLENGE,
														teamAdverse.getTeam(), sp);
									}
								});
							}
						});
			});
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("ConstantConditions")
	@Override
	@Transactional
	public List<UserDTO> rateSppoters(final List<RatingDTO> ratingDTOS, final String sppotiId)
	{
		
		final Optional<SppotiEntity> sppotiEntityOptional = Optional
				.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		if (sppotiEntityOptional.isPresent()) {
			final SppotiEntity sppotiEntity = sppotiEntityOptional.get();
			
			return ratingDTOS.stream().map(rating -> {
				
				Optional<SppoterEntity> optional = Optional.ofNullable(this.sppoterRepository
						.findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(
								rating.getSppoterRatedId(), sppotiEntity.getUuid(), SppotiUtils.statusToFilter()));
				optional.orElseThrow(
						() -> new EntityNotFoundException("Sppoter (" + rating.getSppoterRatedId() + ") not found"));
				
				SppoterEntity ratedSppoter = optional.get();
				
				//Deny rating of a sppoti who hasn't accepted sppoti yet !
				if (ratedSppoter.getStatus().equals(GlobalAppStatusEnum.PENDING)) {
					throw new BusinessGlobalException("Sppoter (" + rating.getId() + ") hasn't accepted sppoti yet");
				}
				
				UserEntity connectUser = getConnectedUser();
				
				//FIXME: create transformer for rating
				RatingEntity ratingEntity = new RatingEntity();
				ratingEntity.setSppotiEntity(sppotiEntity);
				ratingEntity.setRatedSppoter(ratedSppoter.getTeamMember().getUser());
				ratingEntity.setRatingDate(new Date());
				ratingEntity.setRaterSppoter(connectUser);
				ratingEntity.setStars(rating.getStars());
				
				this.ratingRepository.save(ratingEntity);
				
				//Flag rated rating to true.
				//This means that sppoti admin has rate this rating
				SppoterEntity sppoterRaterEntity = this.sppoterRepository
						.findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(connectUser.getUuid(),
								sppotiId, SppotiUtils.statusToFilter());
				sppoterRaterEntity.setHasRateOtherSppoter(Boolean.TRUE);
				this.sppoterRepository.save(sppoterRaterEntity);
				
				if (!connectUser.getId().equals(ratedSppoter.getTeamMember().getUser().getId())) {
					this.notificationService
							.saveAndSendNotificationToUsers(connectUser, ratedSppoter.getTeamMember().getUser(), SPPOTI,
									YOU_HAVE_BEEN_RATED, ratedSppoter.getSppoti(), ratingEntity);
				}
				
				//Get RATED SPPOTER.
				SppoterEntity sppoterRatedEntity = this.sppoterRepository
						.findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(
								rating.getSppoterRatedId(), sppotiId, SppotiUtils.statusToFilter());
				return this.teamMemberTransformer.modelToDto(sppoterRatedEntity.getTeamMember(), sppotiEntity);
				
			}).collect(Collectors.toList());
		}
		
		throw new EntityNotFoundException("Sppoti not found");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SppotiDTO> getAllJoinedSppoties(final String userId, final int page)
	{
		
		CheckConnectedUserAccessPrivileges(userId);
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "invitationDate");
		
		final List<SppoterEntity> sppotiMembers = this.sppoterRepository
				.findByTeamMemberUserUuidAndStatusNotInAndSppotiDeletedFalse(userId, SppotiUtils.statusToFilter(),
						pageable);
		
		return sppotiMembers.stream().filter(s -> !Objects.equals(s.getSppoti().getUserSppoti().getUuid(), userId))
				.map(s -> getSppotiResponse(s.getSppoti())).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<SppotiDTO> getAllConfirmedSppoties(final String userId, final int page)
	{
		
		CheckConnectedUserAccessPrivileges(userId);
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "invitationDate");
		
		return this.sppoterRepository
				.findByTeamMemberUserUuidAndStatusNotInAndSppotiDeletedFalse(userId, SppotiUtils.statusToFilter(),
						pageable).stream().filter(m -> m.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))
				.map(s -> getSppotiResponse(s.getSppoti())).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<SppotiDTO> getAllRefusedSppoties(final String userId, final int page)
	{
		
		CheckConnectedUserAccessPrivileges(userId);
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "invitationDate");
		
		return this.sppoterRepository
				.findByTeamMemberUserUuidAndStatusNotInAndSppotiDeletedFalse(userId, SppotiUtils.statusToFilter(),
						pageable).stream().filter(m -> m.getStatus().equals(GlobalAppStatusEnum.REFUSED))
				.map(s -> getSppotiResponse(s.getSppoti())).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SppotiDTO> getAllUpcomingSppoties(final String userId, final int page)
	{
		
		//		CheckConnectedUserAccessPrivileges(userId);
		
		final Pageable pageable = new PageRequest(page, this.sppotiSize);
		
		return this.sppoterRepository.findAllUpcomingSppoties(userId, GlobalAppStatusEnum.CONFIRMED, pageable).stream()
				.map(SppoterEntity::getSppoti).map(s -> {
					s.setConnectedUserId(getConnectedUser().getId());
					return this.sppotiTransformer.modelToDto(s);
					
				}).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public List<SppotiDTO> getAllPendingChallengeRequestSppoties(final String userId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.sppotiSize);
		final List<SppotiDTO> result = new ArrayList<>();
		
		//Get all user teams.
		final List<TeamMemberEntity> teamMemberEntities = this.teamMembersRepository
				.findByUserUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(userId, SppotiUtils.statusToFilter(),
						pageable);
		
		//Check if one of this teams has received a challenge request.
		teamMemberEntities.stream().map(t -> this.sppotiAdverseRepository.findByTeamUuid(t.getTeam().getUuid()))
				.forEach(k -> k.forEach(a -> {
					if (GlobalAppStatusEnum.PENDING.equals(a.getStatus())) {
						final SppotiEntity sp = a.getSppoti();
						final UserEntity connectedUser = getConnectedUser();
						sp.setConnectedUserId(connectedUser.getId());
						
						final SppotiDTO sppotiDTO = this.sppotiTransformer.modelToDto(sp);
						
						sppotiDTO.setTeamAdverse(
								sppotiDTO.getTeamAdverse().stream().filter(t -> t.getId().equals(a.getTeam().getUuid()))
										.collect(Collectors.toList()));
						result.add(sppotiDTO);
					}
				}));
		
		//Add other teams challenge requests - (requests sent from other admin teams)
		final Pageable pageable1 = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "creationDate");
		
		final Optional<SppotiAdverseEntity> adverseEntityOptional = this.sppotiAdverseRepository
				.findBySppotiUserSppotiUuidAndStatusAndFromSppotiAdminFalse(userId, GlobalAppStatusEnum.PENDING,
						pageable1).stream().findFirst();
		adverseEntityOptional.ifPresent(a -> {
			a.getSppoti().setConnectedUserId(getConnectedUserId());
			result.add(this.sppotiTransformer.modelToDto(a.getSppoti()));
		});
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> findAllSppoterAllowedToJoinSppoti(final String prefix, final String sppotiId, final int page)
	{
		final Optional<SppotiEntity> optional = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		
		//sppoti exists.
		if (optional.isPresent()) {
			final SppotiEntity sp = optional.get();
			final Pageable pageable = new PageRequest(page, this.sppotiSize, Sort.Direction.DESC, "username");
			
			final List<Long> existingSppoter = new ArrayList<>();
			
			sp.getAdverseTeams().forEach(ad -> existingSppoter.addAll(ad.getTeam().getTeamMembers().stream().filter(m ->
					m.getSppotiMembers().stream()
							.noneMatch(s -> s.getStatus().isNotPendingAndNotRefusedAndNotDeletedAndNotCancelled()) &&
							!m.getSppotiMembers().isEmpty()).map(a -> a.getUser().getId())
					.collect(Collectors.toList())));
			
			existingSppoter.addAll(sp.getTeamHostEntity().getTeamMembers().stream().filter(m ->
					m.getSppotiMembers().stream()
							.noneMatch(s -> s.getStatus().isNotPendingAndNotRefusedAndNotDeletedAndNotCancelled()) &&
							!m.getSppotiMembers().isEmpty()).map(m -> m.getUser().getId())
					.collect(Collectors.toList()));
			
			return this.userRepository.findAllAllowedSppoter(prefix, existingSppoter, pageable).stream()
					.map(this.userTransformer::modelToDto).collect(Collectors.toList());
		}
		
		throw new EntityNotFoundException("Sppoti not found");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public UserDTO addSppoter(final String sppotiId, final String userId, final String teamId)
	{
		
		final Optional<SppotiEntity> sppotiEntity = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		
		if (sppotiEntity.isPresent()) {
			
			//Get sppoti content from the optional.
			final SppotiEntity sppoti = sppotiEntity.get();
			
			//Check if user id exists so that we can add it as a sppoter.
			final UserEntity userSppoter = getUserByUuId(userId);
			if (userSppoter == null) {
				throw new EntityNotFoundException("UserDTO with id (" + userId + ") Not found");
			}
			
			//Check if team exists.
			final List<TeamEntity> teamEntity = this.teamRepository.findByUuidAndDeletedFalse(teamId);
			if (teamEntity.isEmpty()) {
				throw new EntityNotFoundException("Team id not found");
			}
			final TeamEntity userTeam = teamEntity.get(0);
			
			/*
			__________________________________________________________________________________________
			Check if the team is a teamHost or a confirmed teamAdverse
			 */
			boolean teamIsAllowed = false;
			if (userTeam.getId().equals(sppoti.getTeamHostEntity().getId())) {
				teamIsAllowed = true;
			} else {
				//if sppoti has a confirmed adverse team
				final Optional<SppotiAdverseEntity> sppotiAdverseEntityOptional = sppoti.getAdverseTeams().stream()
						.filter(a -> a.getStatus().equals(GlobalAppStatusEnum.CONFIRMED) &&
								a.getTeam().getTeamMembers().stream().anyMatch(m -> m.getAdmin().equals(true)))
						.findFirst();
				if (sppotiAdverseEntityOptional.isPresent()) {
					if (userTeam.getId().equals(sppotiAdverseEntityOptional.get().getTeam().getId())) {
						teamIsAllowed = true;
					}
				}
			}
			
			if (!teamIsAllowed) {
				throw new BusinessGlobalException("This team is not allowed in this sppoti");
			}
			/*
			__________________________________________________________________________________________
			 */
			
			//check if the connected user is the admin of the sppoti or team adverse.
			if (!sppoti.getUserSppoti().getId().equals(getConnectedUser().getId()) && sppoti.getAdverseTeams().stream()
					.noneMatch(a -> a.getStatus().equals(GlobalAppStatusEnum.CONFIRMED) &&
							a.getTeam().getTeamMembers().stream().anyMatch(m -> m.getAdmin().equals(true)))) {
				throw new NotAdminException("BIM BIM - You don't have access");
			}
			
			//FIXME: Check if the sppoter has been already deleted from the sppoti, if so, we don't have to add it again as a team member.
			
			final SppoterEntity sppoter = new SppoterEntity();
			final TeamMemberEntity teamMembers;
			final Set<SppoterEntity> sppotiMembers = new HashSet<>();
			
			final List<SppoterEntity> sppoterEntities = this.sppoterRepository
					.findByTeamMemberUserUuidAndSppotiUuidAndSppotiDeletedFalse(userId, sppotiId);
			if (this.sppoterRepository.findByTeamMemberUserUuidAndSppotiUuidAndSppotiDeletedFalse(userId, sppotiId)
					.isEmpty()) {
				
				//team member.
				teamMembers = new TeamMemberEntity();
				teamMembers.setTeam(userTeam);
				teamMembers.setUser(userSppoter);
				
			} else {
				teamMembers = sppoterEntities.get(0).getTeamMember();
			}
			
			//Link sppoter with team member.
			sppotiMembers.add(sppoter);
			teamMembers.setSppotiMembers(sppotiMembers);
			
			//sppoter.
			sppoter.setTeamMember(teamMembers);
			sppoter.setSppoti(sppoti);
			
			//save new member and sppoter.
			final TeamMemberEntity savedMember = this.teamMembersRepository.save(teamMembers);
			
			//Email sppoter
			this.sppotiMailerService.sendJoinSppotiEmailToSppoters(this.sppotiTransformer.modelToDto(sppoti),
					this.userTransformer.modelToDto(userSppoter),
					this.userTransformer.modelToDto(sppoti.getUserSppoti()));
			
			//Notify new sppoter
			this.notificationService.saveAndSendNotificationToUsers(sppoti.getUserSppoti(), userSppoter, SPPOTI,
					X_INVITED_YOU_TO_JOIN_HIS_SPPOTI, sppoti);
			
			//return new member.
			return this.teamMemberTransformer.modelToDto(savedMember, sppoti);
		}
		
		throw new EntityNotFoundException("Sppoti (" + sppotiId + ") not found");
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deleteSppoter(final String sppotiId, final String userId) {
		
		//User must be the admin of the sppoti.
		isSppotiAdmin(sppotiId);
		
		//Find sppoter and delete it.
		final Optional<SppoterEntity> sppoter = Optional.ofNullable(this.sppoterRepository
				.findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(userId, sppotiId,
						SppotiUtils.statusToFilter()));
		
		sppoter.ifPresent(s -> {
			if (!s.getTeamMember().getAdmin()) {
				s.setStatus(GlobalAppStatusEnum.DELETED);
				this.sppoterRepository.save(s);
			} else {
				throw new BusinessGlobalException("You can't delete an admin");
			}
		});
		
		//sppoter not found.
		sppoter.orElseThrow(() -> new EntityNotFoundException("Sppoter not found"));
		
		//Notify him.
		//TODO: notification (Remove sppoter)
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public SppotiDTO updateSppotiType(final String sppotiId, final SppotiStatus type) {
		
		isSppotiAdmin(sppotiId);
		
		final Optional<SppotiEntity> entity = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		
		if (entity.isPresent()) {
			entity.get().setType(type);
			return this.sppotiTransformer.modelToDto(this.sppotiRepository.save(entity.get()));
		}
		
		throw new EntityNotFoundException(ErrorMessageEnum.SPPOTI_NOT_FOUND.getMessage());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public SppotiDTO requestJoinSppoti(final String sppotiId) {
		final Optional<SppotiEntity> entity = Optional.ofNullable(this.sppotiRepository.findByUuid(sppotiId));
		
		if (entity.isPresent()) {
			final UserEntity user = getConnectedUser();
			
			if (this.sppoterRepository
					.findByTeamMemberUserUuidAndSppotiUuidAndSppotiDeletedFalse(user.getUuid(), sppotiId) == null) {
				throw new EntityExistsException("Sppoter already exists");
			}
			
			final SppotiRequest r = new SppotiRequest();
			r.setSppoti(entity.get());
			r.setUser(user);
			entity.get().getSppotiRequests().add(r);
			
			final SppotiDTO dto = this.sppotiTransformer.modelToDto(this.sppotiRepository.save(entity.get()));
			
			this.sppotiMailerService.sendJoinSppotiEmailToSppotiAdmin(dto,
					this.userTransformer.modelToDto(entity.get().getUserSppoti()),
					this.userTransformer.modelToDto(user));
			
			return dto;
		}
		
		throw new EntityNotFoundException(ErrorMessageEnum.SPPOTI_NOT_FOUND.getMessage());
	}
	
	/**
	 * {@inheritDoc}
	 */
	//TODO: to implement
	@Transactional
	@Override
	public void confirmTeamRequestSentFromUser(final String sppotiId, final UserDTO dto) {
		
		final Optional<SppotiRequest> request = this.sppotiRequestRepository
				.findBySppotiUuidAndUserUuidAndStatus(sppotiId, dto.getId(), GlobalAppStatusEnum.PENDING);
		
		request.ifPresent(r -> {
			r.setStatus(GlobalAppStatusEnum.CONFIRMED);
			
			//Check if user is already a member of the team
			
			//If member only add it as sppoter
			
			//if not add it as team member too
			
			//user is confirmed in both, team and sppoti
			
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	//TODO: to implement.
	@Transactional
	@Override
	public void refuseTeamRequestSentFromUser(final String sppotiId, final UserDTO dto) {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void leaveSppoti(final String sppotiId) {
		final Optional<SppoterEntity> optional = Optional.ofNullable(this.sppoterRepository
				.findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(getConnectedUserUuid(),
						sppotiId, SppotiUtils.statusToFilter()));
		
		optional.ifPresent(s -> {
			s.setStatus(GlobalAppStatusEnum.LEFT);
			this.sppoterRepository.save(s);
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("User not member of this sppoti"));
	}
	
	/**
	 * Map sppoti entity to DTO.
	 *
	 * @param sppoti
	 * 		sppoti to return.
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
		
		
		final TeamDTO teamHostResponse = fillTeamResponse(sppoti.getTeamHostEntity(), sppoti);
		
		sppotiDTO.setTeamHost(teamHostResponse);
		sppotiDTO.setId(sppoti.getUuid());
		sppotiDTO.setRelatedSport(this.sportTransformer.modelToDto(sppoti.getSport()));
		
		final List<SppoterEntity> sppotiMembers = this.sppoterRepository
				.findByTeamMemberUserUuidAndSppotiSportIdAndStatusNotInAndSppotiDeletedFalse(
						sppoti.getUserSppoti().getUuid(), sppoti.getSport().getId(), SppotiUtils.statusToFilter());
		
		sppotiDTO.setSppotiCounter(sppotiMembers.size());
		sppotiDTO.setMySppoti(Objects.equals(getConnectedUser().getUuid(), sppoti.getUserSppoti().getUuid()));
		
		//if user is member of a team, get admin of the tem and other informations.
		final TeamMemberEntity teamAdmin = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(
						sppoti.getUserSppoti().getUuid(), sppoti.getTeamHostEntity().getUuid(),
						SppotiUtils.statusToFilter());
		if (teamAdmin != null) {
			sppotiDTO.setAdminTeamId(teamAdmin.getUuid());
			sppotiDTO.setAdminUserId(sppoti.getUserSppoti().getUuid());
			sppotiDTO.setConnectedUserId(getConnectedUser().getUuid());
		}
		
		return sppotiDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSppotiAdmin(final String sppotiId) {
		final SppotiEntity sppotiEntity = this.sppotiRepository.findByUuid(sppotiId);
		if (sppotiEntity == null) {
			throw new EntityNotFoundException("Sppoti (" + sppotiId + ") not found !");
		}
		
		return sppotiEntity.getUserSppoti().getId().equals(getConnectedUser().getId());
	}
}
