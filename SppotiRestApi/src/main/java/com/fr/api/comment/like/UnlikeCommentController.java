package com.fr.api.comment.like;

import com.fr.entities.CommentEntity;
import com.fr.entities.UserEntity;
import com.fr.service.CommentControllerService;
import com.fr.service.LikeControllerService;
import com.fr.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
class UnlikeCommentController {

    private PostControllerService postDataService;
    private CommentControllerService commentControllerService;
    private LikeControllerService likeControllerService;

    @Autowired
    void setLikeControllerService(LikeControllerService likeControllerService) {
        this.likeControllerService = likeControllerService;
    }

    @Autowired
    void setCommentControllerService(CommentControllerService commentControllerService) {
        this.commentControllerService = commentControllerService;
    }

    @Autowired
    void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }

    private Logger LOGGER = Logger.getLogger(GetAllCommentLikesController.class);

    private static final String ATT_USER_ID = "USER_ID";


    /**
     * @param id      post id.
     * @param request
     * @return 200 status if comment has been unliked or 404 if not.
     */
    @DeleteMapping(value = "/comment/{id}")
    ResponseEntity<Void> unLikeComment(@PathVariable("id") int id, HttpServletRequest request) {

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


}
