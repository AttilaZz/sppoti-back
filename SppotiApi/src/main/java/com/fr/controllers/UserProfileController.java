package com.fr.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.UserControllerService;
import com.fr.models.*;
import com.fr.entities.Users;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserControllerService usersDataService;

    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    private static final String ATT_USER_ID = "USER_ID";

    /*
     * Return role of connected user
     */
    @GetMapping(value = "/role")
    public ResponseEntity<List<String>> getRole(HttpServletRequest request) {

        LOGGER.info("Session ID -> " + RequestContextHolder.currentRequestAttributes().getSessionId());

        String confirmationCode = UUID.randomUUID().toString();
        LOGGER.info("sample of generated code: " + confirmationCode);

        List<String> userRole = usersDataService.getUserRole();

        if (!userRole.isEmpty()) {
            LOGGER.info("Role has been returned -> " + userRole.toString());

            return new ResponseEntity<>(userRole, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    /*
     * Return only header data - ANGULAR need - Request from @BACHIR
     */
    @GetMapping(value = "/header/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HeaderData> getHeaderData(HttpServletRequest request, @PathVariable("username") String username) {

        Long userId = null;
        // userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        // Users user = usersDataService.getUserById(userId);

        if (username == null) {
            LOGGER.info("HEADER DATA: username shouldn't be null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } else if (username.equals("sppoti")) {
            userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        } else {
            Users u = usersDataService.getUserByUsername(username);

            if (u != null) {
                userId = u.getId();

            } else {
                LOGGER.info("HEADER DATA: Username is invalid !!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        // Get unseen notifications
        //List<Notification> listNotif = usersDataService.getUnseenNotifications(userId, 0);

        HeaderData headerInfo = usersDataService.getHeaderData(userId);
        //headerInfo.setNotifList(listNotif);
        //headerInfo.setNotifListcount(listNotif.size());

        LOGGER.info("HEADER DATA: Header data has been returned :-)");
        return new ResponseEntity<>(headerInfo, HttpStatus.OK);

    }

    @GetMapping(value = "/topheader", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HeaderData> getTopHeaderData(HttpServletRequest request) {

        Long userId = null;
        userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = usersDataService.getUserById(userId);

        if (user == null) {
            LOGGER.info("HEADER TP DATA: User were not fount !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        HeaderData headerInfo = usersDataService.getTopHeaderData(userId);

        LOGGER.info("HEADER DATA: Header data has been returned :-)");
        return new ResponseEntity<HeaderData>(headerInfo, HttpStatus.OK);

    }

    /*
     * edit profile and cover pictures
     */
    @PostMapping(value = "/editAvatarOrCover/{type}/{previousId}/{extension}", headers = "content-type=application/x-www-form-urlencoded")
    // @RequestMapping(value =
    // "/editAvatarOrCover/{type}/{previousId}/{extension}", method =
    // RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces
    // = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> editProfilePicture(@ModelAttribute JsonPostRequest json,
                                                           UriComponentsBuilder ucBuilder, HttpServletRequest request, @PathVariable("type") int editType,
                                                           @PathVariable("extension") int extension, @PathVariable("previousId") Long oldResId) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        Gson gson = new Gson();
        PostRequest preq = null;

        if (json != null) {

            try {
                preq = gson.fromJson(json.getJson(), PostRequest.class);
                LOGGER.info("POST data sent by user: " + new ObjectMapper().writeValueAsString(preq));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else {
            LOGGER.info("POST: Data sent by user are invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (oldResId == 0) {
            LOGGER.info("PROFILE COVER-PICTURE: Arguments are not correct in the URL !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        /*
         * 1: edit avatar 2: edit cover
		 */
        if (editType == 1) {
            if (preq.getNewAvatar() != null) {
                if (usersDataService.editProfilePicture(preq.getNewAvatar(), oldResId, userId)) {
                    LOGGER.info("PROFILE PICTURE: Successfuly edited  :-)");
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                LOGGER.info("PROFILE PICTURE: Can't persist to database !");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else if (editType == 2) {
            if (preq.getCover() != null) {
                if (usersDataService.editCoverPicture(preq.getCover(), oldResId, extension, userId)) {
                    LOGGER.info("PROFILE COVER: Successfuly edited  :-)");
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                LOGGER.info("PROFILE COVER: Can't persist to database !");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            LOGGER.info("PROFILE COVER-PICTURE: No match for editType:" + editType);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("PROFILE COVER-PICTURE: Post arguments are not correct !");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
     * Get all data for any profile
     */
    @GetMapping(value = "/{username}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfilePage> displayAnyProfileData(@PathVariable("username") String username,
                                                             HttpServletRequest request, @PathVariable("page") int page) {

        Long loggedUserId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users loggedUser = usersDataService.getUserById(loggedUserId);

        Users targetUser = usersDataService.getUserByUsername(username);

        if (targetUser == null) {
            LOGGER.info("PROFILE VISIT-PROFILE: Username not valid !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long targetProfileId = targetUser.getId();
        List<PostResponse> userPost = usersDataService.getPostsFromLastPage(loggedUserId, targetProfileId, page);

        ProfilePage pView = new ProfilePage();
        pView.setUserPosts(userPost);

        pView.setUsername(targetUser.getUsername());
        pView.setUserSports(targetUser.getRelatedSports());
        pView.setUserPosts(userPost);
        pView.setBirthDate(targetUser.getDateBorn());
        pView.setUserAddresses(targetUser.getUserAddress());
        pView.setMyProfile(loggedUser.getUsername().equals(targetUser.getUsername()));

        return new ResponseEntity<>(pView, HttpStatus.OK);
    }

    /*
     * Look for a user from a letter
     */
    @GetMapping(value = "/find/users/{user}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HeaderData>> searchUser(@PathVariable("user") String userPrefix,
                                                       @PathVariable("page") int page, HttpServletRequest request) {

        if (userPrefix.isEmpty()) {
            LOGGER.info("PROFILE SEARCH-USER: Prefix not valid !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<HeaderData> foundUsers = usersDataService.getUserFromPrefix(userPrefix, page);

        LOGGER.info("PROFILE SEARCH-USER: Users has been returned !");
        return new ResponseEntity<>(foundUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/find/friends/{friend}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HeaderData>> searchFriend(@PathVariable("friend") String friendPrefix,
                                                         @PathVariable("page") int page, HttpServletRequest request) {

        Long loggedUserId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        if (friendPrefix.isEmpty()) {
            LOGGER.info("PROFILE SEARCH-FRIEND: Prefix not valid !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //List<HeaderData> foundUsers = usersDataService.getFriendFromPrefix(loggedUserId, friendPrefix, page);

        LOGGER.info("PROFILE SEARCH-USER: Users has been returned !");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
