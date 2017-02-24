package com.fr.core;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.post.PostResponseDTO;
import com.fr.entities.*;
import com.fr.models.NotificationType;
import com.fr.rest.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import utils.EntitytoDtoTransformer;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Component
public class PostControllerServiceImpl extends AbstractControllerServiceImpl implements PostControllerService {

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

        if (postEntity != null && post.getTargetUserProfileUuid() != getConnectedUser().getUuid()) {

            addNotification(NotificationType.X_POSTED_ON_YOUR_PROFILE, getConnectedUser(), getUserByUuId(postEntity.getTargetUserProfileUuid()));

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

        postRepository.delete(postEntity.get(0));

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
    public List<PostResponseDTO> getPhotoGallery(Long userId, int page) {
        return fillPostResponseFromDbPost(page, userId, 1, null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostResponseDTO> getVideoGallery(Long userId, int page) {
        return fillPostResponseFromDbPost(page, userId, 2, null);
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

        return fillPostResponseFromDbPost(0, userId, 3, posts.get(0)).get(0);

    }

    private List<PostResponseDTO> fillPostResponseFromDbPost(int page, Long userId, int operationType, PostEntity postEntity) {

        Pageable pageable = new PageRequest(page, postSize);

        List<PostEntity> dbContent = new ArrayList<PostEntity>();

        switch (operationType) {
            case 1:

                dbContent = postRepository.getByAlbumIsNotNullOrderByDatetimeCreatedDesc(pageable);

                break;
            case 2:

                dbContent = postRepository.getByVideoIsNotNullOrderByDatetimeCreatedDesc(pageable);

                break;
            case 3:

                dbContent.add(postEntity);

                break;
            default:
                break;
        }

        List<PostResponseDTO> mContentResponse = new ArrayList<PostResponseDTO>();

        for (PostEntity post : dbContent) {

            PostResponseDTO pres = new PostResponseDTO();

            UserEntity owner = post.getUser();

            if (post.getId() != null)
                pres.setId(post.getUuid());

            if (post.getContent() != null)
                pres.setContent(post.getContent());

            if (post.getDatetimeCreated() != null)
                pres.setDatetimeCreated(post.getDatetimeCreated());


            pres.setMyPost(userId.equals(owner.getId()));


            if (post.getAlbum() != null)
                pres.setImageLink(post.getAlbum());

            if (post.getVideo() != null)
                pres.setVideoLink(post.getVideo());

//            if (post.getSppoti() != null)
//                pres.setGame(post.getSppoti());

            if (post.getSport() != null && post.getSport().getId() != null) {
                pres.setSportId(post.getSport().getId());
            }

            //Add address
            if (!post.getAddresses().isEmpty()) {

                pres.setAddresses(post.getAddresses());

            }

            // check if content has been modified or not
            List<EditHistoryEntity> editHistory = editHistoryRepository.getByPostUuidOrderByDatetimeEditedDesc(post.getUuid());

            if (!editHistory.isEmpty()) {

                // modification detected
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

                if (post.getContent() != null) {
                    pres.setContent(post.getContent());
                }

                if (post.getSport() != null && post.getSport().getId() != null) {
                    pres.setSportId(post.getSport().getId());
                }

                pres.setDatetimeCreated(post.getDatetimeCreated());
            }

            /*
            Manage commentEntities count + last like
             */
            Set<CommentEntity> commentEntities = post.getCommentEntities();
            pres.setCommentsCount(commentEntities.size());

            try {
                List<CommentEntity> commentsListTemp = new ArrayList<CommentEntity>();
                commentsListTemp.addAll(commentEntities);

                List<CommentDTO> commentList = new ArrayList<CommentDTO>();
                if (!commentsListTemp.isEmpty()) {
                    CommentEntity commentEntity = commentsListTemp.get(commentEntities.size() - 1);

                    CommentDTO commentModelDTO = new CommentDTO(commentEntity, EntitytoDtoTransformer.getUserCoverAndAvatar(commentEntity.getUser()));
                    commentModelDTO.setMyComment(commentEntity.getUser().getId().equals(userId));
                    commentModelDTO.setLikedByUser(isContentLikedByUser(commentEntity, userId));
                    commentModelDTO.setLikeCount(commentEntity.getLikes().size());

                    commentList.add(commentModelDTO);
                }

                pres.setComment(commentList);

            } catch (Exception e) {
                LOGGER.error("Error  asting set<CommentEntity> to List<CommentEntity>", e);
            }

            /*
            End managing commentEntities
             */

            /*
            manage post like + count like
             */
            pres.setLikeCount(post.getLikes().size());

            boolean isPostLikedByMe = isContentLikedByUser(post, userId);
            pres.setLikedByUser(isPostLikedByMe);

            /*
            set post owner info
             */
            pres.setFirstName(owner.getFirstName());
            pres.setLastName(owner.getLastName());
            pres.setUsername(owner.getUsername());

            List<ResourcesEntity> resources = new ArrayList<ResourcesEntity>();
            resources.addAll(owner.getRessources());

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
            if (post.getTargetUserProfileUuid() != 0) {

                UserEntity target = getUserByUuId(post.getTargetUserProfileUuid());

                try {
                    pres.setTargetUser(target.getFirstName(), target.getLastName(), target.getUsername(), target.getUuid(), userId.equals(target.getId()));

                } catch (Exception e) {
                    if (e instanceof NullPointerException) {
                        e.printStackTrace();
                        LOGGER.error("GET-POSTS: Target user UUID is incorrect! -- NOT FOUND");
                    } else {
                        e.printStackTrace();
                        LOGGER.error("GET-POSTS: Target user problem !!");
                    }
                }
            }

            //set visibility
            pres.setVisibility(post.getVisibility());

            //return all formated posts
            mContentResponse.add(pres);
        }

        return mContentResponse;

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