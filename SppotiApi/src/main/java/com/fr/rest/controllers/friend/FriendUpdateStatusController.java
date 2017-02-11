package com.fr.rest.controllers.friend;

import com.fr.commons.dto.User;
import com.fr.entities.FriendShip;
import com.fr.entities.Users;
import com.fr.models.GlobalAppStatus;
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
    public ResponseEntity updateFriend(@RequestBody User user, HttpServletRequest request) {

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

}