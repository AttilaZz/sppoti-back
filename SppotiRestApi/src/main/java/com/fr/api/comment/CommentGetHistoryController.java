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
class CommentGetHistoryController
{
	/** Comment service. */
	private CommentControllerService commentDataService;
	
	/** Init comment service */
	@Autowired
	void setCommentDataService(CommentControllerService commentDataService)
	{
		this.commentDataService = commentDataService;
	}
	
	/**
	 * @param id
	 * 		comment iid.
	 * @param page
	 * 		page number.
	 *
	 * @return List of like DTO
	 */
	@GetMapping("/history/{id}/{page}")
	ResponseEntity<List<ContentEditedResponseDTO>> getAllCommentModification(@PathVariable int id,
																			 @PathVariable int page)
	{
		
		List<ContentEditedResponseDTO> commentModelList = commentDataService.getAllCommentHistory(id, page);
		
		if (commentModelList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(commentModelList, HttpStatus.OK);
		
	}
	
}
