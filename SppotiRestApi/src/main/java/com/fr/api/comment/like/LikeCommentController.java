package com.fr.api.comment.like;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.UserEntity;
import com.fr.service.CommentBusinessService;
import com.fr.service.LikeBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
@ApiVersion("1")
class LikeCommentController
{
	/** Comment servcie. */
	private final CommentBusinessService commentControllerService;
	/** like servcie. */
	private final LikeBusinessService likeControllerService;
	
	/** Init services. */
	LikeCommentController(final CommentBusinessService commentControllerService,
						  final LikeBusinessService likeControllerService)
	{
		this.commentControllerService = commentControllerService;
		this.likeControllerService = likeControllerService;
	}
	
	
	/**
	 * @param id
	 * 		comment id.
	 * @param authentication
	 * 		security auth.
	 *
	 * @return status 200 if comment were liked or 404 if not
	 */
	@PutMapping(value = "/comment/{id}")
	ResponseEntity<Void> likeComment(@PathVariable("id") final String id, final Authentication authentication)
	{
		
		final CommentEntity commentEntityToLike = this.commentControllerService.findComment(id);
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final UserEntity user = this.likeControllerService.getUserById(userId);
		
		if (commentEntityToLike == null || user == null) {
			
			if (commentEntityToLike == null) {
				// post not found
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
		
		final LikeContentEntity likeToSave = new LikeContentEntity();
		likeToSave.setComment(commentEntityToLike);
		likeToSave.setUser(user);
		
		if (!this.likeControllerService.isCommentAlreadyLikedByUser(id, userId)) {
			this.likeControllerService.likeComment(likeToSave);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}
