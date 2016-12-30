package com.fr.controllers;

import com.fr.entities.Friend;
import com.fr.entities.Users;
import com.fr.models.User;
import com.fr.repositories.FriendRepository;
import com.fr.repositories.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by djenanewail on 12/26/16.
 */

@RestController
@RequestMapping("/friend")
public class FriendController {

    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(CommentController.class);

    private FriendRepository friendRepository;
    private UserRepository userRepository;

    @Autowired
    public void setFriendRepository(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${key.friendShipPerPage}")
    private int friend_list_size;

    /**
     * @param userId
     * @param page
     * @return all user friends
     */
    @GetMapping("/{userId}/{page}")
    public ResponseEntity<Object> getAllPostComments(@PathVariable int userId, @PathVariable int page) {

        int debut = page * friend_list_size;

        Pageable pageable = new PageRequest(debut, friend_list_size);


        LOGGER.info("FRIEND_LIST: user friend list has been returned");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * @param user
     * @param request
     * @return add friend personnal information
     */
    @PostMapping
    public ResponseEntity<Friend> getAllPostComments(@RequestBody User user, HttpServletRequest request) {


        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connected_user = userRepository.getById(userId);

        if (user == null || user.getFriendUuid() == 0) {
            LOGGER.error("ADD-FRIEND: Missing parameter to add friend");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(user.getFriendUuid() == connected_user.getUuid()){
            LOGGER.error("ADD-FRIEND: FriendShip denied - choose another person !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        check if id is correct and has an entry in users table
         */
        Users userAsFriend = userRepository.getByUuid(user.getFriendUuid());
        if (userAsFriend == null) {
            LOGGER.error("ADD-FRIEND: Friend id doesn't exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        check if a friend with the id already exist
         */
        Set<Users> userss = new HashSet<>();
        Set<Friend> friends = new HashSet<>();
        Friend existingFriend = friendRepository.getByUuid(user.getFriendUuid());

        if (existingFriend == null) {

            Friend friend = new Friend(userAsFriend);
            friends.add(friend);
            connected_user.setFriends(friends);

            userss.add(connected_user);
            friend.setUsers(userss);


            try {
                //save new friend
                friendRepository.save(friend);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("ADD-FRIEND: Problem when saving friend");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {

            //check if friendship already exist
            if (userRepository.getByIdAndFriendsUuid(userId, user.getFriendUuid()) != null) {
                LOGGER.error("ADD-FRIEND: FriendShip already exist !!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            userss.add(connected_user);
            existingFriend.setUsers(userss);

            try {
                //update existing friend
                friendRepository.save(existingFriend);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("ADD-FRIEND: Problem when saving friend");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }


        LOGGER.error("ADD-FRIEND: Friend has been saved");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}