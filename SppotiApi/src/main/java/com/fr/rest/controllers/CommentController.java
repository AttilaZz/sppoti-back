package com.fr.rest.controllers;

import com.fr.entities.CommentEntity;
import com.fr.rest.service.CommentControllerService;
import com.fr.rest.service.PostControllerService;
import com.fr.entities.EditHistory;
import com.fr.entities.Post;
import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentControllerService commentDataService;
    private PostControllerService postControllerService;

    @Autowired
    public void setPostControllerService(PostControllerService postControllerService) {
        this.postControllerService = postControllerService;
    }

    @Autowired
    public void setCommentDataService(CommentControllerService commentDataService) {
        this.commentDataService = commentDataService;
    }

    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(CommentController.class);

    @GetMapping("/{postId}/{page}")
    public ResponseEntity<Object> getAllPostComments(@PathVariable int postId, @PathVariable int page, HttpServletRequest httpServletRequest) {

        Long userId = (Long) httpServletRequest.getSession().getAttribute(ATT_USER_ID);

        List<CommentDTO> commentModelDTOList = commentDataService.getPostCommentsFromLastId(postId, page, userId);

        if (commentModelDTOList.isEmpty()) {
            LOGGER.info("COMMENT_LIST: No comment has been found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        LOGGER.info("COMMENT_LIST: All comments have been returned");
        return new ResponseEntity<>(commentModelDTOList, HttpStatus.OK);

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

            // get post postId to link the comment
            Post p = postControllerService.findPost(postId);

            if (p != null) {
                commentEntityToSave.setPost(p);
            } else {
                LOGGER.info("COMMENT: post postId is invalid");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

            try {
                CommentDTO savedComment = commentDataService.saveComment(commentEntityToSave, userId);

                savedComment.setMyComment(true);

                LOGGER.info("COMMENT: post has been saved: \n" + newComment);
                return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
            } catch (Exception e) {
                LOGGER.error("COMMENT: Failed to save comment", e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {
            LOGGER.error("COMMENT: Object received is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") int id, Authentication authentication) {


        CommentEntity commentEntityToDelete = commentDataService.findComment(id);

        if (commentEntityToDelete == null) {
            // post not fount
            LOGGER.error("POST: Failed to retreive the comment");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        if (commentDataService.deleteComment(commentEntityToDelete)) {
            // delete success
            LOGGER.info("DELETE: CommentEntity with postId:" + id + " has been deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // database problem
        LOGGER.info("DELETE: Database deleted problem !!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int id, @RequestBody CommentDTO newComment) {

        CommentEntity commentEntityToEdit = commentDataService.findComment(id);

        EditHistory commentEditRow = new EditHistory();
        ContentEditedResponseDTO edit = new ContentEditedResponseDTO();

        // Required attributes
        if (commentEntityToEdit == null) {
            LOGGER.info("COMMENT_UPDATE: Failed to retreive the comment");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        commentEditRow.setComment(commentEntityToEdit);

        // test the received attributes content
        if (newComment.getText() != null && newComment.getText().trim().length() > 0) {
            commentEditRow.setText(newComment.getText());
        } else {
            LOGGER.info("COMMENT_UPDATE: No content assigned with this comment");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if all argument are correctly assigned - edit post
        if (commentDataService.updateComment(commentEditRow)) {

            edit.setId(commentEntityToEdit.getId());
            edit.setDateTime(commentEditRow.getDatetimeEdited());
            edit.setText(commentEditRow.getText());

            LOGGER.info("COMMENT_UPDATE: update success");
            return new ResponseEntity<>(newComment, HttpStatus.ACCEPTED);

        } else {
            LOGGER.info("COMMENT_UPDATE: Failed when trying to update the comment in DB");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /*
    Get all comment modifications
     */
    @GetMapping("/history/{id}/{page}")
    public ResponseEntity<Object> getAllCommentModification(@PathVariable int id, @PathVariable int page, HttpServletRequest httpServletRequest) {

        Long userId = (Long) httpServletRequest.getSession().getAttribute(ATT_USER_ID);

        List<ContentEditedResponseDTO> commentModelList = commentDataService.getAllCommentHistory(id, page);

        if (commentModelList.isEmpty()) {
            LOGGER.info("COMMENT_HISTORY_LIST: No comment has been found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        LOGGER.info("COMMENT_HISTORY_LIST: All comments have been returned");
        return new ResponseEntity<>(commentModelList, HttpStatus.OK);

    }
}