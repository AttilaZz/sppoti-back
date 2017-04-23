package com.fr.api.post;

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
class PostDeleteController {

    /**
     * Post controller service.
     */
    private PostControllerService postDataService;

    /**
     * Init post service.
     */
    @Autowired
    void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }

    /**
     * @param postId post postId.
     * @return 200 status if post deleted.
     */
    @DeleteMapping(value = "/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable int postId) {

        postDataService.deletePost(postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
