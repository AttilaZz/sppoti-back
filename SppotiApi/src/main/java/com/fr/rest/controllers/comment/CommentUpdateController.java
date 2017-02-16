package com.fr.rest.controllers.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistoryEntity;
import com.fr.rest.service.CommentControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping("/comment")
public class CommentUpdateController {

    private CommentControllerService commentDataService;

    @Autowired
    public void setCommentDataService(CommentControllerService commentDataService) {
        this.commentDataService = commentDataService;
    }

    private Logger LOGGER = Logger.getLogger(CommentUpdateController.class);

    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int id, @RequestBody CommentDTO newComment) {

        CommentEntity commentEntityToEdit = commentDataService.findComment(id);

        EditHistoryEntity commentEditRow = new EditHistoryEntity();
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
            LOGGER.error("COMMENT_UPDATE: Failed when trying to update the comment in DB");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
