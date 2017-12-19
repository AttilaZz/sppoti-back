package com.fr.api.post.like;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
import com.fr.service.LikeBusinessService;
import com.fr.service.PostBusinessService;
import com.fr.versionning.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ApiVersion("1")
class LikePostController
{
	private final Logger LOGGER = LoggerFactory.getLogger(LikePostController.class);
	
	@Autowired
	private PostBusinessService postDataService;
	
	@Autowired
	private LikeBusinessService likeControllerService;
	
	@PutMapping(value = "/post/{postId}")
	ResponseEntity<Void> likePost(@PathVariable final String postId, final Authentication authentication)
	{
		this.LOGGER.info("Request sent to like post: {}", postId);
		
		final PostEntity postToLike = this.postDataService.findPost(postId);
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		final UserEntity user = this.likeControllerService.getUserById(userId);
		
		if (postToLike == null || user == null) {
			
			if (postToLike == null) {
				this.LOGGER.info("LIKE_POST: Failed to retrieve the post");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				this.LOGGER.info("LIKE_POST: trying to like a non existing post");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
		
		final LikeContentEntity likeToSave = new LikeContentEntity();
		likeToSave.setPost(postToLike);
		likeToSave.setUser(user);
		
		if (!this.likeControllerService.isPostAlreadyLikedByUser(postId, userId)) {
			try {
				this.likeControllerService.likePost(likeToSave);
				
				this.LOGGER.info("LIKE_POST: PostEntity with postId:{} has been liked by: {} {}", postId,
						user.getFirstName(), user.getLastName());
				return new ResponseEntity<>(HttpStatus.OK);
				
			} catch (final Exception e) {
				this.LOGGER.error("LIKE_POST: Database like problem !!", e);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}
		
		this.LOGGER.error("LIKE_POST: PostEntity already liked by: " + user.getFirstName() + " " + user.getLastName());
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	
	
}
