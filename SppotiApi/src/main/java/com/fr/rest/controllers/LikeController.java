package com.fr.rest.controllers;

import com.fr.commons.dto.HeaderDataDTO;
import com.fr.commons.dto.PostResponseDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
import com.fr.rest.service.CommentControllerService;
import com.fr.rest.service.LikeControllerService;
import com.fr.rest.service.PostControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */

@RestController
@RequestMapping("/like")
public class LikeController {

    private PostControllerService postDataService;
    private CommentControllerService commentControllerService;
    private LikeControllerService likeControllerService;

    @Autowired
    public void setLikeControllerService(LikeControllerService likeControllerService) {
        this.likeControllerService = likeControllerService;
    }

    @Autowired
    public void setCommentControllerService(CommentControllerService commentControllerService) {
        this.commentControllerService = commentControllerService;
    }

    @Autowired
    public void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }

    private Logger LOGGER = Logger.getLogger(LikeController.class);

    private static final String ATT_USER_ID = "USER_ID";

    /*$
     *  ---- Like post
     */

    /**
     * @param id post id.
     * @param request
     * @return Like post
     */
    @PutMapping(value = "/post/{id}")
    public ResponseEntity<Void> likePost(@PathVariable("id") int id, HttpServletRequest request) {

        PostEntity postToLike = postDataService.findPost(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        UserEntity user = likeControllerService.getUserById(userId);

        if (postToLike == null || user == null) {

            if (postToLike == null) {
                // post not found
                LOGGER.info("LIKE_POST: Failed to retreive the post");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.info("LIKE_POST: trying to like a non existing post");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        LikeContentEntity likeToSave = new LikeContentEntity();
        likeToSave.setPost(postToLike);
        likeToSave.setUser(user);

        if (!likeControllerService.isPostAlreadyLikedByUser(id, userId)) {
            try {
                likeControllerService.likePost(likeToSave);

                LOGGER.info("LIKE_POST: PostEntity with id:" + id + " has been liked by: " + user.getFirstName() + " "
                        + user.getLastName());
                return new ResponseEntity<>(HttpStatus.OK);

            } catch (Exception e) {
                LOGGER.error("LIKE_POST: Database like problem !!", e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {
            LOGGER.error("LIKE_POST: PostEntity already liked by: " + user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param id post id.
     * @param request
     * @return unlike post
     */
    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity<Void> unLikePost(@PathVariable("id") int id, HttpServletRequest request, Authentication authentication) {

        PostEntity postToUnlike = postDataService.findPost(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
        UserEntity user = accountUserDetails.getConnectedUserDetails();

        if (postToUnlike == null) {

            if (postToUnlike == null) {
                // post not fount
                LOGGER.info("UNLIKE_POST: Failed to retreive the post");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.error("UNLIKE_POST: Failed to retreive post id: to unlike");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        //post must be liked before unlike
        if (likeControllerService.isPostAlreadyLikedByUser(id, userId)) {

            try {
                likeControllerService.unLikePost(postToUnlike);

                LOGGER.info("UNLIKE_POST: CommentEntity with id:" + id + " has been liked by: " + user.getFirstName() + " "
                        + user.getLastName());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (RuntimeException e) {

                LOGGER.error("UNLIKE_POST: Problem founc when trying to unlike post id:" + id, e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {
            LOGGER.error("UNLIKE_POST: PostEntity NOT liked by: " + user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param id post id.
     * @param page page number.
     * @return list of people who liked the post.
     */
    @GetMapping(value = "/post/{id}/{page}")
    public ResponseEntity<PostResponseDTO> getPostLikers(@PathVariable("id") int id, @PathVariable("page") int page) {

        PostEntity currentPost = postDataService.findPost(id);

        if (currentPost == null) {
            // post not fount
            LOGGER.info("POST_LIKERS_LIST: Failed to retreive the post id" + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<HeaderDataDTO> likersList = likeControllerService.getPostLikersList(id, page);

        if (likersList.isEmpty()) {
            LOGGER.info("POST_LIKERS_LIST: No likers found fot the post id:" + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PostResponseDTO pr = new PostResponseDTO();
        pr.setLikers(likersList);
        pr.setLikeCount(currentPost.getLikes().size());

        return new ResponseEntity<>(pr, HttpStatus.OK);
    }


    /*
     * ---- Like comment
     */

    /**
     * @param id comment id.
     * @param request
     * @return status 200 if comment were liked or 404 if not
     */
    @PutMapping(value = "/comment/{id}")
    public ResponseEntity<Void> likeComment(@PathVariable("id") int id, HttpServletRequest request) {

        CommentEntity commentEntityToLike = commentControllerService.findComment(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity user = likeControllerService.getUserById(userId);

        if (commentEntityToLike == null || user == null) {

            if (commentEntityToLike == null) {
                // post not found
                LOGGER.info("LIKE_COMMENT: Failed to retreive the CommentEntity id" + id);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.info("LIKE_COMMENT: trying to like a non existing CommentEntity id" + id);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        LikeContentEntity likeToSave = new LikeContentEntity();
        likeToSave.setComment(commentEntityToLike);
        likeToSave.setUser(user);

        if (!likeControllerService.isCommentAlreadyLikedByUser(id, userId)) {
            try {
                likeControllerService.likeComment(likeToSave);
                LOGGER.info("LIKE_COMMENT: CommentEntity with id:" + id + " has been liked by: " + user.getFirstName() + " "
                        + user.getLastName());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (RuntimeException e) {

                LOGGER.error("LIKE_COMMENT: Failed to retreive the comment id:" + id, e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.error("LIKE_COMMENT: CommentEntity already liked by: " + user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param id post id.
     * @param request
     * @return 200 status if comment has been unliked or 404 if not.
     */
    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<Void> unLikeComment(@PathVariable("id") int id, HttpServletRequest request) {

        CommentEntity commentEntityToLike = commentControllerService.findComment(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity user = postDataService.getUserById(userId);

        if (commentEntityToLike == null || user == null) {

            if (commentEntityToLike == null) {
                // post not fount
                LOGGER.info("UNLIKE_COMMENT: Failed to retreive the comment id:" + id);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.error("UNLIKE_COMMENT: Failed to retreive comment id: " + id + " to unlike");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        //post must be liked before unlike
        if (likeControllerService.isCommentAlreadyLikedByUser(id, userId)) {
            try {
                likeControllerService.unLikeComment(commentEntityToLike);
                // delete success
                LOGGER.info("UNLIKE_COMMENT: CommentEntity with id:" + id + " has been liked by: " + user.getFirstName() + " "
                        + user.getLastName());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (RuntimeException e) {
                LOGGER.error("UNLIKE_COMMENT: Problem found when trying to unlike comment id" + id, e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {
            LOGGER.error("UNLIKE_COMMENT: CommentEntity id:" + id + "- NOT liked by: " + user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param id comment id.
     * @param page page number.
     * @return list of likers for a comment
     */
    @GetMapping(value = "/comment/{id}/{page}")
    public ResponseEntity<PostResponseDTO> getCommentLikers(@PathVariable("id") int id, @PathVariable("page") int page) {

        CommentEntity currentCommentEntity = commentControllerService.findComment(id);

        if (currentCommentEntity == null) {
            // post not fount
            LOGGER.info("COMMENT_LIKERS_LIST: Failed to retreive the comment id:" + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<HeaderDataDTO> likersList = likeControllerService.getCommentLikersList(id, page);

        if (likersList.isEmpty()) {
            LOGGER.info("COMMENT_LIKERS_LIST: No like for comment id:" + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PostResponseDTO pr = new PostResponseDTO();
        pr.setLikers(likersList);
        pr.setLikeCount(currentCommentEntity.getLikes().size());

        return new ResponseEntity<>(pr, HttpStatus.OK);
    }
}