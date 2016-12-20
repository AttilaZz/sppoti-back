/**
 *
 */
package com.fr.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.CommentControllerService;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;
import com.fr.entities.Post;
import com.fr.entities.Users;
import com.fr.models.CommentModel;
import com.fr.models.ContentEditedResponse;
import com.fr.models.JsonPostRequest;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentControllerService commentDataService;

    @Autowired
    public void setCommentDataService(CommentControllerService commentDataService) {
        this.commentDataService = commentDataService;
    }

    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
    public ResponseEntity<CommentModel> addComment(@ModelAttribute JsonPostRequest json, HttpServletRequest request) {

        Gson gson = new Gson();
        CommentModel newComment = null;
        Comment commentToSave = new Comment();
        if (json != null) {
            try {
                newComment = gson.fromJson(json.getJson(), CommentModel.class);
                LOGGER.info("COMMENT POST - data sent by user: " + new ObjectMapper().writeValueAsString(newComment));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("POST: Data sent by user are invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (newComment != null) {

            String content = newComment.getText();
            String image = newComment.getImageLink();
            String video = newComment.getVideoLink();
            Long postId = newComment.getPostId();

            if ((content == null && image == null && video == null) && postId == null) {
                LOGGER.info("COMMENT: Missing attributes");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (content != null) {
                if (content.trim().length() <= 0) {
                    LOGGER.info("COMMENT: Content value is empty");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                commentToSave.setContent(content);
            }
            if (image != null) {
                if (image.trim().length() <= 0) {
                    LOGGER.info("COMMENT: imageLink value is empty");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                commentToSave.setImageLink(image);
            }
            if (video != null) {
                if (video.trim().length() <= 0) {
                    LOGGER.info("COMMENT: videoLink value is empty");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                commentToSave.setVideoLink(video);
            }
            // get post id to link the comment
            if (postId != null && postId instanceof Long) {
                Post p = commentDataService.findPostById(postId);
                if (p != null) {
                    commentToSave.setPostComment(p);
                } else {
                    LOGGER.info("COMMENT: post id is invalid");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                LOGGER.info("COMMENT: post id must be numeric value");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // get comment author
            Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
            Users user = commentDataService.getUserById(userId);
            commentToSave.setUser(user);

            newComment.setAuthorFirstName(user.getFirstName());
            newComment.setAuthorLastName(user.getLastName());
            newComment.setAuthorUserName(user.getUsername());

            if (commentDataService.saveComment(commentToSave)) {
                LOGGER.info("COMMENT: post has been saved");
                return new ResponseEntity<CommentModel>(newComment, HttpStatus.CREATED);
            } else {
                LOGGER.info("COMMENT: Failed when saving the post in the DB");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } else {
            LOGGER.info("COMMENT: Object received is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        Comment commentToDelete = commentDataService.findComment(id);

        if (commentToDelete == null) {
            // post not fount
            LOGGER.info("POST: Failed to retreive the comment");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

        if (commentDataService.deleteComment(commentToDelete)) {
            // delete success
            LOGGER.info("DELETE: Comment with id:" + id + " has been deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // database problem
        LOGGER.info("DELETE: Database deleted problem !!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
    public ResponseEntity<CommentModel> updateComment(@PathVariable("id") Long id,
                                                      @ModelAttribute JsonPostRequest json) {

        Gson gson = new Gson();
        CommentModel newComment = null;
        ContentEditedResponse edit = new ContentEditedResponse();

        if (json != null) {
            try {
                newComment = gson.fromJson(json.getJson(), CommentModel.class);

                LOGGER.info("COMMENT_UPDATE data sent by user: " + new ObjectMapper().writeValueAsString(newComment));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("COMMENT_UPDATE: Data sent by user are invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Comment commentToEdit = commentDataService.findComment(id);
        EditHistory commentEditRow = new EditHistory();

        // Required attributes
        if (commentToEdit == null) {
            LOGGER.info("COMMENT_UPDATE: Failed to retreive the comment");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        commentEditRow.setComment(commentToEdit);

        // test the received attributes content
        if (newComment.getText() != null && newComment.getText().trim().length() > 0) {
            commentEditRow.setText(newComment.getText());
        } else {
            LOGGER.info("COMMENT_UPDATE: No content assigned with this comment");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // if all argument are correctly assigned - edit post
        if (commentDataService.updateComment(commentEditRow)) {

            edit.setId(commentToEdit.getId());
            edit.setDateTime(commentEditRow.getDatetimeEdited());
            edit.setText(commentEditRow.getText());

            LOGGER.info("COMMENT_UPDATE: update success");
            return new ResponseEntity<CommentModel>(newComment, HttpStatus.ACCEPTED);
        } else {
            LOGGER.info("COMMENT_UPDATE: Failed when trying to update the comment in DB");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
}
