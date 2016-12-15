/**
 *
 */
package com.fr.controllers.serviceImpl;

import com.fr.controllers.service.PostControllerService;
import com.fr.entities.*;
import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.models.PostResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Component
public class PostControllerServiceImpl extends AbstractControllerServiceImpl implements PostControllerService {

    @Value("${key.likesPerPage}")
    private int like_size;

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
    public boolean updatePost(EditHistory postEditRow, Address postEditAddress) {
        if (postEditAddress != null) {

            try {
                addressRepository.save(postEditAddress);
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
    public Post findPost(Long id) {
        if (postRepository.getById(id).isEmpty()) return null;
        return postRepository.getById(id).get(0);
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
    public boolean likePost(LikeContent likeToSave) {
        try {
            likeRepository.save(likeToSave);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean unLikePost(Post post) {
        try {
            LikeContent likeContent = likeRepository.getByPostId(post.getId());
            likeDaoService.delete(likeContent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean isPostAlreadyLikedByUser(Long postId, Long userId) {

        return likeRepository.getByUserIdAndPostId(userId, postId) != null;

    }

    @Override
    public List<PostResponse> getPhotoGallery(Long userId, int buttomMarker) {
        return fillPostResponseFromDbPost(buttomMarker, userId, 1, null);

    }

    @Override
    public List<PostResponse> getVideoGallery(Long userId, int buttomMarker) {
        return fillPostResponseFromDbPost(buttomMarker, userId, 2, null);
    }

    private List<PostResponse> fillPostResponseFromDbPost(int bottomMajId, Long userId, int operationType,
                                                          Post post_param) {
        List<Post> dbContent = new ArrayList<>();

        switch (operationType) {
            case 1:
                dbContent = postDaoService.getPhotoGalleryPostsFromLastMajId(userId, bottomMajId);

                break;
            case 2:
                dbContent = postDaoService.getVideoGalleryPostsFromLastMajId(userId, bottomMajId);

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
                pres.setId(post.getId());

            if (post.getContent() != null)
                pres.setContent(post.getContent());

            if (post.getDatetimeCreated() != null)
                pres.setDatetimeCreated(post.getDatetimeCreated());


            pres.setMyPost(userId.equals(post.getUser().getId()));


            if (post.getAlbum() != null)
                pres.setImageLink(post.getAlbum());

            if (post.getVideoLink() != null)
                pres.setVideoLink(post.getVideoLink());

            if (post.getGame() != null)
                pres.setGame(post.getGame());

            if (post.getSport() != null && post.getSport().getId() != null) {
                pres.setSportId(post.getSport().getId());
            }

            // check if content has been modified or not
            List<EditHistory> editHistory = editHistoryRepository.getByPostIdOrderByIdDesc(post.getId());

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

            // Access here if details message is requested - otherwise no need
            // to show comments
            if (operationType == 3) {
                List<Comment> commentsList = new ArrayList<>();
                if (post.getComments() != null) {
                    commentsList.clear();
                    //commentsList = commentDaoService.getCommentsFromLastMajId(post_param.getId(), 0);
                    pres.setPostComments(fillCommentModelList(commentsList, userId));
                }
            }

            int nbLike = post.getLikes().size();
            if (post.getLikes() != null) {
                pres.setLikeCount(nbLike);
            }

            boolean isPostLikedByMe = isContentLikedByUser(post, userId);
            pres.setLikedByUser(isPostLikedByMe);

            //pres.setCommentsCount(commentDaoService.getCommentCount(post.getId()));
            mContentResponse.add(pres);
        }

        return mContentResponse;

    }

    @Override
    public PostResponse fillPostToSend(Post post, Long userId) {

        return fillPostResponseFromDbPost(0, userId, 3, post).get(0);

    }

    @Override
    public List<ContentEditedResponse> getAllPostHistory(Long id, int page) {
        List<EditHistory> dsHistoryList = editContentDaoService.getAllPostHistory(id, page);
        return fillEditContentResponse(dsHistoryList);
    }

    @Override
    public List<HeaderData> getLikersList(Long id, int page) {

        int debut = page * like_size;


        Pageable pageable1 = new PageRequest(page, like_size);

        List<LikeContent> likersData = likeRepository.getByPostIdOrderByDatetimeCreated(id, pageable1);

        List<HeaderData> likers = new ArrayList<>();

        if (!likersData.isEmpty()) {
            for (LikeContent row : likersData) {
                // get liker data
                HeaderData u = new HeaderData();
                u.setAvatar(userDaoService.getLastAvatar(row.getUser().getId()).get(0).getUrl());
                u.setFirstName(row.getUser().getFirstName());
                u.setLastName(row.getUser().getLastName());
                // u.setCover(userDao.getLastCover(row.getUser().getId(),
                // coverType));
                u.setUsername(row.getUser().getUsername());

                likers.add(u);
            }
        }

        return likers;
    }

    @Override
    public List<EditHistory> getLastModification(Long postId) {
//        return editContentDaoService.getLastEditedPost(postId);
        return editHistoryRepository.getByPostIdOrderByIdDesc(postId);
    }

    @Override
    public Sport getSportById(Long sport_id) {
        return sportDaoService.getEntityByID(sport_id);
    }

    @Override
    public boolean editPostVisibility(Long id, int visibility) {

        Post p = postDaoService.getEntityByID(id);
        p.setVisibility(visibility);

        return postDaoService.update(p);
    }

    @Override
    public boolean addNotification(Long userId, Long postId, String content) {

        Users connectedUser = userRepository.getById(userId);
        Post concernedePostTag = postRepository.getOne(postId);

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
    public List<Post> finAllPosts() {
        return postRepository.findAll();
    }

}
