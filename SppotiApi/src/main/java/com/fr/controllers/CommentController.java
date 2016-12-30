package com.fr.controllers;

import com.fr.controllers.service.CommentControllerService;
import com.fr.controllers.service.PostControllerService;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;
import com.fr.entities.Post;
import com.fr.entities.Users;
import com.fr.models.CommentModel;
import com.fr.models.ContentEditedResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        List<CommentModel> commentModelList = commentDataService.getPostCommentsFromLastId(postId, page, userId);

        if (commentModelList.isEmpty()) {
            LOGGER.info("COMMENT_LIST: No comment has been found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        LOGGER.info("COMMENT_LIST: All comments have been returned");
        return new ResponseEntity<>(commentModelList, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<CommentModel> addComment(@RequestBody CommentModel newComment, HttpServletRequest request) {

        Comment commentToSave = new Comment();

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
                commentToSave.setContent(content);
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
                commentToSave.setImageLink(image);
            }

            if (video != null) {
                if (video.trim().length() <= 0) {
                    LOGGER.info("COMMENT: videoLink value is empty");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                commentToSave.setVideoLink(video);
            }

            // get post postId to link the comment
            Post p = postControllerService.findPost(postId);

            if (p != null) {
                commentToSave.setPost(p);
            } else {
                LOGGER.info("COMMENT: post postId is invalid");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // get comment author
            Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
            Users user = commentDataService.getUserById(userId);
            commentToSave.setUser(user);

            newComment.setAuthorFirstName(user.getFirstName());
            newComment.setAuthorLastName(user.getLastName());
            newComment.setAuthorUsername(user.getUsername());

            try {
                Comment c = commentDataService.saveComment(commentToSave);
                newComment.setId(c.getUuid());
                newComment.setMyComment(true);
                newComment.setCreationDate(c.getDatetimeCreated());
                LOGGER.info("COMMENT: post has been saved");
                return new ResponseEntity<>(newComment, HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("COMMENT: Failed to save comment");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {
            LOGGER.error("COMMENT: Object received is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("postId") int id) {
        Comment commentToDelete = commentDataService.findComment(id);

        if (commentToDelete == null) {
            // post not fount
            LOGGER.error("POST: Failed to retreive the comment");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        if (commentDataService.deleteComment(commentToDelete)) {
            // delete success
            LOGGER.info("DELETE: Comment with postId:" + id + " has been deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // database problem
        LOGGER.info("DELETE: Database deleted problem !!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentModel> updateComment(@PathVariable int id, @RequestBody CommentModel newComment) {

        Comment commentToEdit = commentDataService.findComment(id);
        EditHistory commentEditRow = new EditHistory();
        ContentEditedResponse edit = new ContentEditedResponse();

        // Required attributes
        if (commentToEdit == null) {
            LOGGER.info("COMMENT_UPDATE: Failed to retreive the comment");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        commentEditRow.setComment(commentToEdit);

        // test the received attributes content
        if (newComment.getText() != null && newComment.getText().trim().length() > 0) {
            commentEditRow.setText(newComment.getText());
        } else {
            LOGGER.info("COMMENT_UPDATE: No content assigned with this comment");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if all argument are correctly assigned - edit post
        if (commentDataService.updateComment(commentEditRow)) {

            edit.setId(commentToEdit.getId());
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

        List<ContentEditedResponse> commentModelList = commentDataService.getAllCommentHistory(id, page);

        if (commentModelList.isEmpty()) {
            LOGGER.info("COMMENT_HISTORY_LIST: No comment has been found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        LOGGER.info("COMMENT_HISTORY_LIST: All comments have been returned");
        return new ResponseEntity<>(commentModelList, HttpStatus.OK);

    }
}