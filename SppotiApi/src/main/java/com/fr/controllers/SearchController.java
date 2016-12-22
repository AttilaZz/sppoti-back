package com.fr.controllers;

import com.fr.entities.Users;
import com.fr.models.User;
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
            LOGGER.info("SEARCH-USER: Prefix not valid !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        int debut = page * friend_size;

        Pageable pageable = new PageRequest(debut, friend_size);

        List<Users> foundUsers = userRepository.getByUsernameContaining(userPrefix, pageable);

        List<User> users = new ArrayList<>();

        for (Users u : foundUsers) {
            User user = new User();
            user.setId(u.getUuid());
            user.setUsername(u.getUsername());
            user.setFirstName(u.getFirstName());
            user.setLastName(u.getLastName());

            users.add(user);
        }

        LOGGER.info("PROFILE SEARCH-USER: Users has been returned !");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Find friends
     */

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
