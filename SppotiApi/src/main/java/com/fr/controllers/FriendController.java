package com.fr.controllers;

import com.fr.entities.Friend;
import com.fr.entities.Users;
import com.fr.models.FriendStatus;
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
    public ResponseEntity<Friend> addFriend(@RequestBody User user, HttpServletRequest request) {


        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connected_user = userRepository.getById(userId);

        if (user == null || user.getFriendUuid() == 0) {
            LOGGER.error("ADD-FRIEND: Missing parameter to add friend");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Users targetFriend = userRepository.getByUuid(user.getFriendUuid());
        if (targetFriend == null) {

            LOGGER.error("ADD-FRIEND: Friend id unknown");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        friend with my self
         */
        if (connected_user.getId().equals(targetFriend.getId())) {
            LOGGER.error("ADD-FRIEND: FriendShip denied - choose another person !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        Check if friendship already exist
         */

        Set<Friend> friendsTemp = connected_user.getFriends();
        for (Friend friend : friendsTemp) {
            if (friend.getUuid() == user.getFriendUuid()) {
                LOGGER.error("ADD-FRIEND: FriendShip already exist - choose another person !");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        /*
        save
         */

        Set<Friend> friends = new HashSet<>();

        Friend friend = new Friend(targetFriend);
        friend.setUsers(connected_user);
        friends.add(friend);
        connected_user.setFriends(friends);

        try {
            //save new friend
            userRepository.save(connected_user);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ADD-FRIEND: Problem when saving friend");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        LOGGER.info("ADD-FRIEND: Friend has been saved");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Friend> updateFriend(@RequestBody User user, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        if (user == null) {
            LOGGER.error("UPDATE-FRIEND: No data found in the body");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        check if the given friend id refers to a friend
         */
        Friend targetFriend = friendRepository.getByUuid(user.getFriendUuid());
        if (targetFriend == null) {
            LOGGER.error("UPDATE-FRIEND: No friend with the givven id");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        Check if the target friend is friend with connected user
         */
        if (userRepository.getByIdAndFriendsUuid(userId, user.getFriendUuid()) == null) {
            LOGGER.error("ADD-FRIEND: FriendShip doesn't exist !!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
        Check if setted status is correct
         */
        if (user.getFriendStatus() != FriendStatus.PENDING.getValue()
                && user.getFriendStatus() != FriendStatus.CONFIRMED.getValue()
                && user.getFriendStatus() != FriendStatus.REFUSED.getValue()) {

            LOGGER.error("UPDATE-FRIEND: Status are incorrect ! (0 - 1 - 2)");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        /*
        Prepare update
         */
        for (FriendStatus friendStatus : FriendStatus.values()) {
            if (friendStatus.getValue() == user.getFriendStatus()) {
                targetFriend.setStatus(friendStatus.name());
            }
        }

        /*
        Update
         */
        try {
            friendRepository.save(targetFriend);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("UPDATE-FRIEND: Problem updating friend status");
        }

        LOGGER.info("UPDATE-FRIEND: Friend updated");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/{friend_id}")
    public ResponseEntity<Friend> deleteFriend(@PathVariable("friend_id") int friendId, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

         /*
        Check if the target friend is friend with connected user
         */
        if (userRepository.getByIdAndFriendsUuid(userId, friendId) == null) {
            LOGGER.error("ADD-FRIEND: FriendShip doesn't exist !!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Friend friend = friendRepository.getByUuid(friendId);

        try {
            friendRepository.delete(friend);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("DELETE-FRIEND: Problem deleting friend");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        LOGGER.error("DELETE-FRIEND: Friend deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}