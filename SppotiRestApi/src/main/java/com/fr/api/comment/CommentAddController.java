package com.fr.api.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.entities.CommentEntity;
import com.fr.service.CommentControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping("/comment")
class CommentAddController
{
	
	private static final String ATT_USER_ID = "USER_ID";
	
	/** Comment service. */
	private CommentControllerService commentDataService;
	
	/** Init comment service. */
	@Autowired
	void setCommentDataService(CommentControllerService commentDataService)
	{
		this.commentDataService = commentDataService;
	}
	
	@PostMapping
	ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO newComment, HttpServletRequest request)
	{
		
		CommentEntity commentEntityToSave = new CommentEntity();
		
		if (newComment != null) {
			
			String content = newComment.getText();
			String image = newComment.getImageLink();
			String video = newComment.getVideoLink();
			int postId = newComment.getPostId();
			
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
			
			Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
			
			CommentDTO savedComment = commentDataService.saveComment(commentEntityToSave, userId, postId);
			
			savedComment.setMyComment(true);
			
			return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
			
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
