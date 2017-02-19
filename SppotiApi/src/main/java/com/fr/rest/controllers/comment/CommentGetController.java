package com.fr.rest.controllers.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.rest.service.CommentControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */

@RestController
@RequestMapping("/comment")
public class CommentGetController {

    private CommentControllerService commentDataService;

    @Autowired
    public void setCommentDataService(CommentControllerService commentDataService) {
        this.commentDataService = commentDataService;
    }

    private static final String ATT_USER_ID = "USER_ID";

    private Logger LOGGER = Logger.getLogger(CommentGetController.class);

    /**
     * Get all like for a given post
     *
     * @param postId
     * @param page
     * @param httpServletRequest
     * @return List of like DTO
     */
    @GetMapping("/{postId}/{page}")
    public ResponseEntity<List<CommentDTO>> getAllPostComments(@PathVariable int postId, @PathVariable int page, HttpServletRequest httpServletRequest) {

        Long userId = (Long) httpServletRequest.getSession().getAttribute(ATT_USER_ID);

        List<CommentDTO> commentModelDTOList = commentDataService.getPostCommentsFromLastId(postId, page, userId);

        if (commentModelDTOList.isEmpty()) {
            LOGGER.info("COMMENT_LIST: No like has been found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        LOGGER.info("COMMENT_LIST: All comments have been returned");
        return new ResponseEntity<>(commentModelDTOList, HttpStatus.OK);

    }
}