package com.fr.api.comment.like;

import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.UserEntity;
import com.fr.service.CommentControllerService;
import com.fr.service.LikeControllerService;
import com.fr.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
public class LikeCommentController {

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

    private Logger LOGGER = Logger.getLogger(LikeCommentController.class);

    private static final String ATT_USER_ID = "USER_ID";


    /**
     * @param id      comment id.
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


}
