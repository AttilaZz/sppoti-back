package com.fr.api.comment;

import com.fr.entities.CommentEntity;
import com.fr.service.CommentControllerService;
import com.fr.versionning.ApiVersion;
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
@ApiVersion("1")
class CommentDeleteController
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
	 * @param id
	 * 		comment id.
	 * @param authentication
	 * 		spring secu auth object.
	 *
	 * @return 200 http status.
	 */
	@DeleteMapping("/{commentId}")
	ResponseEntity<Void> deleteComment(@PathVariable("commentId") final String id, final Authentication authentication)
	{
		
		final CommentEntity commentEntityToDelete = this.commentDataService.findComment(id);
		
		if (commentEntityToDelete == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		this.commentDataService.deleteComment(commentEntityToDelete);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
