package com.fr.rest.controllers.friend;

import com.fr.commons.dto.UserDTO;
import com.fr.rest.service.FriendControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 12/26/16.
 * <p>
 * This controller is used to accept or refuse friend request
 */

@RestController
@RequestMapping("/friend")
public class FriendUpdateStatusController {

    private static final String ATT_USER_ID = "USER_ID";
    private Logger LOGGER = Logger.getLogger(FriendUpdateStatusController.class);
    private FriendControllerService friendControllerService;

    @Autowired
    public void setFriendControllerService(FriendControllerService friendControllerService) {
        this.friendControllerService = friendControllerService;
    }

    /**
     * @param user
     * @param request
     * @return 200 http status if updated, 400 otherwise
     */
    @PutMapping
    public ResponseEntity updateFriend(@RequestBody UserDTO user, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

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