package com.fr.controllers;

import com.fr.controllers.service.PostControllerService;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;
import com.fr.entities.Users;
import com.fr.models.HeaderData;
import com.fr.models.PostResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param id
     * @param request
     * @return Like post
     */
    @PutMapping(value = "/post/{id}")
    public ResponseEntity<Void> likePost(@PathVariable("id") int id, HttpServletRequest request) {

        Post postToLike = postDataService.findPost(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = postDataService.getUserById(userId);

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

        LikeContent likeToSave = new LikeContent();
        likeToSave.setPost(postToLike);
        likeToSave.setUser(user);

        if (!postDataService.isPostAlreadyLikedByUser(id, userId)) {
            if (postDataService.likePost(likeToSave)) {
                // delete success
                LOGGER.info("LIKE_POST: Post with id:" + id + " has been liked by: " + user.getFirstName() + " "
                        + user.getLastName());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                LOGGER.error("LIKE_POST: Database like problem !!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.error("LIKE_POST: Post already liked by: " + user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param id
     * @param request
     * @return unlike post
     */
    @DeleteMapping(value = "/post/{id}")
    public ResponseEntity<Void> unLikePost(@PathVariable("id") int id, HttpServletRequest request) {

        Post postToUnlike = postDataService.findPost(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = postDataService.getUserById(userId);

        if (postToUnlike == null || user == null) {

            if (postToUnlike == null) {
                // post not fount
                LOGGER.info("UNLIKE_POST: Failed to retreive the post");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.error("UNLIKE_POST: Failed to retreive post to unlike");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        //post must be liked before unlike
        if (postDataService.isPostAlreadyLikedByUser(id, userId)) {

            if (postDataService.unLikePost(postToUnlike)) {
                // delete success
                LOGGER.info("UNLIKE_POST: Comment with id:" + id + " has been liked by: " + user.getFirstName() + " "
                        + user.getLastName());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                LOGGER.error("UNLIKE_POST: Database like problem !!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.error("UNLIKE_POST: Post NOT liked by: " + user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param id
     * @param page
     * @return list of people who liked the post
     */
    @GetMapping(value = "/post/{id}/{page}")
    public ResponseEntity<PostResponse> getPostLikers(@PathVariable("id") int id, @PathVariable("page") int page) {

        Post currentPost = postDataService.findPost(id);

        if (currentPost == null) {
            // post not fount
            LOGGER.info("LIKE_POST: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<HeaderData> likersList = postDataService.getLikersList(id, page);

        if (likersList.isEmpty()) {
            LOGGER.info("POST_LIKERS_LIST: Empty !!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PostResponse pr = new PostResponse();
        pr.setPostLikers(likersList);
        pr.setLikeCount(currentPost.getLikes().size());

        return new ResponseEntity<>(pr, HttpStatus.OK);
    }


    /**
     * Like comment
     */

}
