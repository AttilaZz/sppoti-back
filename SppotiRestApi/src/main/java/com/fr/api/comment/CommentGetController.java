package com.fr.api.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.service.CommentControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */

@RestController
@RequestMapping("/comment")
class CommentGetController
{
	/** Comment service. */
	private CommentControllerService commentDataService;
	
	/** Init comment service. */
	@Autowired
	void setCommentDataService(CommentControllerService commentDataService)
	{
		this.commentDataService = commentDataService;
	}
	
	private static final String ATT_USER_ID = "USER_ID";
	
	private Logger LOGGER = Logger.getLogger(CommentGetController.class);
	
	/**
	 * Get all like for a given post
	 *
	 * @param postId
	 * 		post id.
	 * @param page
	 * 		page number.
	 * @param authentication
	 * 		auth object spring secu.
	 *
	 * @return List of like DTO.
	 */
	@GetMapping("/{postId}/{page}")
	ResponseEntity<List<CommentDTO>> getAllPostComments(@PathVariable int postId, @PathVariable int page,
														Authentication authentication)
	{
		
		Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		List<CommentDTO> commentModelDTOList = commentDataService.getPostCommentsFromLastId(postId, page, userId);
		
		if (commentModelDTOList.isEmpty()) {
			LOGGER.info("COMMENT_LIST: No like has been found");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		LOGGER.info("COMMENT_LIST: All comments have been returned");
		return new ResponseEntity<>(commentModelDTOList, HttpStatus.OK);
		
	}
}