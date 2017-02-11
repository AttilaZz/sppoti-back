/**
 *
 */
package com.fr.core;

import com.fr.rest.service.PostControllerService;
import com.fr.entities.*;
import com.fr.commons.dto.CommentModel;
import com.fr.commons.dto.ContentEditedResponse;
import com.fr.commons.dto.PostResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Component
public class PostControllerServiceImpl extends AbstractControllerServiceImpl implements PostControllerService {

    @Value("${key.postsPerPage}")
    private int post_size;

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);

    }

    private static Logger LOGGER = Logger.getLogger(PostControllerServiceImpl.class);

    /*
     * The post update method is used to update the location (address) of the
     * post and the content. This two jobs are separated, not at the same time.
     *
     * That's why we check if the address is not null before persisting the new
     * content
     */
    @Override
    public boolean updatePost(EditHistory postEditRow, SortedSet<Address> postEditAddress, int postId) {
        if (postEditAddress != null) {

            try {
                List<Post> post = postRepository.getByUuid(postId);
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

    @Override
    public boolean deletePost(Post p) {

        try {
            p.setDeleted(true);
            postRepository.save(p);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Post findPost(int id) {

        List<Post> posts = postRepository.getByUuid(id);

        if (posts == null || posts.isEmpty()) {
            return null;
        } else {
            return posts.get(0);
        }

    }

    @Override
    public Sport getSportToUse(Long id) {

        return sportRepository.getById(id);
    }

    @Override
    public Sppoti getGameById(Long id) {

        return sppotiRepository.getOne(id);
    }

    @Override
    public List<PostResponse> getPhotoGallery(Long userId, int page) {
        return fillPostResponseFromDbPost(page, userId, 1, null);

    }

    @Override
    public List<PostResponse> getVideoGallery(Long userId, int page) {
        return fillPostResponseFromDbPost(page, userId, 2, null);
    }

    @Override
    public PostResponse fillPostToSend(Post post, Long userId) {

        return fillPostResponseFromDbPost(0, userId, 3, post).get(0);

    }

    private List<PostResponse> fillPostResponseFromDbPost(int page, Long userId, int operationType, Post post_param) {

        Pageable pageable = new PageRequest(page, post_size);

        List<Post> dbContent = new ArrayList<Post>();

        switch (operationType) {
            case 1:

                dbContent = postRepository.getByAlbumIsNotNullOrderByDatetimeCreatedDesc(pageable);

                break;
            case 2:

                dbContent = postRepository.getByVideoIsNotNullOrderByDatetimeCreatedDesc(pageable);

                break;
            case 3:

                dbContent.add(post_param);

                break;
            default:
                break;
        }

        List<PostResponse> mContentResponse = new ArrayList<PostResponse>();

        for (Post post : dbContent) {

            PostResponse pres = new PostResponse();

            Users owner = post.getUser();

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
            List<EditHistory> editHistory = editHistoryRepository.getByPostUuidOrderByDatetimeEditedDesc(post.getUuid());

            if (!editHistory.isEmpty()) {

                // modification detected
                pres.setEdited(true);

                EditHistory ec = editHistory.get(0);

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
            Manage comments count + last comment
             */
            Set<Comment> comments = post.getComments();
            pres.setCommentsCount(comments.size());

            try {
                List<Comment> commentsListTemp = new ArrayList<Comment>();
                commentsListTemp.addAll(comments);

                List<CommentModel> commentList = new ArrayList<CommentModel>();
                if (!commentsListTemp.isEmpty()) {
                    Comment comment = commentsListTemp.get(comments.size() - 1);

                    CommentModel commentModel = new CommentModel(comment, getUserCoverAndAvatar(comment.getUser()));
                    commentModel.setMyComment(comment.getUser().getId().equals(userId));
                    commentModel.setLikedByUser(isContentLikedByUser(comment, userId));
                    commentModel.setLikeCount(comment.getLikes().size());

                    commentList.add(commentModel);
                }

                pres.setComment(commentList);

            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("Error  asting set<Comment> to List<Comment>");
            }

            /*
            End managing comments
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

            List<Resources> resources = new ArrayList<Resources>();
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

                Users target = getUserByUuId(post.getTargetUserProfileUuid());

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

    @Override
    public List<ContentEditedResponse> getAllPostHistory(int id, int page) {

        Pageable pageable = new PageRequest(page, post_size);

        List<EditHistory> postHistory = editHistoryRepository.getByPostUuidOrderByDatetimeEditedDesc(id, pageable);

        return fillEditContentResponse(postHistory);
    }


    @Override
    public List<EditHistory> getLastModification(int postId) {
//        return editContentDaoService.getLastEditedPost(postId);
        return editHistoryRepository.getByPostUuidOrderByDatetimeEditedDesc(postId);
    }

    @Override
    public Sport getSportById(Long sport_id) {
        return sportRepository.getOne(sport_id);
    }

    @Override
    public boolean editPostVisibility(int id, int visibility) {

        List<Post> posts = postRepository.getByUuidOrderByDatetimeCreatedDesc(id, null);

        if (posts == null) return false;

        Post post = posts.get(0);
        post.setVisibility(visibility);

        try {
            postRepository.save(post);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addNotification(Long userId, int postId, String content) {

        Users connectedUser = userRepository.getByIdAndDeletedFalse(userId);
        Post concernedePostTag;


        List<Post> posts = postRepository.getByUuid(postId);

        if (posts == null) {
            throw new IllegalArgumentException("Incorrect post id passed in parameters");
        } else {
            concernedePostTag = posts.get(0);
        }


        /**
         * All words starting with DOLLAR, followed by Letter or accented Letter
         * and finishing with Letter, Number or Accented letter
         */
        String patternString1 = "(\\@+)([a-z|A-Z|\\p{javaLetter}][a-z\\d|A-Z\\d|\\p{javaLetter}]*)";

        Pattern pattern = Pattern.compile(patternString1);
        Matcher matcher = pattern.matcher(content);

        // clean tags from dollar
        List<String> tags = new ArrayList<String>();
        while (matcher.find()) {
            System.out.println(matcher.group());
            String s = matcher.group().trim();
            s = s.replaceAll("[@]", "");
            tags.add(s);
        }

		/*
         * process each tag
		 */

        for (String username : tags) {

            Users userToNotify;

            userToNotify = userRepository.getByUsername(username);

            if (userToNotify != null) {

                //TODO: ADD NOTIFICATION - TAG

            }
        }

    }

    @Override
    public List<Post> findAllPosts(Long userLongId, int userIntId, List visibility, int page) {

        Pageable pageable = new PageRequest(page, post_size, Sort.Direction.DESC, "datetimeCreated");

        return postRepository.getAllPosts(userIntId, userLongId, visibility, pageable);
    }

    @Override
    public boolean isTargetUserFriendOfMe(int connected_user_uuid, int friend_id) {
        return friendShipRepository.findByFriendUuidAndUserAndDeletedFalse(friend_id, connected_user_uuid) != null;
    }
}