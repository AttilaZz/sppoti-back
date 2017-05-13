package com.fr.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.commons.exception.NotAdminException;
import com.fr.commons.exception.TeamMemberNotFoundException;
import com.fr.entities.*;
import com.fr.mail.SppotiMailer;
import com.fr.mail.TeamMailer;
import com.fr.repositories.*;
import com.fr.service.AbstractControllerService;
import com.fr.transformers.TeamTransformer;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.TeamMemberTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
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
	
	/**
	 * Team mailer.
	 */
	@Autowired
	TeamMailer teamMailer;
	
	/**
	 * Sppoti mailer.
	 */
	@Autowired
	SppotiMailer sppotiMailer;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private TeamMemberTransformer teamMemberTransformer;
	
	@Autowired
	private UserTransformer userTransformer;
	
	@Autowired
	private TeamTransformer teamTransformer;
	
	private final Logger LOGGER = Logger.getLogger(AbstractControllerServiceImpl.class);
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUserRole()
	{
		
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final ArrayList<GrantedAuthority> roles;
		
		final List<String> userRoles = new ArrayList<String>();
		
		if (principal instanceof UserDetails) {
			roles = (ArrayList<GrantedAuthority>) ((UserDetails) principal).getAuthorities();
			for (final GrantedAuthority role : roles) {
				userRoles.add(role.getAuthority());
			}
		}
		return userRoles;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthenticationUsername()
	{
		final String userName;
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUserLoginType(final String username)
	{
		final String numberRegex = "[0-9]+";
		final int loginType;
		
		if (username.contains("@")) {
			loginType = 2;
		} else if (username.matches(numberRegex)) {
			loginType = 3;
		} else {
			loginType = 1;
		}
		
		return loginType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getUserById(final Long id)
	{
		return this.userRepository.getByIdAndDeletedFalse(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getUserByUuId(final int id)
	{
		
		final Optional<UserEntity> usersList = this.userRepository.getByUuidAndDeletedFalse(id);
		
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
		final UserDetails accountUserDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return this.getUserByLogin(accountUserDetails.getUsername());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getUserByLogin(final String username)
	{
		final String numberRegex = "[0-9]+";
		
		if (username.contains("@")) {
			return this.userRepository.getByEmailAndDeletedFalse(username);
		} else if (username.matches(numberRegex)) {
			return this.userRepository.getByTelephoneAndDeletedFalse(username);
		} else {
			return this.userRepository.getByUsernameAndDeletedFalse(username);
		}
	}
	
	/**
	 * @param dbCommentEntityList
	 * 		like entity.
	 * @param userId
	 * 		user id.
	 *
	 * @return list of Comment DTO
	 */
	protected List<CommentDTO> fillCommentModelList(final List<CommentEntity> dbCommentEntityList, final Long userId)
	{
		final List<CommentDTO> myList = new ArrayList<CommentDTO>();
		
		for (final CommentEntity commentEntity : dbCommentEntityList) {
			final int commentId = commentEntity.getUuid();
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
			
			final boolean isCommentLikedByMe = isContentLikedByUser(commentEntity, userId);
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
	 * @param o
	 * 		post or like entity.
	 * @param userId
	 * 		liker id.
	 *
	 * @return true if content has been liked by me, false otherwise
	 */
	// detect if post or like has already been liked by user
	protected boolean isContentLikedByUser(final Object o, final Long userId)
	{
		
		final List<LikeContentEntity> lp = new ArrayList<LikeContentEntity>();
		final PostEntity p;
		final CommentEntity c;
		
		if (o instanceof PostEntity) {
			p = (PostEntity) o;
			lp.addAll(p.getLikes());
		} else if (o instanceof CommentEntity) {
			c = (CommentEntity) o;
			lp.addAll(c.getLikes());
		}
		
		for (final LikeContentEntity likePost : lp) {
			if (likePost.getUser().getId().equals(userId)) {
				return true;
			}
		}
		return false;
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
		final Set<NotificationEntity> notificationEntities = new HashSet<>();
		
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
				teamMember.setUsers(user);
				
				if (sppoti != null) {
					final TeamMemberEntity sppoterMember = this.teamMembersRepository
							.findByUsersUuidAndTeamUuid(userDTO.getId(), team.getUuid());

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

                    /* send TEAM && Sppoti notification And TEAM Email to the invited user. */
					if (!user.getId().equals(connectedUserId)) {
						notificationEntities
								.add(getNotificationEntity(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_SPPOTI,
										getUserById(connectedUserId), user, null, sppoti));
						teamNotificationAndEmail(team, notificationEntities, connectedUserId, user);
					}
					
				} else {
					/* if request coming from add team - add members only in (users_team). */
					if (userDTO.getxPosition() != null && !userDTO.getxPosition().equals(0)) {
						teamMember.setxPosition(userDTO.getxPosition());
					}
					
					if (userDTO.getyPosition() != null && !userDTO.getyPosition().equals(0)) {
						teamMember.setyPosition(userDTO.getyPosition());
					}

                    /* send TEAM notification And TEAM Email to the invited user. */
					if (!user.getId().equals(connectedUserId)) {
						teamNotificationAndEmail(team, notificationEntities, connectedUserId, user);
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
	 * Send team member notification and Email.
	 */
	private void teamNotificationAndEmail(final TeamEntity team, final Set<NotificationEntity> notificationEntities,
										  final Long connectedUserId, final UserEntity userEntity)
	{
		this.sendTeamNotification(team, notificationEntities, connectedUserId, userEntity);
		//this.sendJoinTeamEmail(team, userEntity, adminEntity);
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
			teamUsers.add(this.teamMemberTransformer.modelToDto(memberEntity, sppoti));
		}
		final TeamDTO teamDTO = this.teamTransformer.modelToDto(team);
		teamDTO.setMembers(teamUsers);
		
		return teamDTO;
		
	}
	
	/**
	 * @param team
	 * 		team info.
	 * @param notificationEntities
	 * 		list of notif to send.
	 * @param adminId
	 * 		connected user id
	 * @param u
	 * 		user to notify.
	 */
	private void sendTeamNotification(final TeamEntity team, final Set<NotificationEntity> notificationEntities,
									  final Long adminId, final UserEntity u)
	{
		
		notificationEntities
				.add(getNotificationEntity(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM, getUserById(adminId), u,
						team, null));
		if (team.getNotificationEntities() != null) {
			team.getNotificationEntities().addAll(notificationEntities);
		} else {
			team.setNotificationEntities(notificationEntities);
		}
		
	}
	
	/**
	 * Add notification.
	 *
	 * @param notificationType
	 * 		notif type.
	 * @param userFrom
	 * 		notif sender.
	 * @param userTo
	 * 		notif receiver.
	 * @param teamEntity
	 * 		team information.
	 * @param sppoti
	 * 		sppoti info.
	 */
	@Transactional
	protected void addNotification(final NotificationTypeEnum notificationType, final UserEntity userFrom,
								   final UserEntity userTo, final TeamEntity teamEntity, final SppotiEntity sppoti)
	{
		final NotificationEntity notification = getNotificationEntity(notificationType, userFrom, userTo, teamEntity,
				sppoti);
		
		this.notificationRepository.save(notification);
	}
	
	/**
	 * Init notif entity.
	 */
	private NotificationEntity getNotificationEntity(final NotificationTypeEnum notificationType,
													 final UserEntity userFrom, final UserEntity userTo,
													 final TeamEntity teamEntity, final SppotiEntity sppotiEntity)
	{
		final NotificationEntity notification = new NotificationEntity();
		notification.setNotificationType(notificationType);
		notification.setFrom(userFrom);
		notification.setTo(userTo);
		notification.setTeam(teamEntity);
		notification.setSppoti(sppotiEntity);
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
			this.LOGGER.debug(matcher.group());
			String s = matcher.group().trim();
			s = s.replaceAll("[$]", "");
			tags.add(s);
		}
		
		/**
		 * Process each tag.
		 */
		for (final String username : tags) {
			final UserEntity userToNotify;
			
			userToNotify = this.userRepository.getByUsernameAndDeletedFalse(username);
			
			if (userToNotify != null) {
				if (commentEntity != null) {
					addNotification(NotificationTypeEnum.X_TAGGED_YOU_IN_A_COMMENT, commentEntity.getUser(),
							userToNotify, null, null);
				} else if (postEntity != null) {
					addNotification(NotificationTypeEnum.X_TAGGED_YOU_IN_A_POST, postEntity.getUser(), userToNotify,
							null, null);
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
	void CheckConnectedUserAccessPrivileges(final int userId)
	{
		if (getConnectedUser().getUuid() != userId) {
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
		
		final Thread thread = new Thread(() -> {
			this.teamMailer.sendJoinTeamEmail(teamDto, member, admin);
			this.LOGGER.info("Join team email has been sent successfully !");
		});
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
}