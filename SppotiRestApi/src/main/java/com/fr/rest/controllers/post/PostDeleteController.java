package com.fr.rest.controllers.post;

import com.fr.aop.TraceAuthentification;
import com.fr.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 3/19/17.
 */

@RestController
@RequestMapping("/post")
public class PostDeleteController {

    private PostControllerService postDataService;

    @Autowired
    public void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }


    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    /**
     * @param postId post postId.
     * @return 200 status if post deleted.
     */
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable int postId) {

        try {
            postDataService.deletePost(postId);

        } catch (EntityNotFoundException e) {
            LOGGER.error("POST_DELETE: can't retrieve post", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
