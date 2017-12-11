package com.fr.impl;

import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.dto.post.PostRequestDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.service.NotificationBusinessService;
import com.fr.service.PostBusinessService;
import com.fr.service.email.PostMailerService;
import com.fr.transformers.PostTransformer;
import com.fr.transformers.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.fr.commons.enumeration.notification.NotificationObjectType.POST;
import static com.fr.commons.enumeration.notification.NotificationTypeEnum.X_POSTED_ON_YOUR_PROFILE;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Component
class PostBusinessServiceImpl extends CommonControllerServiceImpl implements PostBusinessService
{
	private final Logger LOGGER = LoggerFactory.getLogger(PostBusinessServiceImpl.class);
	
	private final PostMailerService postMailerService;
	private final PostTransformer postTransformer;
	private final NotificationBusinessService notificationService;
	private final UserTransformer userTransformer;
	
	@Value("${key.postsPerPage}")
	private int postSize;
	
	@Autowired
	public PostBusinessServiceImpl(final PostMailerService postMailerService, final PostTransformer postTransformer,
								   final NotificationBusinessService notificationService,
								   final UserTransformer userTransformer)
	{
		this.postMailerService = postMailerService;
		this.postTransformer = postTransformer;
		this.notificationService = notificationService;
		this.userTransformer = userTransformer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public PostDTO savePost(final PostRequestDTO dto)
	{
		final UserEntity connectedUser = getConnectedUser();
		
		final PostEntity entity = new PostEntity();
		entity.setAlbum(dto.getContent().getImageLink());
		entity.setContent(dto.getContent().getContent());
		entity.setVideo(dto.getContent().getVideoLink());
		entity.setVisibility(dto.getVisibility());
		entity.setTimeZone(dto.getTimeZone());
		entity.setUser(connectedUser);
		
		//Post sport.
		final SportEntity sportEntity = this.sportRepository.findOne(dto.getSportId());
		if (sportEntity == null) {
			throw new EntityNotFoundException("Sport not found");
		}
		entity.setSport(sportEntity);
		
		//Post in friend profile.
		final Optional<UserEntity> targetProfile = this.userRepository
				.getByUuidAndDeletedFalseAndConfirmedTrue(dto.getTargetUserUuid());
		entity.setConnectedUserId(connectedUser.getId());
		
		targetProfile.ifPresent(entity::setTargetUserProfile);
		
		targetProfile.ifPresent(entity::setTargetUserProfile);
		
		final PostEntity savedPost = this.postRepository.saveAndFlush(entity);
		
		targetProfile.ifPresent(t -> {
			//Send notification
			if (!t.getId().equals(connectedUser.getId())) {
				
				this.notificationService
						.saveAndSendNotificationToUsers(getConnectedUser(), savedPost.getTargetUserProfile(), POST,
								X_POSTED_ON_YOUR_PROFILE, savedPost);
				
				this.postMailerService.sendEmailToTargetProfileOwner(this.userTransformer.modelToDto(t));
			}
		});
		
		if (dto.getContent() != null) {
			this.notificationService.checkForTagNotification(savedPost, null);
		}
		
		savedPost.setConnectedUserId(connectedUser.getId());
		return this.postTransformer.modelToDto(savedPost);
	}

    /*
	 * The post update method is used to update the location (address) of the
     * post and the content. This two jobs are separated, not at the same time.
     *
     * That's why we check if the address is not null before persisting the new
     * content
     */
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updatePost(final EditHistoryEntity postEditRow, final SortedSet<AddressEntity> postEditAddress,
						   final String postId)
	{
		if (postEditAddress != null) {
			
			final Optional<PostEntity> optional = this.postRepository.findTopByUuidAndDeletedFalse(postId);
			
			optional.ifPresent(p -> this.postRepository.save(p));
			
			optional.orElseThrow(() -> new IllegalArgumentException("Trying to update non existing post"));
			
		} else {
			this.editHistoryRepository.save(postEditRow);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deletePost(final String postId)
	{
		this.LOGGER.info("Deleting post: {}", postId);
		
		final Optional<PostEntity> optional = this.postRepository.findTopByUuidAndDeletedFalse(postId);
		
		optional.orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		optional.ifPresent(p -> {
			p.setDeleted(true);
			this.postRepository.save(p);
			this.LOGGER.info("Post has been deleted: {}", p);
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostEntity findPost(final String id)
	{
		final Optional<PostEntity> optional = this.postRepository.findTopByUuidAndDeletedFalse(id);
		
		return optional.orElse(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SportEntity getSportToUse(final Long id)
	{
		return this.sportRepository.getById(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SppotiEntity getSppotiById(final Long id)
	{
		return this.sppotiRepository.getOne(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostDTO> getPhotoGallery(final String userId, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.postSize);
		
		final UserEntity userEntity = getUserByUuId(userId);
		if (userEntity == null) {
			throw new EntityNotFoundException("User id (" + userId + ") not found !!");
		}
		
		final List<PostEntity> postEntities = this.postRepository
				.getByAlbumIsNotNullAndDeletedFalseAndUserUuidOrderByDatetimeCreatedDesc(userId, pageable);
		
		postEntities.forEach(p -> p.setConnectedUserId(userEntity.getId()));
		
		return this.postTransformer.modelToDto(postEntities);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostDTO> getVideoGallery(final String userId, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.postSize);
		
		final UserEntity userEntity = getUserByUuId(userId);
		if (userEntity == null) {
			throw new EntityNotFoundException("User id (" + userId + ") not found !!");
		}
		
		final List<PostEntity> postEntities = this.postRepository
				.getByVideoIsNotNullAndDeletedFalseAndUserUuidOrderByDatetimeCreatedDesc(userId, pageable);
		
		postEntities.forEach(p -> p.setConnectedUserId(userEntity.getId()));
		
		return this.postTransformer.modelToDto(postEntities);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostDTO fillPostToSend(final String postId, final Long userId)
	{
		this.LOGGER.info("Filling post ({}) to return to the user: ({})", postId, userId);
		final Optional<PostEntity> optional = this.postRepository.findTopByUuidAndDeletedFalse(postId);
		
		if (optional.isPresent()) {
			optional.get().setConnectedUserId(getConnectedUserId());
			return this.postTransformer.modelToDto(optional.get());
		}
		
		throw new EntityNotFoundException("Post id (" + postId + ") not found.");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContentEditedResponseDTO> getAllPostHistory(final String id, final int page)
	{
		final Pageable pageable = new PageRequest(page, this.postSize, Sort.Direction.DESC, "datetimeEdited");
		
		final List<EditHistoryEntity> postHistory = this.editHistoryRepository
				.getByPostUuidOrderByDatetimeEditedDesc(id, pageable);
		
		return fillEditContentResponse(postHistory);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EditHistoryEntity> getLastModification(final String postId)
	{
		//        return editContentDaoService.getLastEditedPost(postId);
		return this.editHistoryRepository.getByPostUuidOrderByDatetimeEditedDesc(postId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SportEntity getSportById(final Long sportId)
	{
		return this.sportRepository.getOne(sportId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void editPostVisibility(final String id, final int visibility)
	{
		
		final List<PostEntity> posts = this.postRepository.getByUuidAndDeletedFalseOrderByDatetimeCreatedDesc(id);
		
		if (posts.isEmpty()) {
			throw new EntityNotFoundException("Post Not found");
		}
		
		final PostEntity post = posts.get(0);
		post.setVisibility(visibility);
		
		this.postRepository.save(post);
		this.LOGGER.info("Visibility has been edited for the post {} to {}", id, visibility);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostEntity> findAllPosts(final Long userLongId, final String userIntId, final List visibility,
										 final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.postSize, Sort.Direction.DESC, "datetimeCreated");
		
		return this.postRepository.getAllPosts(userIntId, userLongId, visibility, pageable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTargetUserFriendOfMe(final String connectedUserUuid, final String friendId)
	{
		return this.friendShipRepository
				.findTopByFriendUuidAndUserUuidAndStatusNotInOrderByDatetimeCreatedDesc(friendId, connectedUserUuid,
						SppotiUtils.statusToFilter()) != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostDTO> getAllUserPosts(final Long connectedUserId, final String connectedUserUuid,
										 final String userID, final int page)
	{
		final List<PostEntity> posts;
		
		final UserEntity requestUser = this.getUserByUuId(userID);
		
		if (requestUser == null) {
			throw new EntityNotFoundException("User (" + userID + ") not found");
		}
		
		if (connectedUserId.equals(requestUser.getId())) {
			//get connected user posts - visibility: 0,1,2
			final List visibility = Arrays.asList(0, 1, 2);
			posts = this.findAllPosts(connectedUserId, userID, visibility, page);
		} else if (this.isTargetUserFriendOfMe(connectedUserUuid, userID)) {
			//get friend posts - visibility: 0,1
			final List visibility = Arrays.asList(0, 1);
			posts = this.findAllPosts(requestUser.getId(), userID, visibility, page);
		} else {
			//get unknown user posts - visibility: 0
			final List visibility = Collections.singletonList(0);
			posts = this.findAllPosts(requestUser.getId(), userID, visibility, page);
		}
		
		final List<PostDTO> postDTOS = new ArrayList<>();
		posts.forEach(t -> postDTOS.add(this.fillPostToSend(t.getUuid(), connectedUserId)));
		
		return postDTOS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostDTO> getAllTimelinePosts(final String userId, final int page, final Long accountUserId)
	{
		
		final Pageable pageable = new PageRequest(page, this.postSize, Sort.Direction.DESC, "datetimeCreated");
		
		final Optional<UserEntity> optional = this.userRepository.getByUuidAndDeletedFalseAndConfirmedTrue(userId);
		
		if (optional.isPresent()) {
			final List<Long> listOfAllUsersIdRelatedToTheTimeline = new ArrayList<>();
			
			listOfAllUsersIdRelatedToTheTimeline.add(getConnectedUserId());
			
			//add user's friend to the list
			this.friendShipRepository
					.findByUserUuidOrFriendUuidAndStatus(userId, userId, GlobalAppStatusEnum.CONFIRMED, null)
					.forEach(f -> {
						if (!f.getFriend().getUuid().equals(userId)) {
							listOfAllUsersIdRelatedToTheTimeline.add(f.getFriend().getId());
						} else if (!f.getUser().getUuid().equals(userId)) {
							listOfAllUsersIdRelatedToTheTimeline.add(f.getUser().getId());
						}
					});
			
			final List<PostEntity> allPostsToDisplayInTheTimeline = this.postRepository
					.findByDeletedFalseAndUserIdIn(listOfAllUsersIdRelatedToTheTimeline, pageable);
			
			//transform posts from entities to dto, with sorting by creation date.
			return allPostsToDisplayInTheTimeline.stream().map(p -> this.fillPostToSend(p.getUuid(), accountUserId))
					.collect(Collectors.toList());
		}
		
		throw new EntityNotFoundException("User (" + userId + ") not found");
	}
}