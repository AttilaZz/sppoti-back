package com.fr.rest.controllers.post;

import com.fr.aop.TraceAuthentification;
import com.fr.commons.dto.PostResponseDTO;
import com.fr.rest.service.PostControllerService;
import com.fr.entities.*;
import com.fr.exceptions.PostContentMissingException;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.PostRequestDTO;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@RestController
@RequestMapping("/post")
public class PostController {

    private PostControllerService postDataService;

    @Autowired
    public void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }


    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    private static final String ATT_USER_ID = "USER_ID";

    /**
     * @param id
     * @param request
     * @return all post details
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostResponseDTO> detailsPost(@PathVariable int id, HttpServletRequest request) {

        PostEntity mPost = postDataService.findPost(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity user = postDataService.getUserById(userId);

        if (mPost == null || user == null) {

            if (mPost == null) {
                // post not fount
                LOGGER.info("DETAILS_POST: Failed to retreive the post");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.info("DETAILS_POST: Failed to retreive user session");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        PostResponseDTO prep = postDataService.fillPostToSend(mPost, userId);
        LOGGER.info("DETAILS_POST: PostEntity details has been returned for postId: " + id);
        return new ResponseEntity<>(prep, HttpStatus.OK);

    }

    @GetMapping(value = "/all/{user_unique_id}/{page}")
    public ResponseEntity<Object> getAllPosts(@PathVariable int user_unique_id, @PathVariable int page, HttpServletRequest request, Authentication authentication) {

        //if user_unique_id is the connected user
        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
        Long connectedUserId = accountUserDetails.getId();

        List<PostEntity> posts;

        UserEntity requestUser = postDataService.getUserByUuId(user_unique_id);

        if (requestUser == null) {
            LOGGER.error("GET-ALL-POSTS: UserDTO id is invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (connectedUserId.equals(requestUser.getId())) {
            //get connected user posts - visibility: 0,1,2
            List visibility = Arrays.asList(0, 1, 2);
            posts = postDataService.findAllPosts(connectedUserId, user_unique_id, visibility, page);
        } else if (postDataService.isTargetUserFriendOfMe(accountUserDetails.getConnectedUserDetails().getUuid(), user_unique_id)) {
            //get friend posts - visibility: 0,1
            List visibility = Arrays.asList(0, 1);
            posts = postDataService.findAllPosts(requestUser.getId(), user_unique_id, visibility, page);
        } else {
            //get unknown user posts - visibility: 0
            List visibility = Arrays.asList(0);
            posts = postDataService.findAllPosts(requestUser.getId(), user_unique_id, visibility, page);
        }

        //if user_unique_id is not the connected user

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        if (posts != null && posts.isEmpty()) {
            LOGGER.warn("GET-ALL-POSTS: No content found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        List<PostResponseDTO> postResponseDTOs = new ArrayList<>();

        for (PostEntity post : posts) {
//            PostResponseDTO postResponse = new PostResponseDTO(post);
            postResponseDTOs.add(postDataService.fillPostToSend(post, userId));
        }

//        PostResponseDTO prep = postDataService.fillPostToSend(postResponseDTOs, userId);
        LOGGER.info("ALL_POST: All post have been returned");
        return new ResponseEntity<>(postResponseDTOs, HttpStatus.OK);

    }

    /**
     * @param postId
     * @param newData
     * @return Update post data
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ContentEditedResponseDTO> updatePost(@PathVariable("id") int postId,
                                                               @RequestBody ContentEditedResponseDTO newData) {

        PostEntity postToEdit = postDataService.findPost(postId);

        List<EditHistory> lastPostEditList = postDataService.getLastModification(postId);
        EditHistory lastPostEdit = null;
        boolean isAlreadyEdited = !lastPostEditList.isEmpty();

        EditHistory postEditRow = new EditHistory();
        SortedSet<Address> postEditAddress = null;

        // Required attributes
        if (postToEdit == null) {
            LOGGER.info("POST_UPDATE: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        postEditRow.setPost(postToEdit);

        // if post has been edited before, get the latest entry
        if (isAlreadyEdited) {
            lastPostEdit = lastPostEditList.get(0);
        }

        // test the received attributes content
        /*
         * If post content has been edited -> New row in EDITHISTORY is created
		 * Otherwise: we just add a new row in address table related to the
		 * targeted post
		 */
        if (newData.getText() != null && newData.getText().trim().length() > 0) {

            postEditRow.setText(newData.getText());

            /*
             related SportModelDTO can be modified
             */
            if (isAlreadyEdited) {
                postEditRow.setSport(lastPostEdit.getSport());
            } else {
                postEditRow.setSport(postToEdit.getSport());
            }

        } else if (newData.getSportId() != null) {
            // SportModelDTO modification
            Sport sp = postDataService.getSportById(newData.getSportId());
            if (sp != null) {
                postEditRow.setSport(sp);

                if (isAlreadyEdited) {
                    postEditRow.setText(lastPostEdit.getText());
                }

            } else {
                LOGGER.info("POST_UPDATE: Failed to retreive the SportModelDTO to update");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.info("POST_UPDATE: No much found for the arguments sent");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if all arguments are correctly assigned - edit post
        if (postDataService.updatePost(postEditRow, postEditAddress, postId)) {
            LOGGER.info("POST_UPDATE: success");

            ContentEditedResponseDTO edit = new ContentEditedResponseDTO();
            edit.setId(postToEdit.getId());
            edit.setDateTime(postEditRow.getDatetimeEdited());
            edit.setText(postEditRow.getText());

            edit.setLatitude(newData.getLatitude());
            edit.setLongitude(newData.getLongitude());

            return new ResponseEntity<>(edit, HttpStatus.ACCEPTED);
        } else {
            LOGGER.info("POST_UPDATE: Failed when trying to update the post in DB");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    /**
     * @param id
     * @return Delete post
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable int id) {
        PostEntity postToDelete = postDataService.findPost(id);

        if (postToDelete == null) {
            // post not fount
            LOGGER.info("POST_DELETE: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

        if (postDataService.deletePost(postToDelete)) {
            // delete success
            LOGGER.info("POST_DELETE: PostEntity with id: " + id + "-- has been deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // database problem
        LOGGER.error("POST_DELETE: can't delete post!!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * @param id
     * @param page
     * @return List of post history edition
     */
    @GetMapping(value = "/history/{id}/{page}")
    public ResponseEntity<List<ContentEditedResponseDTO>> editHistory(@PathVariable int id, @PathVariable int page) {

        PostEntity postToLike = postDataService.findPost(id);

        if (postToLike == null) {

            // post not fount
            LOGGER.info("POST_EDIT_HISTORY: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        LOGGER.info("POST_EDIT_HISTORY: PostEntity HISTORY has been returned for postId: " + id);
        return new ResponseEntity<>(postDataService.getAllPostHistory(id, page), HttpStatus.OK);

    }

    @PutMapping(value = "/{id}/{visibility}")
    public ResponseEntity<Void> editVisibility(@PathVariable int id, @PathVariable int visibility) {

        PostEntity postToEdit = postDataService.findPost(id);

        if (postToEdit == null) {

            // post not fount
            LOGGER.info("POST_EDIT_VISIBILITY: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        if (!postDataService.editPostVisibility(id, visibility)) {
            LOGGER.info("POST_EDIT_VISIBILITY: Can't update visibility!!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        LOGGER.info("POST_EDIT_VISIBILITY: PostEntity VISIBILITY has been changed for postId: " + id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
