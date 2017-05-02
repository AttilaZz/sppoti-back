package com.fr.api.post.like;

import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
import com.fr.security.AccountUserDetails;
import com.fr.service.LikeControllerService;
import com.fr.service.PostControllerService;
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
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
class UnlikePostController
{
	
	/** Post service. */
	private final PostControllerService postDataService;
	
	/** Like service. */
	private final LikeControllerService likeControllerService;
	
	/** Init services. */
	@Autowired
	public UnlikePostController(final PostControllerService postDataService,
								final LikeControllerService likeControllerService)
	{
		this.postDataService = postDataService;
		this.likeControllerService = likeControllerService;
	}
	
	/** Init services. */
	private final Logger LOGGER = Logger.getLogger(UnlikePostController.class);
	
	/**
	 * @param id
	 * 		post id.
	 * @param authentication
	 * 		spring auth.
	 *
	 * @return unlike post
	 */
	@DeleteMapping(value = "/post/{id}")
	ResponseEntity<Void> unLikePost(@PathVariable("id") final int id, final Authentication authentication)
	{
		
		final PostEntity postToUnlike = this.postDataService.findPost(id);
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		final UserEntity user = accountUserDetails.getConnectedUserDetails();
		
		if (postToUnlike == null) {
			// post not fount
			this.LOGGER.error("UNLIKE_POST: Failed to retreive the post");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		//post must be liked before unlike
		if (this.likeControllerService.isPostAlreadyLikedByUser(id, userId)) {
			
			try {
				this.likeControllerService.unLikePost(postToUnlike);
				
				this.LOGGER.info("UNLIKE_POST: CommentEntity with id:" + id + " has been liked by: " +
						user.getFirstName() + " " + user.getLastName());
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (final RuntimeException e) {
				
				this.LOGGER.error("UNLIKE_POST: Problem founc when trying to unlike post id:" + id, e);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		} else {
			this.LOGGER
					.error("UNLIKE_POST: PostEntity NOT liked by: " + user.getFirstName() + " " + user.getLastName());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
