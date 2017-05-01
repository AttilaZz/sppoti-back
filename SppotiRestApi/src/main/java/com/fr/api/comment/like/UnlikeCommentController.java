package com.fr.api.comment.like;

import com.fr.entities.CommentEntity;
import com.fr.entities.UserEntity;
import com.fr.service.CommentControllerService;
import com.fr.service.LikeControllerService;
import com.fr.service.PostControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
class UnlikeCommentController
{
	
	/** Post service. */
	private final PostControllerService postDataService;
	/** Comment service. */
	private final CommentControllerService commentControllerService;
	/** Like service. */
	private final LikeControllerService likeControllerService;
	
	/** User id in session context */
	private static final String ATT_USER_ID = "USER_ID";
	
	/** Init services. */
	@Autowired
	UnlikeCommentController(PostControllerService postDataService, CommentControllerService commentControllerService,
							LikeControllerService likeControllerService)
	{
		this.postDataService = postDataService;
		this.commentControllerService = commentControllerService;
		this.likeControllerService = likeControllerService;
	}
	
	
	/**
	 * @param id
	 * 		post id.
	 * @param request
	 *
	 * @return 200 status if comment has been unliked or 404 if not.
	 */
	@DeleteMapping(value = "/comment/{id}")
	ResponseEntity<Void> unLikeComment(@PathVariable("id") int id, HttpServletRequest request)
	{
		
		CommentEntity commentEntityToLike = commentControllerService.findComment(id);
		
		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		UserEntity user = postDataService.getUserById(userId);
		
		if (commentEntityToLike == null || user == null) {
			
			if (commentEntityToLike == null) {
				// post not fount
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
		
		//post must be liked before unlike
		if (likeControllerService.isCommentAlreadyLikedByUser(id, userId)) {
			likeControllerService.unLikeComment(commentEntityToLike);
			// delete success:" + id + " has been liked by: " + user.getFirstName()
			return new ResponseEntity<>(HttpStatus.OK);
			
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
