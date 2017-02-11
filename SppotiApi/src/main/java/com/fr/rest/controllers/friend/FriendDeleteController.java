package com.fr.rest.controllers.friend;

import com.fr.entities.FriendShip;
import com.fr.entities.Users;
import com.fr.rest.service.FriendControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
public class FriendDeleteController {


    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(FriendDeleteController.class);

    private FriendControllerService friendControllerService;


    @Autowired
    public void setFriendControllerService(FriendControllerService friendControllerService) {
        this.friendControllerService = friendControllerService;
    }

    /**
     *
     * @param friendId
     * @param request
     * @return 200 http status if friendship deleted, 400 status otherwise
     */
    @DeleteMapping("/{friend_id}")
    public ResponseEntity deleteFriend(@PathVariable("friend_id") int friendId, HttpServletRequest request) {

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
