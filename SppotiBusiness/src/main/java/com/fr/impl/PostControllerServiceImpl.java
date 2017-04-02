package com.fr.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.post.PostResponseDTO;
import com.fr.entities.*;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.service.PostControllerService;
import com.fr.transformers.CommentTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Component
class PostControllerServiceImpl extends AbstractControllerServiceImpl implements PostControllerService {

    private static Logger LOGGER = Logger.getLogger(PostControllerServiceImpl.class);

    @Value("${key.postsPerPage}")
    private int postSize;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public PostEntity savePost(PostEntity post) {
        PostEntity postEntity = postRepository.save(post);

        if (postEntity != null && post.getTargetUserProfileUuid() != 0 && post.getTargetUserProfileUuid() != getConnectedUser().getUuid()) {

            addNotification(NotificationTypeEnum.X_POSTED_ON_YOUR_PROFILE, getConnectedUser(), getUserByUuId(postEntity.getTargetUserProfileUuid()), null, null);

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
    public boolean updatePost(EditHistoryEntity postEditRow, SortedSet<AddressEntity> postEditAddress, int postId) {
        if (postEditAddress != null) {

            try {
                List<PostEntity> post = postRepository.getByUuid(postId);
                if (post == null) {
                    throw new IllegalArgumentException("Trying to update non existing post");
                } else {
                    post.get(0).setAddresses(postEditAddress);
                }

                postRepository.save(post);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                editHistoryRepository.save(postEditRow);
            } catch (Exception e) {
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
    public void deletePost(int postId) {

        List<PostEntity> postEntity = postRepository.getByUuid(postId);

        if (postEntity.isEmpty()) {
            throw new EntityNotFoundException("Post (" + postId + ") not found");
        }

        postEntity.get(0).setDeleted(true);
        postRepository.save(postEntity.get(0));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostEntity findPost(int id) {

        List<PostEntity> posts = postRepository.getByUuid(id);

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
    public SportEntity getSportToUse(Long id) {

        return sportRepository.getById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SppotiEntity getSppotiById(Long id) {

        return sppotiRepository.getOne(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostResponseDTO> getPhotoGallery(int userId, int page) {

        Pageable pageable = new PageRequest(page, postSize);

        UserEntity userEntity = getUserByUuId(userId);
        if (userEntity == null) {
            throw new EntityNotFoundException("User id (" + userId + ") not found !!");
        }

        List<PostEntity> postEntities = postRepository.getByAlbumIsNotNullAndUserUuidOrderByDatetimeCreatedDesc(userId, pageable);

        return postEntityToDto(postEntities, userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostResponseDTO> getVideoGallery(int userId, int page) {
        Pageable pageable = new PageRequest(page, postSize);

        UserEntity userEntity = getUserByUuId(userId);
        if (userEntity == null) {
            throw new EntityNotFoundException("User id (" + userId + ") not found !!");
        }

        List<PostEntity> postEntities = postRepository.getByVideoIsNotNullAndUserUuidOrderByDatetimeCreatedDesc(userId, pageable);

        return postEntityToDto(postEntities, userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostResponseDTO fillPostToSend(int postId, Long userId) {


        List<PostEntity> posts = postRepository.getByUuid(postId);
        if (posts.isEmpty()) {
            throw new EntityNotFoundException("Post id (" + postId + ") introuvable.");
        }

        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null) {
            throw new EntityNotFoundException("User id (" + userId + ") not found !!");
        }

        return postEntityToDto(posts, userEntity).get(0);

    }

    /**
     * @param postEntities list of post entities.
     * @param userPost     post creator.
     * @return list of all posts.
     */
    public List<PostResponseDTO> postEntityToDto(final List<PostEntity> postEntities, final UserEntity userPost) {

        List<PostResponseDTO> postResponseDTOs = new ArrayList<PostResponseDTO>();

        postEntities.forEach(

                p -> {
                    PostResponseDTO pres = new PostResponseDTO();

                    UserEntity owner = p.getUser();

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
                    List<EditHistoryEntity> editHistory = editHistoryRepository.getByPostUuidOrderByDatetimeEditedDesc(p.getUuid());

                    if (!editHistory.isEmpty()) {
                        pres.setEdited(true);
                        EditHistoryEntity ec = editHistory.get(0);
                        pres.setDatetimeCreated(ec.getDatetimeEdited());
                        if (ec.getText() != null) {
                            pres.setContent(ec.getText());
                        }

                        if (ec.getSport() != null) {
                            Long spId = ec.getSport().getId();
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
                    Set<CommentEntity> commentEntities = new TreeSet<>();
                    commentEntities.addAll(p.getCommentEntities());
                    pres.setCommentsCount(commentEntities.size());

                    List<CommentEntity> commentsListTemp = new ArrayList<CommentEntity>();
                    commentsListTemp.addAll(commentEntities);

                    List<CommentDTO> commentList = new ArrayList<CommentDTO>();
                    if (!commentsListTemp.isEmpty()) {
                        CommentEntity commentEntity = commentsListTemp.get(commentEntities.size() - 1);

                        CommentDTO commentModelDTO = CommentTransformer.commentEntityToDto(commentEntity, commentEntity.getUser());
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

                    boolean isPostLikedByMe = isContentLikedByUser(p, userPost.getId());
                    pres.setLikedByUser(isPostLikedByMe);

            /*
            set post owner info
             */
                    pres.setFirstName(owner.getFirstName());
                    pres.setLastName(owner.getLastName());
                    pres.setUsername(owner.getUsername());

                    List<ResourcesEntity> resources = new ArrayList<ResourcesEntity>();
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

                        UserEntity target = getUserByUuId(p.getTargetUserProfileUuid());

                        pres.setTargetUser(target.getFirstName(), target.getLastName(), target.getUsername(), target.getUuid(), userPost.getId().equals(target.getId()));

                    }

                    //set visibility
                    pres.setVisibility(p.getVisibility());

                    //return all formated posts
                    postResponseDTOs.add(pres);

                }

        );

        return postResponseDTOs;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ContentEditedResponseDTO> getAllPostHistory(int id, int page) {

        Pageable pageable = new PageRequest(page, postSize);

        List<EditHistoryEntity> postHistory = editHistoryRepository.getByPostUuidOrderByDatetimeEditedDesc(id, pageable);

        return fillEditContentResponse(postHistory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EditHistoryEntity> getLastModification(int postId) {
//        return editContentDaoService.getLastEditedPost(postId);
        return editHistoryRepository.getByPostUuidOrderByDatetimeEditedDesc(postId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SportEntity getSportById(Long sportId) {
        return sportRepository.getOne(sportId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void editPostVisibility(int id, int visibility) {

        List<PostEntity> posts = postRepository.getByUuidOrderByDatetimeCreatedDesc(id, null);

        if (posts.isEmpty()) {
            throw new EntityNotFoundException("Post Not found");
        }

        PostEntity post = posts.get(0);
        post.setVisibility(visibility);

        postRepository.save(post);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostEntity> findAllPosts(Long userLongId, int userIntId, List visibility, int page) {

        Pageable pageable = new PageRequest(page, postSize, Sort.Direction.DESC, "datetimeCreated");

        return postRepository.getAllPosts(userIntId, userLongId, visibility, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetUserFriendOfMe(int connectedUserUuid, int friendId) {
        return friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(friendId, connectedUserUuid) != null;
    }
}