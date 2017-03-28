package com.fr.api.post;

import com.fr.aop.TraceAuthentification;
import com.fr.commons.dto.post.PostResponseDTO;
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

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@RestController
@RequestMapping("/post")
class PostController {

    private PostControllerService postDataService;

    @Autowired
    void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }


    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    /**
     * @param postId         post postId.
     * @param authentication auth object.
     * @return all post details.
     */
    @GetMapping(value = "/{postId}")
    ResponseEntity<PostResponseDTO> detailsPost(@PathVariable int postId, Authentication authentication) {

        Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();


        PostResponseDTO prep = postDataService.fillPostToSend(postId, userId);
        LOGGER.info("DETAILS_POST: PostEntity details has been returned for postId: " + postId);
        return new ResponseEntity<>(prep, HttpStatus.OK);

    }

    @GetMapping(value = "/all/{user_unique_id}/{page}")
    ResponseEntity<List<PostResponseDTO>> getAllPosts(@PathVariable int user_unique_id, @PathVariable int page, HttpServletRequest request, Authentication authentication) {

        //if user_unique_id is the connected user
        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
        Long connectedUserId = accountUserDetails.getId();

        List<PostEntity> posts;

        UserEntity requestUser = postDataService.getUserByUuId(user_unique_id);

        if (requestUser == null) {
            LOGGER.error("GET-ALL-POSTS: UserDTO postId is invalid");
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
            List visibility = Collections.singletonList(0);
            posts = postDataService.findAllPosts(requestUser.getId(), user_unique_id, visibility, page);
        }

        //if user_unique_id is not the connected user

        if (posts == null || posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<PostResponseDTO> postResponseDTOs = new ArrayList<>();
        posts.forEach(t -> postResponseDTOs.add(postDataService.fillPostToSend(t.getUuid(), connectedUserId)));

//        PostResponseDTO prep = postDataService.fillPostToSend(postResponseDTOs, userId);
        LOGGER.info("ALL_POST: All post have been returned");
        return new ResponseEntity<>(postResponseDTOs, HttpStatus.OK);

    }

    /**
     * @param postId  post postId.
     * @param newData data to update.
     * @return Update post data.
     */
    @PutMapping(value = "/{postId}")
    ResponseEntity<ContentEditedResponseDTO> updatePost(@PathVariable("postId") int postId,
                                                        @RequestBody ContentEditedResponseDTO newData) {

        PostEntity postToEdit = postDataService.findPost(postId);

        List<EditHistoryEntity> lastPostEditList = postDataService.getLastModification(postId);
        EditHistoryEntity lastPostEdit = null;
        boolean isAlreadyEdited = !lastPostEditList.isEmpty();

        EditHistoryEntity postEditRow = new EditHistoryEntity();
        SortedSet<AddressEntity> postEditAddress = null;

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
             related SportDTO can be modified
             */
            if (isAlreadyEdited) {
                postEditRow.setSport(lastPostEdit.getSport());
            } else {
                postEditRow.setSport(postToEdit.getSport());
            }

        } else if (newData.getSportId() != null) {
            // SportDTO modification
            SportEntity sp = postDataService.getSportById(newData.getSportId());
            if (sp != null) {
                postEditRow.setSport(sp);

                if (isAlreadyEdited) {
                    postEditRow.setText(lastPostEdit.getText());
                }

            } else {
                LOGGER.info("POST_UPDATE: Failed to retrieve the SportDTO to update");
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
     * @param postId post id.
     * @param page   page number.
     * @return List of post history edition.
     */
    @GetMapping(value = "/history/{postId}/{page}")
    ResponseEntity<List<ContentEditedResponseDTO>> getPostHistory(@PathVariable int postId, @PathVariable int page) {

        List<ContentEditedResponseDTO> contentEditedResponseDTOs = postDataService.getAllPostHistory(postId, page);

        LOGGER.info("POST_EDIT_HISTORY: PostEntity HISTORY has been returned for postId: " + postId);
        return new ResponseEntity<>(contentEditedResponseDTOs, HttpStatus.OK);

    }

    /**
     * @param id         post id.
     * @param visibility post visibility.
     * @return 202 status if visibility has been edited.
     */
    @PutMapping(value = "/{postId}/{visibility}")
    ResponseEntity<Void> editVisibility(@PathVariable int id, @PathVariable int visibility) {

        try {
            postDataService.editPostVisibility(id, visibility);
        } catch (EntityNotFoundException e) {
            LOGGER.info("POST_EDIT_VISIBILITY: Can't update visibility!!", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("POST_EDIT_VISIBILITY: PostEntity VISIBILITY has been changed for postId: " + id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }
}
