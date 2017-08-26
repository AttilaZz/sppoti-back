package com.fr.api.post.like;

import com.fr.service.LikeBusinessService;
import com.fr.versionning.ApiVersion;
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
@ApiVersion("1")
class UnlikePostController
{
	
	/** Like service. */
	private final LikeBusinessService likeControllerService;
	
	/** Init services. */
	@Autowired
	public UnlikePostController(final LikeBusinessService likeControllerService)
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
	ResponseEntity<Void> unLikePost(@PathVariable("id") final String id)
	{
		
		this.likeControllerService.unLikePost(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
