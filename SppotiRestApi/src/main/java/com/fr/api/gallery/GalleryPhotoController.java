package com.fr.api.gallery;

import com.fr.commons.dto.post.PostDTO;
import com.fr.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by djenanewail on 12/24/16.
 */

@RestController
@RequestMapping("/gallery/photo/{userId}")
class GalleryPhotoController
{
	
	/** Post service. */
	private PostControllerService postControllerService;
	
	/** Init post service. */
	@Autowired
	void setPostControllerService(PostControllerService postControllerService)
	{
		this.postControllerService = postControllerService;
	}
	
	/**
	 * @param page
	 * 		page number.
	 *
	 * @return List of all photos posted by a user.
	 */
	@GetMapping(value = "/{page}")
	ResponseEntity<List<PostDTO>> photoGallery(@PathVariable int page, @PathVariable int userId)
	{
		
		return new ResponseEntity<>(postControllerService.getPhotoGallery(userId, page), HttpStatus.OK);
		
	}
	
}