/**
 *
 */
package com.fr.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.CommentControllerService;
import com.fr.models.CommentModel;
import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.models.JsonPostRequest;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired

    private CommentControllerService commentDataService;

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

    @RequestMapping(value = "/like/{id}", method = RequestMethod.GET)
    public ResponseEntity<Void> likeComment(@PathVariable("id") Long id, HttpServletRequest request) {

        Comment commentToLike = commentDataService.findComment(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = commentDataService.getUserById(userId);

        if (commentToLike == null || user == null) {

            if (commentToLike == null) {
                // post not fount
                LOGGER.info("LIKE_COMMENT: Failed to retreive the post");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.info("LIKE_COMMENT: Failed to retreive user session");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        LikeContent likeToSave = new LikeContent();
        likeToSave.setComment(commentToLike);
        likeToSave.setUser(user);

        if (!commentDataService.isCommentAlreadyLikedByUser(id, userId)) {

            if (commentDataService.likeComment(likeToSave)) {
                // delete success
                LOGGER.info("LIKE_COMMENT: Comment with id:" + id + " has been liked by: " + user.getFirstName() + " "
                        + user.getLastName());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                LOGGER.info("LIKE_COMMENT: Database like problem !!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.info("LIKE: Comment already liked by: " + user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/unlike/{id}", method = RequestMethod.GET)
    public ResponseEntity<Void> unLikeComment(@PathVariable("id") Long id, HttpServletRequest request) {

        Comment commentToLike = commentDataService.findComment(id);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = commentDataService.getUserById(userId);

        if (commentToLike == null || user == null) {

            if (commentToLike == null) {
                // post not fount
                LOGGER.info("LIKE_COMMENT: Failed to retreive the comment");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.info("LIKE_COMMENT: Failed to retreive user session");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }
        if (commentDataService.isCommentAlreadyLikedByUser(id, userId)) {
            if (commentDataService.unLikeComment(id, userId)) {
                // delete success
                LOGGER.info("LIKE_COMMENT: Comment with id:" + id + " has been unliked by: " + user.getFirstName() + " "
                        + user.getLastName());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                LOGGER.info("LIKE_COMMENT: Database like problem !!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.info("LIKE_COMMENT: Comment not liked by: " + user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /*
     * List of people who liked the comment
     */
    @RequestMapping(value = "/like/list/{id}/{page}", method = RequestMethod.GET)
    public ResponseEntity<CommentModel> getPostLikers(@PathVariable("id") Long id, @PathVariable("page") int page) {

        Comment currentComment = commentDataService.findComment(id);

        if (currentComment == null) {
            // post not fount
            LOGGER.info("COMMENT_LIKERS_COMMENT: Failed to retreive the post");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<HeaderData> likersList = commentDataService.getLikersList(id, page);

        if (likersList.isEmpty()) {
            LOGGER.info("COMMENT_LIKERS_COMMENT: Empty !!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        CommentModel pr = new CommentModel();
        pr.setCommentLikers(likersList);
        pr.setLikeCount(currentComment.getLikes().size());

        LOGGER.info("COMMENT_LIKERS_COMMENT: returned :-)");
        return new ResponseEntity<>(pr, HttpStatus.OK);
    }

    @RequestMapping(value = "/post/{id}/{bottomMajId}", method = RequestMethod.GET)
    public ResponseEntity<List<CommentModel>> getAllPostComment(@PathVariable("id") Long postId,
                                                                @PathVariable("bottomMajId") int bottomMajId, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Post mPost = commentDataService.findPostById(postId);

        // Required attributes
        if (mPost == null) {
            LOGGER.info("COMMENT: Failed to retreive the post id");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<CommentModel> postComments = commentDataService.getPostCommentsFromLastId(postId, bottomMajId, userId);

        return new ResponseEntity<List<CommentModel>>(postComments, HttpStatus.OK);
    }

    @RequestMapping(value = "/edithistory/{id}/{page}", method = RequestMethod.GET)
    public ResponseEntity<List<ContentEditedResponse>> editHistory(@PathVariable("id") Long id,
                                                                   @PathVariable("page") int page, HttpServletRequest request) {

        Comment targetComment = commentDataService.findComment(id);

        if (targetComment == null) {

            // post not fount
            LOGGER.info("COMMENT_EDIT_HISTORY: Failed to retreive the comment");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        LOGGER.info("COMMENT_EDIT_HISTORY: comment history has been returned for id:" + id);
        return new ResponseEntity<>(commentDataService.getAllPostHistory(id, page), HttpStatus.OK);

    }

}
