package com.fr.api.post;

import com.fr.service.PostBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 3/19/17.
 */

@RestController
@RequestMapping("/post")
@ApiVersion("1")
class PostDeleteController
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
	 * @param postId
	 * 		post postId.
	 *
	 * @return 200 status if post deleted.
	 */
	@DeleteMapping(value = "/{postId}")
	ResponseEntity<Void> deletePost(@PathVariable final String postId)
	{
		
		this.postDataService.deletePost(postId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
