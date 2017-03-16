package com.fr.rest.controllers.friend;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.service.FriendControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
public class FriendAddController {


    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(FriendAddController.class);

    private FriendControllerService friendControllerService;

    @Autowired
    public void setFriendControllerService(FriendControllerService friendControllerService) {
        this.friendControllerService = friendControllerService;
    }

    /**
     * @param user friend to add.
     * @param request spring secu object.
     * @return created friend.
     */
    @PostMapping
    public ResponseEntity<Object> addFriend(@RequestBody UserDTO user, HttpServletRequest request) {

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
        UserEntity connectedUser = friendControllerService.getUserById(userId);
        UserEntity friend = friendControllerService.getUserByUuId(user.getFriendUuid());

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
        FriendShipEntity tempFriendShip = friendControllerService.getByFriendUuidAndUser(friend.getUuid(), connectedUser.getUuid());
        if (tempFriendShip != null && !tempFriendShip.getStatus().equals(GlobalAppStatus.PUBLIC_RELATION)) {
            LOGGER.error("ADD-FRIEND: You are already friends");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        FriendShipEntity friendShip = new FriendShipEntity();

        //friendship aleady exist in a different status
        if (tempFriendShip != null && tempFriendShip.getStatus().equals(GlobalAppStatus.PUBLIC_RELATION)) {
            friendShip = tempFriendShip;
            friendShip.setStatus(GlobalAppStatus.PENDING);
        } else {
            UserEntity u = friendControllerService.getUserByUuId(user.getFriendUuid());
            friendShip.setFriend(u);
            friendShip.setUser(connectedUser);
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

}
