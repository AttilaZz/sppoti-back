package com.fr.impl;

import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.dto.post.PostRequestDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.entities.*;
import com.fr.service.PostControllerService;
import com.fr.transformers.CommentTransformer;
import com.fr.transformers.PostTransformer;
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

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Component
class PostControllerServiceImpl extends AbstractControllerServiceImpl implements PostControllerService
{
	
	/** Comment transformer. */
	private final CommentTransformer commentTransformer;
	
	/** Post transformer. */
	private final PostTransformer postTransformer;
	
	/** Post list size. */
	@Value("${key.postsPerPage}")
	private int postSize;
	
	/** Init dependencies. */
	@Autowired
	public PostControllerServiceImpl(final CommentTransformer commentTransformer, final PostTransformer postTransformer)
	{
		this.commentTransformer = commentTransformer;
		this.postTransformer = postTransformer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public PostDTO savePost(final PostRequestDTO postRequestDTO)
	{
		final UserEntity connectedUSer = getConnectedUser();
		
		final PostEntity entity = new PostEntity();
		entity.setAlbum(postRequestDTO.getContent().getImageLink());
		entity.setContent(postRequestDTO.getContent().getContent());
		entity.setVideo(postRequestDTO.getContent().getVideoLink());
		entity.setVisibility(postRequestDTO.getVisibility());
		
		//Post sport.
		final SportEntity sportEntity = this.sportRepository.findOne(postRequestDTO.getSportId());
		if (sportEntity == null) {
			throw new EntityNotFoundException("Sport not found");
		}
		entity.setSport(sportEntity);
		
		//Post in friend profile.
		final Optional<UserEntity> targetProfile = this.userRepository
				.getByUuidAndDeletedFalse(postRequestDTO.getTargetUserUuid());
		targetProfile.ifPresent(entity::setTargetUserProfile);
		targetProfile.orElseThrow(() -> new EntityNotFoundException("Target profile not found !!"));
		
		//Saved post final
		final PostEntity savedPost = this.postRepository.save(entity);
		savedPost.setConnectedUserId(connectedUSer.getId());
		
		//Send notification
		if (savedPost.getTargetUserProfile() != null &&
				savedPost.getTargetUserProfile().getId().equals(connectedUSer.getId())) {
			
			addNotification(NotificationTypeEnum.X_POSTED_ON_YOUR_PROFILE, getConnectedUser(),
					savedPost.getTargetUserProfile(), null, null, savedPost, null);
			
			//Tag notification
			if (savedPost.getContent() != null) {
				addTagNotification(savedPost, null);
			}
			
		}
		
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
	public boolean updatePost(final EditHistoryEntity postEditRow, final SortedSet<AddressEntity> postEditAddress,
							  final int postId)
	{
		if (postEditAddress != null) {
			
			try {
				final List<PostEntity> post = this.postRepository.getByUuidAndDeletedFalse(postId);
				if (post == null) {
					throw new IllegalArgumentException("Trying to update non existing post");
				} else {
					post.get(0).setAddresses(postEditAddress);
				}
				
				this.postRepository.save(post);
			} catch (final Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try {
				this.editHistoryRepository.save(postEditRow);
			} catch (final Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deletePost(final int postId)
	{
		
		final List<PostEntity> postEntity = this.postRepository.getByUuidAndDeletedFalse(postId);
		
		if (postEntity.isEmpty()) {
			throw new EntityNotFoundException("Post (" + postId + ") not found");
		}
		
		postEntity.get(0).setDeleted(true);
		this.postRepository.save(postEntity.get(0));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostEntity findPost(final int id)
	{
		
		final List<PostEntity> posts = this.postRepository.getByUuidAndDeletedFalse(id);
		
		if (posts == null || posts.isEmpty()) {
			return null;
		} else {
			return posts.get(0);
		}
		
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
	public List<PostDTO> getPhotoGallery(final int userId, final int page)
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
	public List<PostDTO> getVideoGallery(final int userId, final int page)
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
	public PostDTO fillPostToSend(final int postId, final Long userId)
	{
		
		
		final List<PostEntity> postEntities = this.postRepository.getByUuidAndDeletedFalse(postId);
		if (postEntities.isEmpty()) {
			throw new EntityNotFoundException("Post id (" + postId + ") introuvable.");
		}
		
		final UserEntity userEntity = this.userRepository.findOne(userId);
		if (userEntity == null) {
			throw new EntityNotFoundException("User id (" + userId + ") not found !!");
		}
		
		postEntities.forEach(p -> p.setConnectedUserId(userEntity.getId()));
		
		return this.postTransformer.modelToDto(postEntities.get(0));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContentEditedResponseDTO> getAllPostHistory(final int id, final int page)
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
	public List<EditHistoryEntity> getLastModification(final int postId)
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
	public void editPostVisibility(final int id, final int visibility)
	{
		
		final List<PostEntity> posts = this.postRepository.getByUuidAndDeletedFalseOrderByDatetimeCreatedDesc(id);
		
		if (posts.isEmpty()) {
			throw new EntityNotFoundException("Post Not found");
		}
		
		final PostEntity post = posts.get(0);
		post.setVisibility(visibility);
		
		this.postRepository.save(post);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostEntity> findAllPosts(final Long userLongId, final int userIntId, final List visibility,
										 final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.postSize, Sort.Direction.DESC, "datetimeCreated");
		
		return this.postRepository.getAllPosts(userIntId, userLongId, visibility, pageable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTargetUserFriendOfMe(final int connectedUserUuid, final int friendId)
	{
		return this.friendShipRepository
				.findLastByFriendUuidAndUserUuidAndDeletedFalseOrderByDatetimeCreatedDesc(friendId,
						connectedUserUuid) != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostDTO> getAllUserPosts(final Long connectedUserId, final int connectedUserUuid, final int userID,
										 final int page)
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
	public List<PostDTO> getAllTimelinePosts(final int userId, final int page, final Long accountUserId)
	{
		
		final Pageable pageable = new PageRequest(page, this.postSize, Sort.Direction.DESC, "datetimeCreated");
		
		final Optional<UserEntity> optional = this.userRepository.getByUuidAndDeletedFalse(userId);
		
		if (optional.isPresent()) {
			final List<PostEntity> returnedPostEntities = new ArrayList<>();
			
			//get recent posts from each friend
			this.friendShipRepository
					.findByUserUuidOrFriendUuidAndStatusAndDeletedFalse(userId, userId, GlobalAppStatusEnum.CONFIRMED,
							pageable).forEach(f -> {
				
				if (f.getFriend().getUuid() != userId) {
					returnedPostEntities.addAll(this.postRepository
							.getByUserUuidAndDeletedFalse(f.getFriend().getUuid(), pageable));
				} else if (f.getUser().getUuid() != userId) {
					returnedPostEntities
							.addAll(this.postRepository.getByUserUuidAndDeletedFalse(f.getUser().getUuid(), pageable));
				}
				
			});
			
			//add connected user posts
			returnedPostEntities
					.addAll(this.postRepository.getByUserUuidAndDeletedFalse(getConnectedUser().getUuid(), pageable));
			
			//transform posts from entities to dto, with sorting by creation date.
			return returnedPostEntities.stream().map(p -> this.fillPostToSend(p.getUuid(), accountUserId))
					.sorted((u1, u2) -> u2.getDatetimeCreated().compareTo(u1.getDatetimeCreated()))
					.collect(Collectors.toList());
			
		}
		
		throw new EntityNotFoundException("User (" + userId + ") not found");
		
	}
}
