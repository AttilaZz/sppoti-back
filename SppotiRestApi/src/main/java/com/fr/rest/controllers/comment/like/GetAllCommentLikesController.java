package com.fr.rest.controllers.comment.like;

import com.fr.commons.dto.HeaderDataDTO;
import com.fr.commons.dto.post.PostResponseDTO;
import com.fr.entities.CommentEntity;
import com.fr.service.CommentControllerService;
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
public class GetAllCommentLikesController {

    private PostControllerService postDataService;
    private CommentControllerService commentControllerService;
    private LikeControllerService likeControllerService;

    @Autowired
    public void setLikeControllerService(LikeControllerService likeControllerService) {
        this.likeControllerService = likeControllerService;
    }

    @Autowired
    public void setCommentControllerService(CommentControllerService commentControllerService) {
        this.commentControllerService = commentControllerService;
    }

    @Autowired
    public void setPostDataService(PostControllerService postDataService) {
        this.postDataService = postDataService;
    }

    private Logger LOGGER = Logger.getLogger(GetAllCommentLikesController.class);

    private static final String ATT_USER_ID = "USER_ID";


    /**
     * @param id comment id.
     * @param page page number.
     * @return list of likers for a comment
     */
    @GetMapping(value = "/comment/{id}/{page}")
    public ResponseEntity<PostResponseDTO> getCommentLikers(@PathVariable("id") int id, @PathVariable("page") int page) {

        CommentEntity currentCommentEntity = commentControllerService.findComment(id);

        if (currentCommentEntity == null) {
            // post not fount
            LOGGER.info("COMMENT_LIKERS_LIST: Failed to retreive the comment id:" + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<HeaderDataDTO> likersList = likeControllerService.getCommentLikersList(id, page);

        if (likersList.isEmpty()) {
            LOGGER.info("COMMENT_LIKERS_LIST: No like for comment id:" + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PostResponseDTO pr = new PostResponseDTO();
        pr.setLikers(likersList);
        pr.setLikeCount(currentCommentEntity.getLikes().size());

        return new ResponseEntity<>(pr, HttpStatus.OK);
    }
}
