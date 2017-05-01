package com.fr.api.comment.like;

import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.UserEntity;
import com.fr.service.CommentControllerService;
import com.fr.service.LikeControllerService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
class LikeCommentController
{
	/** Comment servcie. */
	private final CommentControllerService commentControllerService;
	/** like servcie. */
	private final LikeControllerService likeControllerService;
	
	private static final String ATT_USER_ID = "USER_ID";
	
	/** Init services. */
	LikeCommentController(CommentControllerService commentControllerService,
						  LikeControllerService likeControllerService)
	{
		this.commentControllerService = commentControllerService;
		this.likeControllerService = likeControllerService;
	}
	
	
	/**
	 * @param id
	 * 		comment id.
	 * @param request
	 *
	 * @return status 200 if comment were liked or 404 if not
	 */
	@PutMapping(value = "/comment/{id}")
	ResponseEntity<Void> likeComment(@PathVariable("id") int id, HttpServletRequest request)
	{
		
		CommentEntity commentEntityToLike = commentControllerService.findComment(id);
		
		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		UserEntity user = likeControllerService.getUserById(userId);
		
		if (commentEntityToLike == null || user == null) {
			
			if (commentEntityToLike == null) {
				// post not found
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
		
		LikeContentEntity likeToSave = new LikeContentEntity();
		likeToSave.setComment(commentEntityToLike);
		likeToSave.setUser(user);
		
		if (!likeControllerService.isCommentAlreadyLikedByUser(id, userId)) {
			likeControllerService.likeComment(likeToSave);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}
