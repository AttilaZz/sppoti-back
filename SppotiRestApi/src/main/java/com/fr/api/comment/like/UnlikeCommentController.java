package com.fr.api.comment.like;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.CommentEntity;
import com.fr.entities.UserEntity;
import com.fr.service.CommentControllerService;
import com.fr.service.LikeControllerService;
import com.fr.service.PostControllerService;
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
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
@ApiVersion("1")
class UnlikeCommentController
{
	
	/** Post service. */
	private final PostControllerService postDataService;
	/** Comment service. */
	private final CommentControllerService commentControllerService;
	/** Like service. */
	private final LikeControllerService likeControllerService;
	
	/** Init services. */
	@Autowired
	UnlikeCommentController(final PostControllerService postDataService,
							final CommentControllerService commentControllerService,
							final LikeControllerService likeControllerService)
	{
		this.postDataService = postDataService;
		this.commentControllerService = commentControllerService;
		this.likeControllerService = likeControllerService;
	}
	
	
	/**
	 * @param id
	 * 		post id.
	 * @param authentication
	 * 		spring secu.
	 *
	 * @return 200 status if comment has been unliked or 404 if not.
	 */
	@DeleteMapping(value = "/comment/{id}")
	ResponseEntity<Void> unLikeComment(@PathVariable("id") final int id, final Authentication authentication)
	{
		
		final CommentEntity commentEntityToLike = this.commentControllerService.findComment(id);
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final UserEntity user = this.postDataService.getUserById(userId);
		
		if (commentEntityToLike == null || user == null) {
			
			if (commentEntityToLike == null) {
				// post not fount
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
		
		//post must be liked before unlike
		if (this.likeControllerService.isCommentAlreadyLikedByUser(id, userId)) {
			this.likeControllerService.unLikeComment(commentEntityToLike);
			// delete success:" + id + " has been liked by: " + user.getFirstName()
			return new ResponseEntity<>(HttpStatus.OK);
			
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
