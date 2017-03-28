package com.fr.api.post.like;

import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
import com.fr.service.LikeControllerService;
import com.fr.service.PostControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
class UnlikePostController {

    private PostControllerService postDataService;
    private LikeControllerService likeControllerService;

    @Autowired
    void setLikeControllerService(LikeControllerService likeControllerService) {
        this.likeControllerService = likeControllerService;
    }

    @Autowired
    void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }

    private Logger LOGGER = Logger.getLogger(UnlikePostController.class);

    private static final String ATT_USER_ID = "USER_ID";

    /**
     * @param id      post id.
     * @param request
     * @return unlike post
     */
    @DeleteMapping(value = "/post/{id}")
    ResponseEntity<Void> unLikePost(@PathVariable("id") int id, HttpServletRequest request, Authentication authentication) {

        PostEntity postToUnlike = postDataService.findPost(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
        UserEntity user = accountUserDetails.getConnectedUserDetails();

        if (postToUnlike == null) {
            // post not fount
            LOGGER.error("UNLIKE_POST: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

}
