package com.fr.api.comment;

import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.service.CommentControllerService;
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
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping("/comment")
public class CommentGetHistoryController {

    private Logger LOGGER = Logger.getLogger(CommentGetHistoryController.class);
    private CommentControllerService commentDataService;

    @Autowired
    public void setCommentDataService(CommentControllerService commentDataService) {
        this.commentDataService = commentDataService;
    }

    /**
     * @param id
     * @param page
     * @param httpServletRequest
     * @return List of like DTO
     */
    @GetMapping("/history/{id}/{page}")
    public ResponseEntity<List<ContentEditedResponseDTO>> getAllCommentModification(@PathVariable int id, @PathVariable int page, HttpServletRequest httpServletRequest) {

        List<ContentEditedResponseDTO> commentModelList = commentDataService.getAllCommentHistory(id, page);

        if (commentModelList.isEmpty()) {
            LOGGER.info("COMMENT_HISTORY_LIST: No like has been found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        LOGGER.info("COMMENT_HISTORY_LIST: All comments have been returned");
        return new ResponseEntity<>(commentModelList, HttpStatus.OK);

    }

}
