package com.fr.controllers;

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

/**
 * Created by djenanewail on 12/26/16.
 */

@RestController
@RequestMapping("/friend")
public class FriendController extends AbstractRestController {

    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(CommentController.class);

    @Value("${key.friendShipPerPage}")
    private int friend_list_size;



    @GetMapping("/{userId}/{page}")
    public ResponseEntity<Object> getAllPostComments(@PathVariable int userId, @PathVariable int page) {

        int debut = page * friend_list_size;

        Pageable pageable = new PageRequest(debut, friend_list_size);



        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
