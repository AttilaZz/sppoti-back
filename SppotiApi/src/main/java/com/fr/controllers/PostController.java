/**
 *
 */
package com.fr.controllers;

import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.PostControllerService;
import com.fr.entities.*;
import com.fr.exceptions.PostContentMissingException;
import com.fr.models.ContentEditedResponse;
import com.fr.models.PostRequest;
import com.fr.models.PostResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<PostResponse> detailsPost(@PathVariable("id") int id, HttpServletRequest request) {

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
        LOGGER.info("DETAILS_POST: Post details has been returned for postId:" + id);
        return new ResponseEntity<>(prep, HttpStatus.OK);

    }

    @GetMapping(value = "/all/{user_unique_id}")
    public ResponseEntity<Object> getAllPosts(@PathVariable int user_unique_id, HttpServletRequest request) {

        List<Post> posts = postDataService.finAllPosts(user_unique_id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        List<PostResponse> postResponses = new ArrayList<>();


        for (Post post : posts) {
//            PostResponse postResponse = new PostResponse(post);
            postResponses.add(postDataService.fillPostToSend(post, userId));
        }

//        PostResponse prep = postDataService.fillPostToSend(postResponses, userId);
        LOGGER.info("all_POST: All post have been returned");
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
                LOGGER.info("POST-ADD: The received sport ID is not valid");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {

            LOGGER.info("POST-ADD: A sport must be defined ");
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
                    LOGGER.info("POST-ADD: imageLink value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                postRep.setImageLink(image);
                newPostToSave.setAlbum(image);
            }

            if (video != null) {
                if (video.trim().length() <= 0) {
                    LOGGER.info("POST-ADD: videoLink value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                postRep.setVideoLink(video);
                newPostToSave.setVideoLink(video);
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


            newPostToSave.setUuid(UUID.randomUUID().hashCode());
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
        Address postEditAddress = null;

        // Required attributes
        if (postToEdit == null) {
            LOGGER.info("POST_UPDATE: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        postEditRow.setPost(postToEdit);

        // if post has been edited before, get the latest entry
        if (isAlreadyEdited) {
            lastPostEdit = new EditHistory();
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
            if (isAlreadyEdited) {
                postEditRow.setSport(lastPostEdit.getSport());
            } else {
                postEditRow.setSport(postToEdit.getSport());
            }

        } else if (newData.getLatitude() != 0.0 && newData.getLongitude() != 0.0) {
            // get old address -> update coordinate -> save as edited content
            // TODO: Location edit should be as same as text and sport
            postEditAddress = new Address();
            postEditAddress.setLatitude(newData.getLatitude());
            postEditAddress.setLongitude(newData.getLongitude());
            postEditAddress.setPost(postToEdit);

        } else if (newData.getSportId() != null) {
            // sport modification
            Sport sp = postDataService.getSportById(newData.getSportId());
            if (sp != null) {
                postEditRow.setSport(sp);
                if (isAlreadyEdited) {
                    postEditRow.setText(lastPostEdit.getText());
                }
            } else {
                LOGGER.info("POST_UPDATE: Failed to retreive the sport to update");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.info("POST_UPDATE: No much found for the arguments sent");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if all arguments are correctly assigned - edit post
        if (postDataService.updatePost(postEditRow, postEditAddress))

        {
            LOGGER.info("POST_UPDATE: success");

            ContentEditedResponse edit = new ContentEditedResponse();
            edit.setId(postToEdit.getId());
            edit.setDateTime(postEditRow.getDatetimeEdited());
            edit.setText(postEditRow.getText());

            edit.setLatitude(newData.getLatitude());
            edit.setLongitude(newData.getLongitude());

            return new ResponseEntity<>(edit, HttpStatus.ACCEPTED);
        } else

        {
            LOGGER.info("POST_UPDATE: Failed when trying to update the post in DB");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    /**
     * @param id
     * @return Delete post
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") int id) {
        Post postToDelete = postDataService.findPost(id);

        if (postToDelete == null) {
            // post not fount
            LOGGER.info("POST_DELETE: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

        if (postDataService.deletePost(postToDelete)) {
            // delete success
            LOGGER.info("POST_DELETE: Post with id:" + id + " has been deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // database problem
        LOGGER.error("POST_DELETE: can't delete post!!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    /**
     * @param buttomMarker
     * @param request
     * @return List of all photos posted by a usert
     */
    @GetMapping(value = "/gallery/photo/{page}")
    public ResponseEntity<List<PostResponse>> getGalleryPhoto(@PathVariable("page") int buttomMarker,
                                                              HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = postDataService.getUserById(userId);

        List<PostResponse> lpost = postDataService.getPhotoGallery(userId, buttomMarker);

        if (lpost.size() == 0) {
            LOGGER.info("PHOTO_GALLERY: Empty !!");
            return new ResponseEntity<>(lpost, HttpStatus.NO_CONTENT);
        }

        LOGGER.info("PHOTO_GALLERY: has been returned to user:  " + user.getFirstName() + " " + user.getLastName());
        return new ResponseEntity<>(lpost, HttpStatus.OK);

    }

    @GetMapping(value = "/gallery/video/{page}")
    public ResponseEntity<List<PostResponse>> getGalleryVideo(@PathVariable("page") int buttomMarker,
                                                              HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = postDataService.getUserById(userId);

        List<PostResponse> lpost = postDataService.getVideoGallery(userId, buttomMarker);

        if (lpost.size() == 0) {
            LOGGER.info("VIDEO_GALLERY: Empty !!");
            return new ResponseEntity<>(lpost, HttpStatus.NO_CONTENT);
        }

        LOGGER.info("VIDEO_GALLERY: has been returned to user:  " + user.getFirstName() + " " + user.getLastName());
        return new ResponseEntity<>(lpost, HttpStatus.OK);

    }

    /**
     * @param id
     * @param page
     * @param request
     * @return List of post history edition
     */
    @GetMapping(value = "/history/{id}/{page}")
    public ResponseEntity<List<ContentEditedResponse>> editHistory(@PathVariable("id") int id,
                                                                   @PathVariable("page") int page, HttpServletRequest request) {

        Post postToLike = postDataService.findPost(id);

        if (postToLike == null) {

            // post not fount
            LOGGER.info("POST_EDIT_HISTORY: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        LOGGER.info("POST_EDIT_HISTORY: Post HISTORY has been returned for postId:" + id);
        return new ResponseEntity<>(postDataService.getAllPostHistory(id, page), HttpStatus.OK);

    }

    @PutMapping(value = "/posthistory/{id}/{visibility}")
    public ResponseEntity<Void> editVisibility(@PathVariable("id") int id, @PathVariable("visibility") int visibility,
                                               HttpServletRequest request) {

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

        LOGGER.info("POST_EDIT_VISIBILITY: Post VISIBILITY has been changed for postId:" + id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
