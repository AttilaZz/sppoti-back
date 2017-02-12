package com.fr.rest.controllers.friend;

import com.fr.commons.dto.FriendResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.FriendShip;
import com.fr.entities.UserEntity;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.service.AccountControllerService;
import com.fr.rest.service.FriendControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
public class FriendGetController {

    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(FriendGetController.class);

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
    public ResponseEntity<FriendResponseDTO> getConfirmedFriendList(@PathVariable int userId, @PathVariable int page, HttpServletRequest request) {

        Long connected_user = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity connectedUser = friendControllerService.getUserById(connected_user);

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShip> friendShips = friendControllerService.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatus.CONFIRMED.name(), pageable);

        FriendResponseDTO friendResponse = getFriendResponse(friendShips);

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
    public ResponseEntity<FriendResponseDTO> getRefusedFriendList(@PathVariable int userId, @PathVariable int page, HttpServletRequest request) {

        Long connected_user = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity connectedUser = friendControllerService.getUserById(connected_user);


        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShip> friendShips = friendControllerService.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatus.REFUSED.name(), pageable);

        if (friendShips.isEmpty()) {
            LOGGER.error("GET_REFUSED_FRIEND_REQUEST: No sent friend request found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        FriendResponseDTO friendResponse = getFriendResponse(friendShips);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }


    /**
     * @param page
     * @param request
     * @return
     */
    @GetMapping("/pending/sent/{page}")
    public ResponseEntity<FriendResponseDTO> getSentPendingFriendList(@PathVariable int page, HttpServletRequest request) {

        Long connected_user = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity connectedUser = friendControllerService.getUserById(connected_user);

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShip> friendShips = friendControllerService.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatus.PENDING.name(), pageable);

        if (friendShips.isEmpty()) {
            LOGGER.error("GET_PENDING_SENT: No sent friend request found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        FriendResponseDTO friendResponse = getFriendResponse(friendShips);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }


    /**
     * @param page
     * @param request
     * @return
     */
    @GetMapping("/pending/received/{page}")
    public ResponseEntity<FriendResponseDTO> getREceivedPendingFriendList(@PathVariable int page, HttpServletRequest request) {

        Long connected_user = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity connectedUser = friendControllerService.getUserById(connected_user);

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShip> friendShips = friendControllerService.getByFriendUuidAndStatus(connectedUser.getUuid(), GlobalAppStatus.PENDING.name(), pageable);

        if (friendShips.isEmpty()) {
            LOGGER.error("GET_PENDING_RECEIVED: No received request friend found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        FriendResponseDTO friendResponse = getFriendResponse(friendShips);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }


    private FriendResponseDTO getFriendResponse(List<FriendShip> friendShips) {
        List<UserDTO> friendList = new ArrayList<>();

        for (FriendShip friendShip : friendShips) {
            UserEntity userdb = friendShip.getFriend();

            UserDTO user = accountControllerService.fillUserResponse(userdb, null);

            friendList.add(user);

        }

        FriendResponseDTO friendResponse = new FriendResponseDTO();
        friendResponse.setPendingList(friendList);

        return friendResponse;
    }
}
