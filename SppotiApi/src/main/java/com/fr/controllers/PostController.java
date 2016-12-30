package com.fr.controllers;

import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.PostControllerService;
import com.fr.entities.*;
import com.fr.exceptions.PostContentMissingException;
import com.fr.models.ContentEditedResponse;
import com.fr.models.PostRequest;
import com.fr.models.PostResponse;
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
    public ResponseEntity<PostResponse> detailsPost(@PathVariable int id, HttpServletRequest request) {

        Post mPost = postDataService.findPost(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = postDataService.getUserById(userId);

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

        PostResponse prep = postDataService.fillPostToSend(mPost, userId);
        LOGGER.info("DETAILS_POST: Post details has been returned for postId: " + id);
        return new ResponseEntity<>(prep, HttpStatus.OK);

    }

    @GetMapping(value = "/all/{user_unique_id}/{page}")
    public ResponseEntity<Object> getAllPosts(@PathVariable int user_unique_id, @PathVariable int page, HttpServletRequest request, Authentication authentication) {

        //if user_unique_id is the connected user
        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
        Long connectedUserId = accountUserDetails.getId();

        List<Post> posts;

        Users requestUser = postDataService.getUserByUuId(user_unique_id);

        if (requestUser == null) {
            LOGGER.error("GET-ALL-POSTS: User id is invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (connectedUserId.equals(requestUser.getId())) {
            //get connected user posts - visibility: 0,1,2
            List visibility = Arrays.asList(0, 1, 2);
            posts = postDataService.findAllPosts(connectedUserId, user_unique_id, visibility, page);
        } else if (requestUser.getFriends().contains(requestUser.getId())) {
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
            LOGGER.error("GET-ALL-POSTS: No content found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        List<PostResponse> postResponses = new ArrayList<>();

        for (Post post : posts) {
//            PostResponse postResponse = new PostResponse(post);
            postResponses.add(postDataService.fillPostToSend(post, userId));
        }

//        PostResponse prep = postDataService.fillPostToSend(postResponses, userId);
        LOGGER.info("ALL_POST: All post have been returned");
        return new ResponseEntity<>(postResponses, HttpStatus.OK);

    }

    /**
     * @param newPostReq
     * @param request
     * @return Add post by user
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<PostResponse> addPost(@RequestBody PostRequest newPostReq, HttpServletRequest request) {

        // get current logged user
        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = postDataService.getUserById(userId);
        LOGGER.info("POST-ADD: LOGGED User: => " + userId);

        boolean canAdd = false;

        Sport targedSport;
        Sppoti game;
        Long sportId;
        Long gameId;

        Post newPostToSave = new Post(); // Object to save in database
        newPostToSave.setUser(user);

        PostResponse postRep = new PostResponse();// object to send on success

        // Sport is required
        if (newPostReq.getSportId() != null) {

            sportId = newPostReq.getSportId();
            targedSport = postDataService.getSportToUse(sportId);

            if (targedSport != null) {
                newPostToSave.setSport(targedSport);
                postRep.setSportId(sportId);
            } else {
                LOGGER.info("POST-ADD: The received SportModel ID is not valid");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {

            LOGGER.info("POST-ADD: A sport_id must be defined ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        // if post is about a game
        if (newPostReq.getGameId() != null) {

            gameId = newPostReq.getGameId();
            game = postDataService.getGameById(gameId);

            if (game == null) {
                LOGGER.info("POST-ADD: Game id is not valid");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            newPostToSave.setGame(game);
            postRep.setGame(game);
            canAdd = true;

        }

        // visibility
        int visibility = newPostReq.getVisibility();
        newPostToSave.setVisibility(visibility);
        postRep.setVisibility(visibility);

        /*
            ---- Manage address
         */
        Address address = newPostReq.getAddress();

        if (address != null) {
            address.setPost(newPostToSave);
            SortedSet<Address> addresses = new TreeSet<>();
            addresses.add(address);

            newPostToSave.setAddresses(addresses);
        }

        /*
            ---- End address
         */
        String content = null;
        Set<String> image = new HashSet<>();
        String video = null;

        try {
            content = newPostReq.getContent().getContent();
            image = newPostReq.getContent().getImageLink();
            video = newPostReq.getContent().getVideoLink();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ADD-POST: Data sent in json rrquest are incorrect");
        }

        // if post has only classic content - Text, Image, Video
        if (newPostReq.getContent() != null) {

            if (content == null && image == null && video == null) {
                LOGGER.info("POST-ADD: missing attributes");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (image != null && video != null) {
                LOGGER.info("POST-ADD: image OR video, make a choice !!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (content != null) {
                if (content.trim().length() <= 0) {
                    LOGGER.info("POST-ADD: Content value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                // base64 encode content
                // Charset.forName("UTF-8").encode(content);
                // Base64Utils.encode(content.getBytes())
                postRep.setContent(content);
                newPostToSave.setContent(content);

            }

            if (image != null) {
                if (image.size() <= 0) {
                    LOGGER.info("POST-ADD: image link value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                postRep.setImageLink(image);
                newPostToSave.setAlbum(image);
            }

            if (video != null) {
                if (video.trim().length() <= 0) {
                    LOGGER.info("POST-ADD: video link value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                postRep.setVideoLink(video);
                newPostToSave.setVideo(video);
            }

            canAdd = true;

        }

        postRep.setFirstName(user.getFirstName());
        postRep.setLastName(user.getLastName());
        postRep.setUsername(user.getUsername());

        // if a game or one of the previous classic content has been assigned -
        // save post

        try {

            if (!canAdd) throw new PostContentMissingException("At least a game or a post content must be assigned");
            //Save and get the inserted id

            newPostToSave.setTargetUserProfileUuid(newPostReq.getTargetUseruuid());

            int insertedPostId = postDataService.savePost(newPostToSave).getUuid();

            //Fill the id in the response object
            postRep.setId(insertedPostId);
            postRep.setDatetimeCreated(newPostToSave.getDatetimeCreated());
            LOGGER.info("POST-ADD: post has been saved");

            /**
             * prepare notifications
             */
            // check if logged user has been tagged
            if (content != null) {
                //postDataService.addNotification(userId, insertedPostId, content);
            }

            postRep.setMyPost(true);
            LOGGER.info("UUID: " + postRep.getId());
            return new ResponseEntity<>(postRep, HttpStatus.CREATED);

        } catch (PostContentMissingException e1) {
            e1.printStackTrace();
            LOGGER.error("POST-ADD: At least a game or a post content must be assigned");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("POST-ADD: Unhandled problem has been found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param postId
     * @param newData
     * @return Update post data
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ContentEditedResponse> updatePost(@PathVariable("id") int postId,
                                                            @RequestBody ContentEditedResponse newData) {

        Post postToEdit = postDataService.findPost(postId);

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
             related SportModel can be modified
             */
            if (isAlreadyEdited) {
                postEditRow.setSport(lastPostEdit.getSport());
            } else {
                postEditRow.setSport(postToEdit.getSport());
            }

        } else if (newData.getSportId() != null) {
            // SportModel modification
            Sport sp = postDataService.getSportById(newData.getSportId());
            if (sp != null) {
                postEditRow.setSport(sp);

                if (isAlreadyEdited) {
                    postEditRow.setText(lastPostEdit.getText());
                }

            } else {
                LOGGER.info("POST_UPDATE: Failed to retreive the SportModel to update");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.info("POST_UPDATE: No much found for the arguments sent");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if all arguments are correctly assigned - edit post
        if (postDataService.updatePost(postEditRow, postEditAddress, postId)) {
            LOGGER.info("POST_UPDATE: success");

            ContentEditedResponse edit = new ContentEditedResponse();
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
        Post postToDelete = postDataService.findPost(id);

        if (postToDelete == null) {
            // post not fount
            LOGGER.info("POST_DELETE: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

        if (postDataService.deletePost(postToDelete)) {
            // delete success
            LOGGER.info("POST_DELETE: Post with id: " + id + "-- has been deleted");
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
    public ResponseEntity<List<ContentEditedResponse>> editHistory(@PathVariable int id, @PathVariable int page) {

        Post postToLike = postDataService.findPost(id);

        if (postToLike == null) {

            // post not fount
            LOGGER.info("POST_EDIT_HISTORY: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        LOGGER.info("POST_EDIT_HISTORY: Post HISTORY has been returned for postId: " + id);
        return new ResponseEntity<>(postDataService.getAllPostHistory(id, page), HttpStatus.OK);

    }

    @PutMapping(value = "/{id}/{visibility}")
    public ResponseEntity<Void> editVisibility(@PathVariable int id, @PathVariable int visibility) {

        Post postToEdit = postDataService.findPost(id);

        if (postToEdit == null) {

            // post not fount
            LOGGER.info("POST_EDIT_VISIBILITY: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        if (!postDataService.editPostVisibility(id, visibility)) {
            LOGGER.info("POST_EDIT_VISIBILITY: Can't update visibility!!");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

        LOGGER.info("POST_EDIT_VISIBILITY: Post VISIBILITY has been changed for postId: " + id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
