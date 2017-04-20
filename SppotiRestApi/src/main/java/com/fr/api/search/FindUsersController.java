package com.fr.api.search;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;
import com.fr.repositories.UserRepository;
import com.fr.service.AccountControllerService;
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
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 12/17/16.
 */

@RestController
@RequestMapping("/find/users")
class FindUsersController {

    @Value("${key.friendShipPerPage}")
    private int friend_size;

    private final UserRepository userRepository;
    private final AccountControllerService accountControllerService;

    private Logger LOGGER = Logger.getLogger(FindUsersController.class);

    @Autowired
    FindUsersController(UserRepository userRepository, AccountControllerService accountControllerService) {
        this.userRepository = userRepository;
        this.accountControllerService = accountControllerService;
    }

    /**
     * @param userPrefix user prefix to find.
     * @param page page number.
     * @return List of all users containing the STRING in request
     */
    @GetMapping(value = "/{user}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDTO>> searchUser(@PathVariable("user") String userPrefix,
                                             @PathVariable("page") int page) {

        //TODO: move implementation to CORE MODULE

        if (userPrefix.isEmpty()) {
            LOGGER.error("SEARCH-USER: Prefix not valid !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<UserEntity> foundUsers;
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

        List<UserDTO> users = foundUsers.stream().map(accountControllerService::fillAccountResponse)
                .collect(Collectors.toList());

        LOGGER.info("PROFILE SEARCH-USER: UserEntity has been returned !");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


}
