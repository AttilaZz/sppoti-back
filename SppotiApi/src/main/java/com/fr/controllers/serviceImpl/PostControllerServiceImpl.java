/**
 *
 */
package com.fr.controllers.serviceImpl;

import com.fr.controllers.service.PostControllerService;
import com.fr.entities.*;
import com.fr.models.ContentEditedResponse;
import com.fr.models.PostResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
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
                Post post = postRepository.getByUuid(postId);
                post.setAddresses(postEditAddress);

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
        List<Post> posts = postRepository.getByUuid(id, null);

        if (posts.isEmpty()) return null;

        return posts.get(0);
    }

    @Override
    public Sport getSportToUse(Long id) {

        return sportRepository.getById(id);
    }

    @Override
    public Sppoti getGameById(Long id) {

        return sppotiDaoService.getEntityByID(id);
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

        int debut = page * post_size;

        Pageable pageable = new PageRequest(debut, post_size);

        List<Post> dbContent = new ArrayList<>();

        switch (operationType) {
            case 1:

                dbContent = postRepository.getByAlbumIsNotNull(pageable);

                break;
            case 2:

                dbContent = postRepository.getByVideoIsNotNull(pageable);

                break;
            case 3:

                dbContent.add(post_param);

                break;
            default:
                break;
        }

        List<PostResponse> mContentResponse = new ArrayList<>();

        for (Post post : dbContent) {

            PostResponse pres = new PostResponse();

            if (post.getId() != null)
                pres.setId(post.getUuid());

            if (post.getContent() != null)
                pres.setContent(post.getContent());

            if (post.getDatetimeCreated() != null)
                pres.setDatetimeCreated(post.getDatetimeCreated());


            pres.setMyPost(userId.equals(post.getUser().getId()));


            if (post.getAlbum() != null)
                pres.setImageLink(post.getAlbum());

            if (post.getVideo() != null)
                pres.setVideoLink(post.getVideo());

            if (post.getGame() != null)
                pres.setGame(post.getGame());

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

            //comments count
            pres.setCommentsCount(post.getComments().size());

            //like count
            pres.setLikeCount(post.getLikes().size());

            boolean isPostLikedByMe = isContentLikedByUser(post, userId);
            pres.setLikedByUser(isPostLikedByMe);

            mContentResponse.add(pres);
        }

        return mContentResponse;

    }

    @Override
    public List<ContentEditedResponse> getAllPostHistory(int id, int page) {


        int debut = page * post_size;

        Pageable pageable = new PageRequest(debut, post_size);

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
        return null;
    }

    @Override
    public boolean editPostVisibility(int id, int visibility) {

        List<Post> posts = postRepository.getByUuid(id, null);

        if (posts == null) return false;

        Post post = posts.get(0);
        post.setVisibility(visibility);

        try {
            postRepository.save(post);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addNotification(Long userId, int postId, String content) {

        Users connectedUser = userRepository.getById(userId);
        Post concernedePostTag = postRepository.getByUuid(postId);

        /**
         * All words starting with DOLLAR, followed by Letter or accented Letter
         * and finishing with Letter, Number or Accented letter
         */
        String patternString1 = "(\\@+)([a-z|A-Z|\\p{javaLetter}][a-z\\d|A-Z\\d|\\p{javaLetter}]*)";

        Pattern pattern = Pattern.compile(patternString1);
        Matcher matcher = pattern.matcher(content);

        // clean tags from dollar
        List<String> tags = new ArrayList<>();
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
            try {
                userToNotify = userRepository.getByUsername(username);
            } catch (Exception e) {
                LOGGER.info("POST-ADD: Username tag" + username + " is not valid !");
                return false;
            }
            if (userToNotify != null) {

                Notifications notif = new Notifications();
                notif.setTag(true);
                notif.setContentShared(false);
                notif.setViewed(false);
                notif.setNotifSender(connectedUser);
                notif.setNotifiedUserId(userToNotify.getId());
                notif.setPostTag(concernedePostTag);

                try {
                    notificationRepository.save(notif);
                } catch (Exception e) {
                    return false;
                }

                LOGGER.info(notif.toString());
            } else
                continue;
        }

        return true;
    }

    @Override
    public List<Post> findAllPosts(int uuid, int page) {

        int debut = page * post_size;

        Pageable pageable = new PageRequest(debut, post_size);

        return postRepository.getByUserUuid(uuid, pageable);
    }

}