package com.fr.api.post.like;

import com.fr.service.LikeControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	/** Like service. */
	private final LikeControllerService likeControllerService;
	
	/** Init services. */
	@Autowired
	public UnlikePostController(final LikeControllerService likeControllerService)
	{
		this.likeControllerService = likeControllerService;
	}
	
	/**
	 * @param id
	 * 		post id.
	 *
	 * @return unlike post
	 */
	@DeleteMapping(value = "/post/{id}")
	ResponseEntity<Void> unLikePost(@PathVariable("id") final int id)
	{
		
		this.likeControllerService.unLikePost(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
