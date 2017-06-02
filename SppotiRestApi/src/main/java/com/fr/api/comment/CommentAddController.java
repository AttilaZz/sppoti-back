package com.fr.api.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.CommentEntity;
import com.fr.service.CommentControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping("/comment")
class CommentAddController
{
	
	/** Comment service. */
	private CommentControllerService commentDataService;
	
	/** Init comment service. */
	@Autowired
	void setCommentDataService(final CommentControllerService commentDataService)
	{
		this.commentDataService = commentDataService;
	}
	
	@PostMapping
	ResponseEntity<CommentDTO> addComment(@RequestBody final CommentDTO newComment, final Authentication authentication)
	{
		
		final CommentEntity commentEntityToSave = new CommentEntity();
		
		if (newComment != null) {
			
			final String content = newComment.getText();
			final String image = newComment.getImageLink();
			final String video = newComment.getVideoLink();
			final int postId = newComment.getPostId();
			
			if (content == null && image == null && video == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			if (content != null) {
				if (content.trim().length() <= 0) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				commentEntityToSave.setContent(content);
			}
			
			if (image != null && video != null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			if (image != null) {
				if (image.trim().length() <= 0) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				commentEntityToSave.setImageLink(image);
			}
			
			if (video != null) {
				if (video.trim().length() <= 0) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				commentEntityToSave.setVideoLink(video);
			}
			
			final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
			
			final CommentDTO savedComment = this.commentDataService.saveComment(commentEntityToSave, userId, postId);
			
			savedComment.setMyComment(true);
			
			return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
			
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
