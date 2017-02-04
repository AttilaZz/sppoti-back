package com.fr.rest.controllers;

import com.fr.rest.service.AccountControllerService;
import com.fr.rest.service.FriendControllerService;
import com.fr.entities.FriendShip;
import com.fr.entities.Users;
import com.fr.commons.dto.FriendResponse;
import com.fr.models.GlobalAppStatus;
import com.fr.commons.dto.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 12/26/16.
 */

@RestController
@RequestMapping("/friend")
public class FriendController {

    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(CommentController.class);

    private AccountControllerService accountControllerService;

    private FriendControllerService friendControllerService;

    @Autowired
    public void setFriendControllerService(FriendControllerService friendControllerService) {
        this.friendControllerService = friendControllerService;
    }

    @Autowired
    public void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }



    @Value("${key.friendShipPerPage}")
    private int friend_list_size;

    /**
     * @param userId
     * @param page
     * @param request
     * @return confirmed friend lis
     */
    @GetMapping("/confirmed/{userId}/{page}")
    public ResponseEntity<FriendResponse> getConfirmedFriendList(@PathVariable int userId, @PathVariable int page, HttpServletRequest request) {

        Long connected_user = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connectedUser = friendControllerService.getUserById(connected_user);

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShip> friendShips = friendControllerService.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatus.CONFIRMED.name(), pageable);

        List<User> friendList = new ArrayList<>();

        for (FriendShip friendShip : friendShips) {
            Users userdb = friendShip.getFriend();

            User user = accountControllerService.fillUserResponse(userdb, null);

            friendList.add(user);

        }

        /*
        Prepare response
         */
        FriendResponse friendResponse = new FriendResponse();
        friendResponse.setConfirmedList(friendList);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }

    /**
     * @param userId
     * @param page
     * @param request
     * @return
     */
    @GetMapping("/refused/{userId}/{page}")
    public ResponseEntity<FriendResponse> getRefusedFriendList(@PathVariable int userId, @PathVariable int page, HttpServletRequest request) {

        Long connected_user = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connectedUser = friendControllerService.getUserById(connected_user);


        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShip> friendShips = friendControllerService.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatus.REFUSED.name(), pageable);

        List<User> friendList = new ArrayList<>();

        for (FriendShip friendShip : friendShips) {
            Users userdb = friendShip.getFriend();

            User user = accountControllerService.fillUserResponse(userdb, null);

            friendList.add(user);

        }

        /*
        Prepare response
         */
        FriendResponse friendResponse = new FriendResponse();
        friendResponse.setConfirmedList(friendList);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }


    /**
     * @param page
     * @param request
     * @return
     */
    @GetMapping("/pending/sent/{page}")
    public ResponseEntity<FriendResponse> getSentPendingFriendList(@PathVariable int page, HttpServletRequest request) {

        Long connected_user = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connectedUser = friendControllerService.getUserById(connected_user);

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShip> friendShips = friendControllerService.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatus.PENDING.name(), pageable);

        if (friendShips.isEmpty()) {
            LOGGER.error("GET_PENDING_SENT: No sent friend request found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<User> friendList = new ArrayList<>();

        for (FriendShip friendShip : friendShips) {
            Users userdb = friendShip.getFriend();

            User user = accountControllerService.fillUserResponse(userdb, null);
            user.setDatetimeCreated(friendShip.getDatetime());

            friendList.add(user);

        }

        /*
        Prepare response
         */
        FriendResponse friendResponse = new FriendResponse();
        friendResponse.setPendingList(friendList);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }


    /**
     * @param page
     * @param request
     * @return
     */
    @GetMapping("/pending/received/{page}")
    public ResponseEntity<FriendResponse> getREceivedPendingFriendList(@PathVariable int page, HttpServletRequest request) {

        Long connected_user = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connectedUser = friendControllerService.getUserById(connected_user);

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShip> friendShips = friendControllerService.getByFriendUuidAndStatus(connectedUser.getUuid(), GlobalAppStatus.PENDING.name(), pageable);

        if (friendShips.isEmpty()) {
            LOGGER.error("GET_PENDING_RECEIVED: No received request friend found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<User> friendList = new ArrayList<>();

        for (FriendShip friendShip : friendShips) {
            Users userdb = friendControllerService.getUserByUuId(friendShip.getUser());

            User user = accountControllerService.fillUserResponse(userdb, null);
            user.setDatetimeCreated(friendShip.getDatetime());

            friendList.add(user);

        }

        /*
        Prepare response
         */
        FriendResponse friendResponse = new FriendResponse();
        friendResponse.setPendingList(friendList);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }

    /**
     * @param user
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<Object> addFriend(@RequestBody User user, HttpServletRequest request) {

        /*
        Chekck received data
         */
        if (user == null || user.getFriendUuid() == 0) {
            LOGGER.error("ADD-FRIEND: No data found in the body");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        Get connected user id
         */
        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        /*
        Prepare friendShip
         */
        Users connectedUser = friendControllerService.getUserById(userId);
        Users friend = friendControllerService.getUserByUuId(user.getFriendUuid());

        /*
        Check if the friend id refers to an existing user account
         */
        if (friend == null) {
            LOGGER.error("ADD-FRIEND: Friend id not found !!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        Friend with my self
         */
        if (connectedUser.equals(friend)) {
            LOGGER.error("ADD-FRIEND: You can't be friend of your self");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        Check if friendship exist
         */
        FriendShip tempFriendShip = friendControllerService.getByFriendUuidAndUser(friend.getUuid(), connectedUser.getUuid());
        if (tempFriendShip != null && !tempFriendShip.getStatus().equals(GlobalAppStatus.PUBLIC_RELATION.name())) {
            LOGGER.error("ADD-FRIEND: You are already friends");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        FriendShip friendShip = new FriendShip();

        //friendship aleady exist in a different status
        if(tempFriendShip != null && tempFriendShip.getStatus().equals(GlobalAppStatus.PUBLIC_RELATION.name())){
            friendShip = tempFriendShip;
            friendShip.setStatus(GlobalAppStatus.PENDING.name());
        }else{
            Users u = friendControllerService.getUserByUuId(user.getFriendUuid());
            friendShip.setFriend(u);
            friendShip.setUser(connectedUser.getUuid());
        }

        /*
        Prepare friendship for saving
         */


        try {
            friendControllerService.saveFriendShip(friendShip);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ADD-FRIEND: Problem saving the friendship ! try again OR read logs");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("ADD-FRIEND: Friend has been saved");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * @param user
     * @param request
     * @return
     */
    @PutMapping
    public ResponseEntity<Object> updateFriend(@RequestBody User user, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        if (user == null || user.getFriendUuid() == 0) {
            LOGGER.error("UPDATE-FRIEND: No data found in the body");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        Prepare friendShip
         */
        Users connectedUser = friendControllerService.getUserById(userId);

         /*
        Check if i received a friend request from the USER in the request
         */
        FriendShip friendShip = friendControllerService.getByFriendUuidAndUser(connectedUser.getUuid(), user.getFriendUuid());
        if (friendShip == null) {
            LOGGER.error("UPDATE-FRIEND: FriendShip not found !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        prepare update
         */
        for (GlobalAppStatus globalAppStatus : GlobalAppStatus.values()) {
            if (globalAppStatus.getValue() == user.getFriendStatus()) {
                friendShip.setStatus(globalAppStatus.name());
            }
        }

        /*
        Update
         */
        try {
            friendControllerService.updateFriendShip(friendShip);
        } catch (Exception e) {
            e.printStackTrace();

            LOGGER.error("UPDATE-FRIEND: FriendShip failed to update");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("UPDATE-FRIEND: Friend updated");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * @param friendId
     * @param request
     * @return
     */
    @DeleteMapping("/{friend_id}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("friend_id") int friendId, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connectedUser = friendControllerService.getUserById(userId);

        /*
        Check if friendship exist
         */
        FriendShip friendShip = friendControllerService.getByFriendUuidAndUser(friendId, connectedUser.getUuid());
        if (friendShip == null) {
            LOGGER.error("UPDATE-FRIEND: No friendship found to delete !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            friendControllerService.deleteFriendShip(friendShip);
        } catch (Exception e) {
            e.printStackTrace();

            LOGGER.error("DELETE-FRIEND: Failed to delete ! see logs.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.error("DELETE-FRIEND: Friend deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}