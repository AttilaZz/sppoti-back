package com.fr.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.entities.*;
import com.fr.service.PostControllerService;
import com.fr.transformers.CommentTransformer;
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
	
	/** Post list size. */
	@Value("${key.postsPerPage}")
	private int postSize;
	
	/** Comment transformer. */
	private final CommentTransformer commentTransformer;
	
	/** Init dependencies. */
	@Autowired
	public PostControllerServiceImpl(final CommentTransformer commentTransformer)
	{
		this.commentTransformer = commentTransformer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public PostEntity savePost(final PostEntity post)
	{
		final PostEntity postEntity = this.postRepository.save(post);
		
		if (postEntity != null && post.getTargetUserProfileUuid() != 0 &&
				post.getTargetUserProfileUuid() != getConnectedUser().getUuid()) {
			
			addNotification(NotificationTypeEnum.X_POSTED_ON_YOUR_PROFILE, getConnectedUser(),
					getUserByUuId(postEntity.getTargetUserProfileUuid()), null, null);
			
			if (post.getContent() != null) {
				addTagNotification(postEntity, null);
			}
			
		}
		
		return postEntity;
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
				final List<PostEntity> post = this.postRepository.getByUuid(postId);
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
		
		final List<PostEntity> postEntity = this.postRepository.getByUuid(postId);
		
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
		
		final List<PostEntity> posts = this.postRepository.getByUuid(id);
		
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
		
		return postEntityToDto(postEntities, userEntity);
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
		
		return postEntityToDto(postEntities, userEntity);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostDTO fillPostToSend(final int postId, final Long userId)
	{
		
		
		final List<PostEntity> posts = this.postRepository.getByUuid(postId);
		if (posts.isEmpty()) {
			throw new EntityNotFoundException("Post id (" + postId + ") introuvable.");
		}
		
		final UserEntity userEntity = this.userRepository.findOne(userId);
		if (userEntity == null) {
			throw new EntityNotFoundException("User id (" + userId + ") not found !!");
		}
		
		return postEntityToDto(posts, userEntity).get(0);
		
	}
	
	/**
	 * @param postEntities
	 * 		list of post entities.
	 * @param userPost
	 * 		post creator.
	 *
	 * @return list of all posts.
	 */
	public List<PostDTO> postEntityToDto(final List<PostEntity> postEntities, final UserEntity userPost)
	{
		
		final List<PostDTO> postDTOS = new ArrayList<PostDTO>();
		
		postEntities.forEach(
				
				p -> {
					final PostDTO pres = new PostDTO();
					
					final UserEntity owner = p.getUser();
					
					pres.setId(p.getUuid());
					
					if (p.getContent() != null)
						pres.setContent(p.getContent());
					
					pres.setDatetimeCreated(p.getDatetimeCreated());
					
					pres.setMyPost(userPost.getId().equals(owner.getId()));
					
					if (p.getAlbum() != null)
						pres.setImageLink(p.getAlbum());
					
					if (p.getVideo() != null)
						pres.setVideoLink(p.getVideo());
					
					pres.setSportId(p.getSport().getId());
					
					//Add address
					if (!p.getAddresses().isEmpty()) {
						//                        pres.setAddresses(p.getAddresses());
					}
					
					// check if content has been modified or not
					final List<EditHistoryEntity> editHistory = this.editHistoryRepository
							.getByPostUuidOrderByDatetimeEditedDesc(p.getUuid());
					
					if (!editHistory.isEmpty()) {
						pres.setEdited(true);
						final EditHistoryEntity ec = editHistory.get(0);
						pres.setDatetimeCreated(ec.getDatetimeEdited());
						if (ec.getText() != null) {
							pres.setContent(ec.getText());
						}
						
						if (ec.getSport() != null) {
							final Long spId = ec.getSport().getId();
							pres.setSportId(spId);
						}
					} else {
						// post has not been edited - set initial params
						if (p.getContent() != null) {
							pres.setContent(p.getContent());
						}
						if (p.getSport() != null && p.getSport().getId() != null) {
							pres.setSportId(p.getSport().getId());
						}
						pres.setDatetimeCreated(p.getDatetimeCreated());
					}

            /*
			Manage commentEntities count + last like
             */
					final Set<CommentEntity> commentEntities = new TreeSet<>();
					commentEntities.addAll(p.getCommentEntities());
					pres.setCommentsCount(commentEntities.size());
					
					final List<CommentEntity> commentsListTemp = new ArrayList<CommentEntity>();
					commentsListTemp.addAll(commentEntities);
					
					final List<CommentDTO> commentList = new ArrayList<CommentDTO>();
					if (!commentsListTemp.isEmpty()) {
						final CommentEntity commentEntity = commentsListTemp.get(commentEntities.size() - 1);
						
						final CommentDTO commentModelDTO = this.commentTransformer.modelToDto(commentEntity);
						commentModelDTO.setMyComment(commentEntity.getUser().getId().equals(userPost.getId()));
						commentModelDTO.setLikedByUser(isContentLikedByUser(commentEntity, userPost.getId()));
						commentModelDTO.setLikeCount(commentEntity.getLikes().size());
						
						commentList.add(commentModelDTO);
					}
					
					pres.setComment(commentList);

            /*
			End managing commentEntities
             */

            /*
			manage post like + count like
             */
					pres.setLikeCount(p.getLikes().size());
					
					final boolean isPostLikedByMe = isContentLikedByUser(p, userPost.getId());
					pres.setLikedByUser(isPostLikedByMe);

            /*
			set post owner info
             */
					pres.setFirstName(owner.getFirstName());
					pres.setLastName(owner.getLastName());
					pres.setUsername(owner.getUsername());
					
					final List<ResourcesEntity> resources = new ArrayList<ResourcesEntity>();
					resources.addAll(owner.getResources());
					
					if (!resources.isEmpty()) {
						if (resources.get(0) != null && resources.get(0).getType() == 1) {
							pres.setAvatar(resources.get(0).getUrl());
						} else if (resources.get(1) != null && resources.get(1).getType() == 1) {
							pres.setAvatar(resources.get(1).getUrl());
						}
					}

            /*
			Check if post has been posted on a friend profile -- default value for integer is ZERO (UUID can never be a zero)
             */
					if (p.getTargetUserProfileUuid() != 0) {
						
						final UserEntity target = getUserByUuId(p.getTargetUserProfileUuid());
						
						pres.setTargetUser(target.getFirstName(), target.getLastName(), target.getUsername(),
								target.getUuid(), userPost.getId().equals(target.getId()));
						
					}
					
					//set visibility
					pres.setVisibility(p.getVisibility());
					
					//return all formated posts
					postDTOS.add(pres);
					
				}
		
		);
		
		return postDTOS;
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
		
		final List<PostEntity> posts = this.postRepository.getByUuidAndDeletedFalseOrderByDatetimeCreatedDesc(id, null);
		
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
		return this.friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(friendId, connectedUserUuid) !=
				null;
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
			final List<PostEntity> postEntities = new ArrayList<>();
			
			//get recent posts from each friend
			this.friendShipRepository
					.findByUserUuidOrFriendUuidAndStatusAndDeletedFalse(userId, userId, GlobalAppStatusEnum.CONFIRMED,
							pageable).forEach(f -> {
				if (f.getFriend().getUuid() != userId) {
					postEntities.addAll(this.postRepository
							.getByUserUuidAndDeletedFalse(f.getFriend().getUuid(), pageable));
				} else if (f.getUser().getUuid() != userId) {
					postEntities
							.addAll(this.postRepository.getByUserUuidAndDeletedFalse(f.getUser().getUuid(), pageable));
				}
			});
			
			//add connected user posts
			postEntities
					.addAll(this.postRepository.getByUserUuidAndDeletedFalse(getConnectedUser().getUuid(), pageable));
			
			//transform posts from entities to dto, with sorting by creation date.
			return postEntities.stream().map(p -> this.fillPostToSend(p.getUuid(), accountUserId))
					.sorted((u1, u2) -> u2.getDatetimeCreated().compareTo(u1.getDatetimeCreated()))
					.collect(Collectors.toList());
			
		}
		
		throw new EntityNotFoundException("User (" + userId + ") not found");
		
	}
}
