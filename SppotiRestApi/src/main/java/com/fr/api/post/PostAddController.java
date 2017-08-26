package com.fr.api.post;

import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.dto.post.PostRequestDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.PostBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Created by djenanewail on 2/13/17.
 */

@RestController
@RequestMapping("/post")
@ApiVersion("1")
class PostAddController
{
	
	/** Post controller service. */
	private PostBusinessService postDataService;
	
	/** Init post service. */
	@Autowired
	void setPostDataService(final PostBusinessService postDataService)
	{
		this.postDataService = postDataService;
	}
	
	/**
	 * @param postDto
	 * 		post data to save.
	 *
	 * @return Add post by user.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping
	ResponseEntity<PostDTO> addPost(@RequestBody final PostRequestDTO postDto)
	{
		//		final SppotiEntity game;
		//		final Long gameId;
		
		// SportEntity is required
		if (postDto.getSportId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// if post is about a game
		//		if (postDto.getGameId() != null) {
		//
		//			gameId = postDto.getGameId();
		//			game = this.postDataService.getSppotiById(gameId);
		//
		//			if (game == null) {
		//				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		//			}
		//		}
		
		if (postDto.getContent() == null ||
				(postDto.getContent() == null && postDto.getContent() == null && postDto.getContent() == null)) {
			throw new BusinessGlobalException("Missing content to add post");
		}
		
		final String content = postDto.getContent().getContent();
		final Set<String> image = postDto.getContent().getImageLink();
		final String video = postDto.getContent().getVideoLink();
		
		if (image != null && video != null) {
			throw new BusinessGlobalException("You can't post image and video in same time");
		}
		
		if (content != null) {
			if (content.trim().length() == 0) {
				throw new BusinessGlobalException("Content text is empty !!");
			}
		}
		
		if (image != null) {
			if (image.size() <= 0) {
				throw new BusinessGlobalException("Image link is empty !!");
			}
		}
		
		if (video != null) {
			if (video.trim().length() <= 0) {
				throw new BusinessGlobalException("Video link is empty !!");
			}
		}
		
		return new ResponseEntity<>(this.postDataService.savePost(postDto), HttpStatus.CREATED);
		
	}
	
}
