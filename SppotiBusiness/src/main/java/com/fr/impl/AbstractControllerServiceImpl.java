package com.fr.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.commons.exception.NotAdminException;
import com.fr.commons.exception.TeamMemberNotFoundException;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.mail.SppotiMailer;
import com.fr.mail.TeamMailer;
import com.fr.repositories.*;
import com.fr.service.AbstractControllerService;
import com.fr.transformers.NotificationTransformer;
import com.fr.transformers.TeamTransformer;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.TeamMemberTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Component("abstractService")
abstract class AbstractControllerServiceImpl implements AbstractControllerService
{
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	SportRepository sportRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PostRepository postRepository;
	@Autowired
	EditHistoryRepository editHistoryRepository;
	@Autowired
	LikeRepository likeRepository;
	@Autowired
	ResourceRepository resourceRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	FriendShipRepository friendShipRepository;
	@Autowired
	SppotiRepository sppotiRepository;
	@Autowired
	TeamRepository teamRepository;
	@Autowired
	TeamMembersRepository teamMembersRepository;
	@Autowired
	SppotiMembersRepository sppotiMembersRepository;
	@Autowired
	NotificationRepository notificationRepository;
	@Autowired
	RatingRepository ratingRepository;
	@Autowired
	ScoreRepository scoreRepository;
	@Autowired
	SppotiAdverseRepository sppotiAdverseRepository;
	
	/** Team mailer. */
	@Autowired
	TeamMailer teamMailer;
	
	/** Sppoti mailer. */
	@Autowired
	SppotiMailer sppotiMailer;
	
	@Autowired
	private Environment environment;
	
	/** Team member controller. */
	@Autowired
	private TeamMemberTransformer teamMemberTransformer;
	
	/** User transformer. */
	@Autowired
	private UserTransformer userTransformer;
	
	/** Team transformer. */
	@Autowired
	private TeamTransformer teamTransformer;
	
	/** Socket messaging temples. */
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	/** Notification Transformer. */
	@Autowired
	private NotificationTransformer notificationTransformer;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getUserById(final Long id)
	{
		return this.userRepository.getByIdAndDeletedFalseAndConfirmedTrue(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getUserByUuId(final String id)
	{
		
		final Optional<UserEntity> usersList = this.userRepository.getByUuidAndDeletedFalseAndConfirmedTrue(id);
		
		return usersList.orElse(null);
		
	}
	
	protected Properties globalAddressConfigProperties()
	{
		final Properties properties = new Properties();
		properties.put("rootAddress", this.environment.getRequiredProperty("rootAddress"));
		
		return properties;
	}
	
	/**
	 * @param dsHistoryList
	 * 		lost of edited content.
	 *
	 * @return list of ContentEditedResponseDTO.
	 */
	protected List<ContentEditedResponseDTO> fillEditContentResponse(final List<EditHistoryEntity> dsHistoryList)
	{
		final List<ContentEditedResponseDTO> editHistoryResponse = new ArrayList<ContentEditedResponseDTO>();
		editHistoryResponse.clear();
		for (final EditHistoryEntity editContent : dsHistoryList) {
			
			final ContentEditedResponseDTO cer = new ContentEditedResponseDTO();
			cer.setDateTime(editContent.getDatetimeEdited());
			cer.setId(editContent.getId());
			cer.setText(editContent.getText());
			
			editHistoryResponse.add(cer);
		}
		
		return editHistoryResponse;
	}
	
	/**
	 * @return connected user entity
	 */
	protected UserEntity getConnectedUser()
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth.getPrincipal().equals("anonymousUser")) {
			return null;
		}
		
		final UserDetails accountUserDetails = (UserDetails) auth.getPrincipal();
		return this.getUserByLogin(accountUserDetails.getUsername(), true);
	}
	
