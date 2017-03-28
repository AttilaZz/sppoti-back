package com.fr.api.friend;

import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.service.FriendControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
class FriendDeleteController {


    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(FriendDeleteController.class);

    private FriendControllerService friendControllerService;

    @Autowired
    void setFriendControllerService(FriendControllerService friendControllerService) {
        this.friendControllerService = friendControllerService;
    }

    /**
     * @param friendId       friend id.
     * @param authentication spring auth.
     * @return 200 http status if friendship deleted, 400 status otherwise
     */
    @DeleteMapping("/{friend_id}")
    ResponseEntity deleteFriend(@PathVariable("friend_id") int friendId, Authentication authentication) {

        Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
        UserEntity connectedUser = friendControllerService.getUserById(userId);

        /*
        Check if friendship exist
         */
        FriendShipEntity friendShip = friendControllerService.findFriendShip(friendId, connectedUser.getUuid());
        if (friendShip == null) {
            LOGGER.error("UPDATE-FRIEND: No friendship found to delete !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        friendControllerService.deleteFriendShip(friendShip);

        LOGGER.error("DELETE-FRIEND: Friend deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
