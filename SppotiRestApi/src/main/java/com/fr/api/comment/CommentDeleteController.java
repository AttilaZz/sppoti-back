package com.fr.api.comment;

import com.fr.entities.CommentEntity;
import com.fr.service.CommentControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping("/comment")
class CommentDeleteController
{
	/** Comment service. */
	private CommentControllerService commentDataService;
	
	/** Init comment service.*/
	@Autowired
	void setCommentDataService(CommentControllerService commentDataService)
	{
		this.commentDataService = commentDataService;
	}
	
	@DeleteMapping("/{commentId}")
	ResponseEntity<Void> deleteComment(@PathVariable("commentId") int id, Authentication authentication)
	{
		
		
		CommentEntity commentEntityToDelete = commentDataService.findComment(id);
		
		if (commentEntityToDelete == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
		
		if (commentDataService.deleteComment(commentEntityToDelete)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