	/**
	 * Get user entity from use login.
	 *
	 * @param username
	 * 		user login.
	 *
	 * @return Connected user data.
	 */
	public UserEntity getUserByLogin(final String username, final boolean filterDeleted)
	{
		final String numberRegex = "[0-9]+";
		
		if (filterDeleted) {
			if (username.contains("@")) {
				return this.userRepository.getByEmailAndDeletedFalseAndConfirmedTrue(username);
			} else if (username.matches(numberRegex)) {
				return this.userRepository.getByTelephoneAndDeletedFalseAndConfirmedTrue(username);
			} else {
				return this.userRepository.getByUsernameAndDeletedFalseAndConfirmedTrue(username);
			}
		} else {
			if (username.contains("@")) {
				return this.userRepository.findByEmail(username);
			} else if (username.matches(numberRegex)) {
				return this.userRepository.findByTelephone(username);
			} else {
				return this.userRepository.findByUsername(username);
			}
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public UserDTO getUserByUsernameForLogin(final String username) {
		final UserEntity entity = getUserByLogin(username, false);
		//if account is deactivated and suppress date is less than 90 days reactivate account.
		UserEntity temp = null;
		if (entity != null) {
			if (entity.getDeactivationDate() != null &&
					!SppotiUtils.isAccountReadyToBeCompletlyDeleted(entity.getDeactivationDate(), 90)) {
				entity.setDeleted(false);
				temp = this.userRepository.saveAndFlush(entity);
				return this.userTransformer.modelToDto(temp);
			}
			return this.userTransformer.modelToDto(entity);
			
		}
		
		return null;
	}
	
	/**
	 * @param dbCommentEntityList
	 * 		like entity.
	 * @param userId
	 * 		user id.
	 *
	 * @return list of Comment DTO
	 */
	//TODO: Move it to the transformer
	protected List<CommentDTO> fillCommentModelList(final List<CommentEntity> dbCommentEntityList, final Long userId)
	{
		final List<CommentDTO> myList = new ArrayList<>();
		
		for (final CommentEntity commentEntity : dbCommentEntityList) {
			final String commentId = commentEntity.getUuid();
			final CommentDTO cm = new CommentDTO();
			
			//            if (!userDaoService.getLastAvatar(userId).isEmpty())
			//                cm.setAuthorAvatar(userDaoService.getLastAvatar(userId).get(0).getUrl());
			
			final UserDTO userCoverAndAvatar = this.userTransformer.getUserCoverAndAvatar(commentEntity.getUser());
			cm.setAuthorAvatar(userCoverAndAvatar.getAvatar());
			cm.setAuthorFirstName(commentEntity.getUser().getFirstName());
			cm.setAuthorLastName(commentEntity.getUser().getLastName());
			cm.setCreationDate(commentEntity.getDatetimeCreated());
			cm.setId(commentId);
			cm.setImageLink(commentEntity.getImageLink());
			cm.setMyComment(commentEntity.getUser().getId().equals(userId));
			
			final boolean isCommentLikedByMe = commentEntity.getLikes().stream()
					.anyMatch(l -> l.getUser().getId().equals(userId));
			cm.setLikedByUser(isCommentLikedByMe);
			cm.setLikeCount(commentEntity.getLikes().size());
			
			final List<EditHistoryEntity> editHistory = this.editHistoryRepository
					.getByCommentUuidOrderByDatetimeEditedDesc(commentId);
			
			if (!editHistory.isEmpty()) {
				cm.setEdited(true);
				
				final EditHistoryEntity ec = editHistory.get(0);
				
				cm.setCreationDate(ec.getDatetimeEdited());
				cm.setText(ec.getText());
			} else {
				cm.setText(commentEntity.getContent());
				cm.setCreationDate(commentEntity.getDatetimeCreated());
			}
			
			cm.setLikeCount(commentEntity.getLikes().size());
			
			myList.add(cm);
		}
		
		return myList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Set<TeamMemberEntity> getTeamMembersEntityFromDto(final List<UserDTO> users, final TeamEntity team,
															 final SppotiEntity sppoti)
	{
		
		final Set<TeamMemberEntity> teamUsers = new HashSet<>();
		
		final Long connectedUserId = getConnectedUser().getId();
		
		users.forEach(userDTO -> {
			
			final Optional<UserEntity> userEntity = Optional.ofNullable(getUserByUuId(userDTO.getId()));
			
			final TeamMemberEntity teamMember = new TeamMemberEntity();
			final SppoterEntity sppoter = new SppoterEntity();
			
			userEntity.ifPresent(user -> {
				if (user.getId().equals(connectedUserId)) {
					teamMember.setAdmin(true);
					teamMember.setTeamCaptain(true);
					/* Admin is member of the team, status should be confirmed. */
					teamMember.setStatus(GlobalAppStatusEnum.CONFIRMED);
				}
				teamMember.setTeam(team);
				teamMember.setUser(user);
				
				if (sppoti != null) {
					final TeamMemberEntity sppoterMember = this.teamMembersRepository
							.findByUserUuidAndTeamUuidAndStatusNot(userDTO.getId(), team.getUuid(),
									GlobalAppStatusEnum.DELETED);

                    /* if request comming from add sppoti, insert new coordinate in (team_sppoti) to define new sppoter. */
					if (userDTO.getxPosition() != null && !userDTO.getxPosition().equals(0)) {
						sppoter.setxPosition(userDTO.getxPosition());
					}
					
					if (userDTO.getyPosition() != null && !userDTO.getyPosition().equals(0)) {
						sppoter.setyPosition(userDTO.getyPosition());
					}

                    /* Admin is member of sppoti, status should be confirmed. */
					if (teamMember.getAdmin() != null && teamMember.getAdmin()) {
						sppoter.setStatus(GlobalAppStatusEnum.CONFIRMED);
					}

                    /* if the sppoter already exist - default coordinate doesn't change. */
					if (sppoterMember == null) {
						if (userDTO.getxPosition() != null && !userDTO.getxPosition().equals(0)) {
							teamMember.setxPosition(userDTO.getxPosition());
						}
						
						if (userDTO.getyPosition() != null && !userDTO.getyPosition().equals(0)) {
							teamMember.setyPosition(userDTO.getyPosition());
						}
					}

                    /* Convert team members to sppoters. */
					final Set<SppoterEntity> sppotiMembers = new HashSet<>();
					sppoter.setTeamMember(teamMember);
					sppoter.setSppoti(sppoti);
					sppotiMembers.add(sppoter);
					
					teamMember.setSppotiMembers(sppotiMembers);
					sppoti.setSppotiMembers(sppotiMembers);
					
					
				} else {
					/* if request coming from add team - add members only in (users_team). */
					if (userDTO.getxPosition() != null && !userDTO.getxPosition().equals(0)) {
						teamMember.setxPosition(userDTO.getxPosition());
					}
					
					if (userDTO.getyPosition() != null && !userDTO.getyPosition().equals(0)) {
						teamMember.setyPosition(userDTO.getyPosition());
					}
					
				}
				
				teamUsers.add(teamMember);
			});
			
			userEntity.orElseThrow(
					() -> new TeamMemberNotFoundException("team member (" + userDTO.getId() + ") not found"));
			
		});
		
		return teamUsers;
		
	}
	
	/**
	 * @param team
	 * 		team to map.
	 *
	 * @return a teamResponse object from TeamEntity entity.
	 */
	protected TeamDTO fillTeamResponse(final TeamEntity team, final SppotiEntity sppoti)
	{
		
		final List<UserDTO> teamUsers = new ArrayList<>();
		
		for (final TeamMemberEntity memberEntity : team.getTeamMembers()) {
			/**
			 * If team member is not deleted && Sppoter Not deleted too, add sppoter to the list of members.
			 */
			if (sppoti != null) {
				if (!memberEntity.getStatus().equals(GlobalAppStatusEnum.DELETED) &&
						memberEntity.getSppotiMembers().stream().anyMatch(
								s -> s.getSppoti().getUuid().equals(sppoti.getUuid()) &&
										!s.getStatus().equals(GlobalAppStatusEnum.DELETED))) {
					teamUsers.add(this.teamMemberTransformer.modelToDto(memberEntity, sppoti));
				}
			} else if (!memberEntity.getStatus().equals(GlobalAppStatusEnum.DELETED)) {
				teamUsers.add(this.teamMemberTransformer.modelToDto(memberEntity, null));
			}
		}
		final TeamDTO teamDTO = this.teamTransformer.modelToDto(team);
		teamDTO.setMembers(teamUsers);
		
		return teamDTO;
		
	}
	
	/**
	 * Prepare and send notification to users..
	 */
	@Transactional
	protected void addNotification(final NotificationTypeEnum notificationType, final UserEntity userFrom,
								   final UserEntity userTo, final TeamEntity teamEntity, final SppotiEntity sppoti,
								   final PostEntity post, final CommentEntity comment, final ScoreEntity score)
	{
		final NotificationEntity notification = getNotificationEntity(notificationType, userFrom, userTo, teamEntity,
				sppoti, post, comment, score);
		
		final NotificationDTO notificationDTO = this.notificationTransformer.modelToDto(notification);
		this.messagingTemplate.convertAndSendToUser(userTo.getEmail(), "/queue/notify", notificationDTO);
		this.notificationRepository.save(notification);
	}
	
	/**
	 * Init notif entity.
	 */
	NotificationEntity getNotificationEntity(final NotificationTypeEnum notificationType, final UserEntity userFrom,
											 final UserEntity userTo, final TeamEntity team, final SppotiEntity sppoti,
											 final PostEntity post, final CommentEntity comment,
											 final ScoreEntity scoreEntity)
	{
		final NotificationEntity notification = new NotificationEntity();
		notification.setNotificationType(notificationType);
		notification.setFrom(userFrom);
		notification.setTo(userTo);
		notification.setTeam(team);
		
		if (sppoti != null) {
			sppoti.setConnectedUserId(userTo.getId());
			notification.setSppoti(sppoti);
		}
		
		if (post != null) {
			post.setConnectedUserId(userTo.getId());
			notification.setPost(post);
		}
		
		if (comment != null) {
			comment.setConnectedUserId(userTo.getId());
			notification.setComment(comment);
		}
		
		notification.setScore(scoreEntity);
		
		return notification;
	}
	
	/**
	 * Find tags in content and add notifications.
	 *
	 * @param commentEntity
	 * 		like entity.
	 * @param postEntity
	 * 		post entity.
	 */
	@Transactional
	public void addTagNotification(final PostEntity postEntity, final CommentEntity commentEntity)
	{
		
		String content = null;
		if (postEntity != null) {
			content = postEntity.getContent();
		} else if (commentEntity != null) {
			content = commentEntity.getContent();
		}
		
		if (content != null) {
			/**
			 * All words starting with @, followed by Letter or accented Letter.
			 * and finishing with Letter, Number or Accented letter.
			 */
			final String patternString1 = "(\\$+)([a-z|A-Z|\\p{javaLetter}][a-z\\d|A-Z\\d|\\p{javaLetter}]*)";
			
			final Pattern pattern = Pattern.compile(patternString1);
			final Matcher matcher = pattern.matcher(content);
			
			/**
			 *  clean tags from @.
			 */
			final List<String> tags = new ArrayList<>();
			while (matcher.find()) {
				String s = matcher.group().trim();
				s = s.replaceAll("[$]", "");
				tags.add(s);
			}
			
			/**
			 * Process each tag.
			 */
			for (final String username : tags) {
				final UserEntity userToNotify;
				
				userToNotify = this.userRepository.getByUsernameAndDeletedFalseAndConfirmedTrue(username);
				
				if (userToNotify != null) {
					if (commentEntity != null) {
						addNotification(NotificationTypeEnum.X_TAGGED_YOU_IN_A_COMMENT, commentEntity.getUser(),
								userToNotify, null, null, commentEntity.getPost(), commentEntity, null);
					} else {
						addNotification(NotificationTypeEnum.X_TAGGED_YOU_IN_A_POST, postEntity.getUser(), userToNotify,
								null, null, postEntity, null, null);
					}
					
				}
			}
		}
		
	}
	
	/**
	 * Check if the user id in parameter is same as connected user id.
	 *
	 * @param userId
	 * 		user id resource.
	 */
	void CheckConnectedUserAccessPrivileges(final String userId)
	{
		if (!Objects.equals(getConnectedUser().getUuid(), userId)) {
			throw new NotAdminException("Unauthorized access");
		}
	}
	
	
	/**
	 * Send Email to the invited member to join the team.
	 *
	 * @param team
	 * 		team to add member.
	 * @param to
	 * 		added member.
	 * @param from
	 * 		team admin.
	 */
	void sendJoinTeamEmail(final TeamEntity team, final UserEntity to, final TeamMemberEntity from)
	{
		
		final UserDTO member = this.userTransformer.modelToDto(to);
		final UserDTO admin = this.teamMemberTransformer.modelToDto(from, null);
		final TeamDTO teamDto = this.teamTransformer.modelToDto(team);
		
		final Thread thread = new Thread(() -> this.teamMailer.sendJoinTeamEmail(teamDto, member, admin));
		thread.start();
	}
	
	/**
	 * @param challengeTeam
	 * 		adverse team.
	 * @param sppoti
	 * 		sppoti id.
	 *
	 * @return all adverse team as sppoters.
	 */
	Set<SppoterEntity> convertAdverseTeamMembersToSppoters(final TeamEntity challengeTeam, final SppotiEntity sppoti)
	{
		return challengeTeam.getTeamMembers().stream().map(teamMember -> {
					SppoterEntity sppotiMember = new SppoterEntity();
					sppotiMember.setTeamMember(teamMember);
					sppotiMember.setSppoti(sppoti);
					
					if (teamMember.getAdmin())
						sppotiMember.setStatus(GlobalAppStatusEnum.CONFIRMED);
					
					return sppotiMember;
				}
		
		).collect(Collectors.toSet());
	}
	
	/**
	 * Get timeZone from security context.
	 */
	public String getTimeZone() {
		final AccountUserDetails accountUserDetails = (AccountUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		return accountUserDetails.getTimeZone();
	}
}