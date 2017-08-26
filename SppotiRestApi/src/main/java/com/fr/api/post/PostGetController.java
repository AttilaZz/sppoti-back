package com.fr.api.post;

import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.service.PostBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@RestController
@RequestMapping("/post")
@ApiVersion("1")
class PostGetController
{
	
	/** Post controller service. */
	private PostBusinessService postDataService;
	
	/** Init services. */
	@Autowired
	void setPostDataService(final PostBusinessService postDataService)
	{
		this.postDataService = postDataService;
	}
	
	/** Get post details. */
	@GetMapping(value = "/{postId}")
	ResponseEntity<PostDTO> detailsPost(@PathVariable final String postId, final Authentication authentication)
	{
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		final PostDTO prep = this.postDataService.fillPostToSend(postId, userId);
		return new ResponseEntity<>(prep, HttpStatus.OK);
		
	}
	
	/**
	 * Get al posts for a given user id.
	 */
	@GetMapping(value = "/all/{userId}/{page}")
	ResponseEntity<List<PostDTO>> getAllPosts(@PathVariable final String userId, @PathVariable final int page,
											  final Authentication authentication)
	{
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		return new ResponseEntity<>(this.postDataService
				.getAllUserPosts(accountUserDetails.getId(), accountUserDetails.getUuid(), userId, page),
				HttpStatus.OK);
		
	}
	
	/**
	 * List of all edition on a post.
	 */
	@GetMapping(value = "/history/{postId}/{page}")
	ResponseEntity<List<ContentEditedResponseDTO>> getPostHistory(@PathVariable final String postId,
																  @PathVariable    final int page)
	{
		
		final List<ContentEditedResponseDTO> contentEditedResponseDTOs = this.postDataService
				.getAllPostHistory(postId, page);
		
		return new ResponseEntity<>(contentEditedResponseDTOs, HttpStatus.OK);
		
	}
	
	/**
	 * Get all friend posts
	 */
	@GetMapping("/all/timeline/{userId}/{page}")
	ResponseEntity<List<PostDTO>> getAllFriendPosts(@PathVariable final String userId, @PathVariable final int page,
													final Authentication authentication)
	{
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		return new ResponseEntity<>(this.postDataService.getAllTimelinePosts(userId, page, accountUserDetails.getId()),
				HttpStatus.OK);
	}
}
