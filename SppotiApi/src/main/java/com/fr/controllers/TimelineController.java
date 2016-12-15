/**
 *
 */
package com.fr.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.ActuControllerService;
import com.fr.entities.Post;

/**
 * Created by: Wail DJENANE on Jun 21, 2016
 */

@RestController
@RequestMapping("/actu")
public class TimelineController {

    @Autowired
    private ActuControllerService actuControllerService;

    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    private static final String ATT_USER_ID = "USER_ID";

    @RequestMapping(value = "/allUserActu/{pageId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Post>> getAllUserFeeds(@PathVariable("pageId") int pageId, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        LOGGER.info("Connected user: " + userId);

        if (pageId < 0) {
            LOGGER.info("Page number is invalid !!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Post> data = actuControllerService.getAllUserFriendPosts(userId, pageId);

        if (data.isEmpty()) {
            LOGGER.info("No data to return !!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Post>>(data, HttpStatus.OK);

    }

}
