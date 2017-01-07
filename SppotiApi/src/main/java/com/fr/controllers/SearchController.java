package com.fr.controllers;

import com.fr.controllers.service.AccountControllerService;
import com.fr.entities.FriendShip;
import com.fr.entities.Users;
import com.fr.models.FriendStatus;
import com.fr.models.User;
import com.fr.repositories.FriendShipRepository;
import com.fr.repositories.UserRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 12/17/16.
 */

@RestController
@RequestMapping("/find")
public class SearchController {

    @Value("${key.friendShipPerPage}")
    private int friend_size;

    private UserRepository userRepository;
    private FriendShipRepository friendShipRepository;

    private AccountControllerService accountControllerService;

    @Autowired
    public void setFriendShipRepository(FriendShipRepository friendShipRepository) {
        this.friendShipRepository = friendShipRepository;
    }

    @Autowired
    public void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Logger LOGGER = Logger.getLogger(SearchController.class);

    private static final String ATT_USER_ID = "USER_ID";

    /**
     * Find users
     */
    @GetMapping(value = "/users/{user}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> searchUser(@PathVariable("user") String userPrefix,
                                                 @PathVariable("page") int page, HttpServletRequest request) {


        if (userPrefix.isEmpty()) {
            LOGGER.error("SEARCH-USER: Prefix not valid !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Users> foundUsers = null;
        Pageable pageable = new PageRequest(page, friend_size);

        String[] parts = userPrefix.split(" ");

        if (parts.length > 2) {
            LOGGER.error("SEARCH-USER: Too many words in your request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (parts.length == 2) {
            //get users by first name and last name
            foundUsers = userRepository.getSearchedUsersByFirstNameAndLastName(parts[0], parts[1], pageable);
        } else {
            //get users by username, first name and last name
            foundUsers = userRepository.getSearchedUsers(parts[0], pageable);

        }


        List<User> users = new ArrayList<>();

        for (Users users1 : foundUsers) {

            users.add(accountControllerService.fillUserResponse(users1, null));
        }

        LOGGER.info("PROFILE SEARCH-USER: Users has been returned !");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Find confirmed friends friends
     */
    @GetMapping(value = "/friends/{motif}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> searchConfimedFriend(@PathVariable("motif") String userPrefix,
                                                           @PathVariable("page") int page) {


        if (userPrefix.isEmpty()) {
            LOGGER.error("SEARCH-CONFIRMED-FRIEND: Prefix not valid !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<FriendShip> foundFriends;
        Pageable pageable = new PageRequest(page, friend_size);

        String[] parts = userPrefix.split(" ");

        if (parts.length > 2) {
            LOGGER.error("SEARCH-CONFIRMED-FRIEND: Too many words in your request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else //get users by first name and last name
            if (parts.length == 2)
                foundFriends = friendShipRepository.findFriendsByFirstNameAndLastNameAndStatus(parts[0], parts[1], FriendStatus.CONFIRMED.name(), pageable);
            else {
                //get users by username, first name and last name
                foundFriends = friendShipRepository.findFriendByUsernameAndStatus(parts[0], FriendStatus.CONFIRMED.name(), pageable);

            }


        List<User> users = new ArrayList<>();

        for (FriendShip friendShip : foundFriends) {

            users.add(accountControllerService.fillUserResponse(friendShip.getFriend(), null));
        }

        LOGGER.info("SEARCH-CONFIRMED-FRIEND: friends has been returned !");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    /**
     * Find spooties
     */

    /**
     * Find posts
     */

    /**
     * Find comments
     */
}
