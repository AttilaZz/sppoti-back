package com.fr.api.friend;

import com.fr.commons.dto.UserDTO;
import com.fr.service.FriendControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 12/26/16.
 * <p>
 * This controller is used to accept or refuse friend request
 */

@RestController
@RequestMapping("/friend")
public class FriendUpdateStatusController {

    private Logger LOGGER = Logger.getLogger(FriendUpdateStatusController.class);
    private FriendControllerService friendControllerService;

    @Autowired
    public void setFriendControllerService(FriendControllerService friendControllerService) {
        this.friendControllerService = friendControllerService;
    }

    /**
     * @param user friend id.
     * @param authentication spring auth.
     * @return 200 http status if updated, 400 otherwise
     */
    @PutMapping
    public ResponseEntity updateFriend(@RequestBody UserDTO user, Authentication authentication) {

        Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();

        if (user == null || user.getFriendUuid() == 0) {
            LOGGER.error("UPDATE-FRIEND: No data found in the body");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            friendControllerService.updateFriendShip(userId, user.getFriendUuid(), user.getFriendStatus());

        } catch (Exception e) {

            LOGGER.error("UPDATE-FRIEND: FriendShipEntity failed to update", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("UPDATE-FRIEND: Friend updated");
        return new ResponseEntity<>(HttpStatus.OK);

    }

}