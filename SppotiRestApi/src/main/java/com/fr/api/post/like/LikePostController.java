package com.fr.api.post.like;

import com.fr.entities.LikeContentEntity;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
class LikePostController
{
	
	/** Post service. */
	private final PostControllerService postDataService;
	
	/** like service. */
	private final LikeControllerService likeControllerService;
	
	/** Init services. */
	@Autowired
	public LikePostController(final PostControllerService postDataService,
							  final LikeControllerService likeControllerService)
	{
		this.postDataService = postDataService;
		this.likeControllerService = likeControllerService;
	}
	
	/** Init service. */
	
	private final Logger LOGGER = Logger.getLogger(LikePostController.class);
	
	/**
	 * @param id
	 * 		post id.
	 * @param authentication
	 * 		spring auth.
	 *
	 * @return Like post
	 */
	@PutMapping(value = "/post/{id}")
	ResponseEntity<Void> likePost(@PathVariable("id") final int id, final Authentication authentication)
	{
		
		final PostEntity postToLike = this.postDataService.findPost(id);
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		final UserEntity user = this.likeControllerService.getUserById(userId);
		
		if (postToLike == null || user == null) {
			
			if (postToLike == null) {
				// post not found
				this.LOGGER.info("LIKE_POST: Failed to retreive the post");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				this.LOGGER.info("LIKE_POST: trying to like a non existing post");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
		
		final LikeContentEntity likeToSave = new LikeContentEntity();
		likeToSave.setPost(postToLike);
		likeToSave.setUser(user);
		
		if (!this.likeControllerService.isPostAlreadyLikedByUser(id, userId)) {
			try {
				this.likeControllerService.likePost(likeToSave);
				
				this.LOGGER.info("LIKE_POST: PostEntity with id:" + id + " has been liked by: " + user.getFirstName() +
						" " + user.getLastName());
				return new ResponseEntity<>(HttpStatus.OK);
				
			} catch (final Exception e) {
				this.LOGGER.error("LIKE_POST: Database like problem !!", e);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		} else {
			this.LOGGER
					.error("LIKE_POST: PostEntity already liked by: " + user.getFirstName() + " " + user.getLastName());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}
