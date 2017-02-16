package com.fr.rest.controllers.search;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.FriendShipEntity;
import com.fr.models.GlobalAppStatus;
import com.fr.repositories.FriendShipRepository;
import com.fr.rest.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wdjenane on 10/02/2017.
 */
@RestController
@RequestMapping("/find/friends")
public class FindFriendController {


    @Value("${key.friendShipPerPage}")
    private int friend_size;

    private final FriendShipRepository friendShipRepository;

    private final AccountControllerService accountControllerService;

    private Logger LOGGER = Logger.getLogger(FindFriendController.class);

    @Autowired
    public FindFriendController(FriendShipRepository friendShipRepository, AccountControllerService accountControllerService) {
        this.friendShipRepository = friendShipRepository;
        this.accountControllerService = accountControllerService;
    }

    /**
     * @param userPrefix
     * @param page
     * @return All confirmed friends of connected user
     */
    @GetMapping(value = "/{motif}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> searchConfimedFriend(@PathVariable("motif") String userPrefix,
                                                              @PathVariable("page") int page) {


        if (userPrefix.isEmpty()) {
            LOGGER.error("SEARCH-CONFIRMED-FRIEND: Prefix not valid !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<FriendShipEntity> foundFriends;
        Pageable pageable = new PageRequest(page, friend_size);

        String[] parts = userPrefix.split(" ");

        if (parts.length > 2) {
            LOGGER.error("SEARCH-CONFIRMED-FRIEND: Too many words in your request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else //get users by first name and last name
            if (parts.length == 2)
                foundFriends = friendShipRepository.findFriendsByFirstNameAndLastNameAndStatus(parts[0], parts[1], GlobalAppStatus.CONFIRMED.name(), pageable);
            else {
                //get users by username, first name and last name
                foundFriends = friendShipRepository.findFriendByUsernameAndStatus(parts[0], GlobalAppStatus.CONFIRMED.name(), pageable);

            }


        List<UserDTO> users = new ArrayList<>();

        for (FriendShipEntity friendShip : foundFriends) {

            users.add(accountControllerService.fillUserResponse(friendShip.getFriend(), null));
        }

        LOGGER.info("SEARCH-CONFIRMED-FRIEND: friends has been returned !");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
