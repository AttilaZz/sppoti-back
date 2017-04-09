package com.fr.api.post;

import com.fr.aop.TraceAuthentification;
import com.fr.commons.dto.post.PostDTO;
import com.fr.entities.*;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.security.AccountUserDetails;
import com.fr.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@RestController
@RequestMapping("/post")
class PostGetController {

    /**
     * Post controller service.
     */
    private PostControllerService postDataService;

    /**
     * Init services.
     */
    @Autowired
    void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }

    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    /**
     * Get post details.
     */
    @GetMapping(value = "/{postId}")
    ResponseEntity<PostDTO> detailsPost(@PathVariable int postId, Authentication authentication) {

        Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();

        PostDTO prep = postDataService.fillPostToSend(postId, userId);
        LOGGER.info("DETAILS_POST: PostEntity details has been returned for postId: " + postId);
        return new ResponseEntity<>(prep, HttpStatus.OK);

    }

    /**
     * Get al posts for a given user id.
     */
    @GetMapping(value = "/all/{userUniqueId}/{page}")
    ResponseEntity<List<PostDTO>> getAllPosts(@PathVariable("userUniqueId") int targetUserPost, @PathVariable int page, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        LOGGER.info("ALL_POST: All post have been returned");
        return new ResponseEntity<>(postDataService.getAllUserPosts(accountUserDetails.getId(), accountUserDetails.getUuid(), targetUserPost, page), HttpStatus.OK);

    }

    /**
     * List of all edition on a post.
     */
    @GetMapping(value = "/history/{postId}/{page}")
    ResponseEntity<List<ContentEditedResponseDTO>> getPostHistory(@PathVariable int postId, @PathVariable int page) {

        List<ContentEditedResponseDTO> contentEditedResponseDTOs = postDataService.getAllPostHistory(postId, page);

        LOGGER.info("POST_EDIT_HISTORY: PostEntity HISTORY has been returned for postId: " + postId);
        return new ResponseEntity<>(contentEditedResponseDTOs, HttpStatus.OK);

    }

    /**
     * Get all friend posts
     */
    @GetMapping("/all/friend/{userId}/{page}")
    ResponseEntity<List<PostDTO>> getAllFriendPosts(@PathVariable int userId, @PathVariable int page, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        return new ResponseEntity<>(postDataService.getAllFriendPosts(userId, page, accountUserDetails.getId()
        ), HttpStatus.OK);
    }
}
