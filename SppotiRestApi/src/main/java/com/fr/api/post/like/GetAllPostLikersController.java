package com.fr.api.post.like;

import com.fr.commons.dto.HeaderDataDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.entities.PostEntity;
import com.fr.service.LikeControllerService;
import com.fr.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
class GetAllPostLikersController {

    /**
     * Posts Controller service.
     */
    private PostControllerService postDataService;
    /**
     * Likes Controller service.
     */
    private LikeControllerService likeControllerService;
    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(GetAllPostLikersController.class);

    /**
     * Init services.
     */
    @Autowired
    public GetAllPostLikersController(PostControllerService postDataService, LikeControllerService likeControllerService) {
        this.postDataService = postDataService;
        this.likeControllerService = likeControllerService;
    }

    /**
     * @param id   post id.
     * @param page page number.
     * @return list of people who liked the post.
     */
    @GetMapping(value = "/post/{id}/{page}")
    ResponseEntity<PostDTO> getPostLikers(@PathVariable("id") int id, @PathVariable("page") int page) {

        PostEntity currentPost = postDataService.findPost(id);

        if (currentPost == null) {
            // post not fount
            LOGGER.info("POST_LIKERS_LIST: Failed to retreive the post id" + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<HeaderDataDTO> likersList = likeControllerService.getPostLikersList(id, page);

        if (likersList.isEmpty()) {
            LOGGER.info("POST_LIKERS_LIST: No likers found fot the post id:" + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PostDTO pr = new PostDTO();
        pr.setLikers(likersList);
        pr.setLikeCount(currentPost.getLikes().size());

        return new ResponseEntity<>(pr, HttpStatus.OK);
    }


}
