package com.fr.api.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.service.CommentControllerService;
import com.fr.versionning.ApiVersion;
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
@ApiVersion("1")
class CommentGetController
{
	/** Comment service. */
	private CommentControllerService commentDataService;
	
	/** Init comment service. */
	@Autowired
	void setCommentDataService(final CommentControllerService commentDataService)
	{
		this.commentDataService = commentDataService;
	}
	
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
	ResponseEntity<List<CommentDTO>> getAllPostComments(@PathVariable final String postId, @PathVariable final int page,
														final Authentication authentication)
	{
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		final List<CommentDTO> commentModelDTOList = this.commentDataService
				.getPostCommentsFromLastId(postId, page, userId);
		
		if (commentModelDTOList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(commentModelDTOList, HttpStatus.OK);
		
	}
}