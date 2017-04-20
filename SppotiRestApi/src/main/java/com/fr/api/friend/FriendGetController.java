package com.fr.api.friend;

import com.fr.commons.dto.FriendResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.service.AccountControllerService;
import com.fr.service.FriendControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
class FriendGetController {

    private Logger LOGGER = Logger.getLogger(FriendGetController.class);

    private AccountControllerService accountControllerService;

    private FriendControllerService friendControllerService;

    @Autowired
    void setFriendControllerService(FriendControllerService friendControllerService) {
        this.friendControllerService = friendControllerService;
    }

    @Autowired
    void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    @Value("${key.friendShipPerPage}")
    private int friend_list_size;

    /**
     * @param userId connected user id.
     * @param page   page number.
     * @return confirmed friend list.
     */
    @GetMapping("/confirmed/{userId}/{page}")
    ResponseEntity<List<UserDTO>> getConfirmedFriendList(@PathVariable int userId, @PathVariable int page) {

        List<UserDTO> friendList = friendControllerService.getConfirmedFriendList(userId, page);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendList, HttpStatus.OK);

    }

    /**
     * @param userId         connected user id.
     * @param page           page number.
     * @param authentication spring secu.
     * @return all refused friend requests.
     */
    @GetMapping("/refused/{userId}/{page}")
    ResponseEntity<FriendResponseDTO> getRefusedFriendList(@PathVariable int userId, @PathVariable int page, Authentication authentication) {

        Long connected_user = ((AccountUserDetails) authentication.getPrincipal()).getId();
        UserEntity connectedUser = friendControllerService.getUserById(connected_user);


        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShipEntity> friendShips = friendControllerService.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.REFUSED, pageable);

        if (friendShips.isEmpty()) {
            LOGGER.error("GET_REFUSED_FRIEND_REQUEST: No sent friend request found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        FriendResponseDTO friendResponse = getFriendResponse(friendShips, connected_user);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }


    /**
     * @param page           page number.
     * @param authentication spring secu.
     * @return all pending requests.
     */
    @GetMapping("/pending/sent/{page}")
    ResponseEntity<FriendResponseDTO> getSentPendingFriendList(@PathVariable int page, Authentication authentication) {

        Long connected_user = ((AccountUserDetails) authentication.getPrincipal()).getId();
        UserEntity connectedUser = friendControllerService.getUserById(connected_user);

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShipEntity> friendShips = friendControllerService.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.PENDING, pageable);

        if (friendShips.isEmpty()) {
            LOGGER.error("GET_PENDING_SENT: No sent friend request found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        FriendResponseDTO friendResponse = getFriendResponse(friendShips, connected_user);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }

    /**
     * @param page           page number.
     * @param authentication spring secu.
     * @return all friend pending requests.
     */
    @GetMapping("/pending/received/{page}")
    ResponseEntity<FriendResponseDTO> getReceivedPendingFriendList(@PathVariable int page, Authentication authentication) {

        Long connected_user = ((AccountUserDetails) authentication.getPrincipal()).getId();
        UserEntity connectedUser = friendControllerService.getUserById(connected_user);

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShipEntity> friendShips = friendControllerService.getByFriendUuidAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.PENDING, pageable);

        if (friendShips.isEmpty()) {
            LOGGER.error("GET_PENDING_RECEIVED: No received request friend found !");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        FriendResponseDTO friendResponse = getFriendResponse(friendShips, connected_user);

        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(friendResponse, HttpStatus.OK);

    }

    /**
     * @param friendShips list of all friendships.
     * @return friend response DTO.
     */
    private FriendResponseDTO getFriendResponse(List<FriendShipEntity> friendShips, Long connectedUser) {
        List<UserDTO> friendList = new ArrayList<>();

        for (FriendShipEntity friendShip : friendShips) {
            UserEntity user;

            if (friendShip.getFriend().getId().equals(connectedUser)) {
                user = friendShip.getUser();
            } else {
                user = friendShip.getFriend();
            }

            friendList.add(accountControllerService.fillAccountResponse(user));

        }

        FriendResponseDTO friendResponse = new FriendResponseDTO();
        friendResponse.setPendingList(friendList);

        return friendResponse;
    }
}
