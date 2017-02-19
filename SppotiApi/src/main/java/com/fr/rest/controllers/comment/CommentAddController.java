package com.fr.rest.controllers.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.PostEntity;
import com.fr.rest.service.CommentControllerService;
import com.fr.rest.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping("/comment")
public class CommentAddController {

    private static final String ATT_USER_ID = "USER_ID";
    private Logger LOGGER = Logger.getLogger(CommentAddController.class);

    private CommentControllerService commentDataService;

    @Autowired
    public void setCommentDataService(CommentControllerService commentDataService) {
        this.commentDataService = commentDataService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO newComment, HttpServletRequest request) {

        CommentEntity commentEntityToSave = new CommentEntity();

        if (newComment != null) {

            String content = newComment.getText();
            String image = newComment.getImageLink();
            String video = newComment.getVideoLink();
            int postId = newComment.getPostId();

            if (content == null && image == null && video == null) {
                LOGGER.info("COMMENT: Missing attributes");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (content != null) {
                if (content.trim().length() <= 0) {
                    LOGGER.info("COMMENT: Content value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                commentEntityToSave.setContent(content);
            }

            if (image != null && video != null) {
                LOGGER.info("COMMENT: Image or Video ! make a choice");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (image != null) {
                if (image.trim().length() <= 0) {
                    LOGGER.info("COMMENT: imageLink value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                commentEntityToSave.setImageLink(image);
            }

            if (video != null) {
                if (video.trim().length() <= 0) {
                    LOGGER.info("COMMENT: videoLink value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                commentEntityToSave.setVideoLink(video);
            }

            Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

            try {
                CommentDTO savedComment = commentDataService.saveComment(commentEntityToSave, userId, postId);

                savedComment.setMyComment(true);

                LOGGER.info("COMMENT: post has been saved: \n" + newComment);
                return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
            } catch (Exception e) {
                LOGGER.error("COMMENT: Failed to save like", e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {
            LOGGER.error("COMMENT: Object received is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
