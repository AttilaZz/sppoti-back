package com.fr.api.post.like;

import com.fr.entities.LikeContentEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
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
public class LikePostController {

    private PostControllerService postDataService;
    private LikeControllerService likeControllerService;

    @Autowired
    public void setLikeControllerService(LikeControllerService likeControllerService) {
        this.likeControllerService = likeControllerService;
    }

    @Autowired
    public void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }

    private Logger LOGGER = Logger.getLogger(LikePostController.class);

    private static final String ATT_USER_ID = "USER_ID";

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



}
