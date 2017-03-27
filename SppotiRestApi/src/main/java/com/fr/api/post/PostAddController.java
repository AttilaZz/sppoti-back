package com.fr.api.post;

import com.fr.aop.TraceAuthentification;
import com.fr.commons.dto.post.PostRequestDTO;
import com.fr.commons.dto.post.PostResponseDTO;
import com.fr.entities.*;
import com.fr.commons.exception.PostContentMissingException;
import com.fr.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by djenanewail on 2/13/17.
 */

@RestController
@RequestMapping("/post")
public class PostAddController {

    private PostControllerService postDataService;

    @Autowired
    public void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }


    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    private static final String ATT_USER_ID = "USER_ID";


    /**
     * @param newPostReq post data to save.
     * @param request    http request object.
     * @return Add post by user.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<PostResponseDTO> addPost(@RequestBody PostRequestDTO newPostReq, HttpServletRequest request) {

        // get current logged user
        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity user = postDataService.getUserById(userId);
        LOGGER.debug("POST-ADD: LOGGED UserDTO: => " + userId);

        boolean canAdd = false;

        SportEntity targedSport;
        SppotiEntity game;
        Long sportId;
        Long gameId;

        PostEntity newPostToSave = new PostEntity(); // Object to save in database
        newPostToSave.setUser(user);

        PostResponseDTO postRep = new PostResponseDTO();// object to send on success

        // SportEntity is required
        if (newPostReq.getSportId() != null) {

            sportId = newPostReq.getSportId();
            targedSport = postDataService.getSportToUse(sportId);

            if (targedSport != null) {
                newPostToSave.setSport(targedSport);
                postRep.setSportId(sportId);
            } else {
                LOGGER.debug("POST-ADD: The received SportDTO ID is not valid");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {
            LOGGER.debug("POST-ADD: A sport_id must be defined ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if post is about a game
        if (newPostReq.getGameId() != null) {

            gameId = newPostReq.getGameId();
            game = postDataService.getSppotiById(gameId);

            if (game == null) {
                LOGGER.info("POST-ADD: Game id is not valid");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

//            newPostToSave.setSppoti(game);
//            postRep.setGame(game);
            canAdd = true;

        }

        // visibility
        int visibility = newPostReq.getVisibility();
        newPostToSave.setVisibility(visibility);
        postRep.setVisibility(visibility);

        /*
            ---- Manage address
         */

        //AddressEntity address = newPostReq.getAddress();

//        if (address != null) {
//            address.setPost(newPostToSave);
//            SortedSet<AddressEntity> addresses = new TreeSet<>();
//            addresses.add(address);
//
//            newPostToSave.setAddresses(addresses);
//        }

        /*
            ---- End address
         */
        String content;
        Set<String> image;
        String video;

        content = newPostReq.getContent().getContent();
        image = newPostReq.getContent().getImageLink();
        video = newPostReq.getContent().getVideoLink();

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

        List<ResourcesEntity> resources = new ArrayList<>();
        resources.addAll(user.getResources());

        if (!resources.isEmpty()) {
            if (resources.get(0) != null && resources.get(0).getType() == 1) {
                postRep.setAvatar(resources.get(0).getUrl());
            } else if (resources.get(1) != null && resources.get(1).getType() == 1) {
                postRep.setAvatar(resources.get(1).getUrl());
            }
        }

        /*
        Check target user
         */
        int requestTargetUserId = newPostReq.getTargetUserUuid();
        UserEntity targetUser = postDataService.getUserByUuId(requestTargetUserId);
        if (requestTargetUserId != 0 && targetUser != null) {

            newPostToSave.setTargetUserProfileUuid(newPostReq.getTargetUserUuid());
            postRep.setTargetUser(targetUser.getFirstName(), targetUser.getLastName(), targetUser.getUsername(), targetUser.getUuid(), false);
        } else if (requestTargetUserId != 0) {
            LOGGER.error("ADD-POST: Target user id not found !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if a game or one of the previous classic content has been assigned -
        // save post

        try {
            if (!canAdd) throw new PostContentMissingException("At least a game or a post content must be assigned");
            //Save and get the inserted id

            int insertedPostId = postDataService.savePost(newPostToSave).getUuid();

            //Fill the id in the response object
            postRep.setId(insertedPostId);
            postRep.setDatetimeCreated(newPostToSave.getDatetimeCreated());
            LOGGER.info("POST-ADD: post has been saved");

            postRep.setMyPost(true);
            LOGGER.info("UUID: " + postRep.getId());
            return new ResponseEntity<>(postRep, HttpStatus.CREATED);

        } catch (PostContentMissingException e1) {
            LOGGER.error("POST-ADD: At least a game or a post content must be assigned", e1);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }

}
