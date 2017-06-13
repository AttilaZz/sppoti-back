package com.fr.api.gallery;

import com.fr.commons.dto.post.PostDTO;
import com.fr.service.PostControllerService;
import com.fr.versionning.ApiVersion;
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
@ApiVersion("1")
class GalleryPhotoController
{
	
	/** Post service. */
	private PostControllerService postControllerService;
	
	/** Init post service. */
	@Autowired
	void setPostControllerService(final PostControllerService postControllerService)
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
	ResponseEntity<List<PostDTO>> photoGallery(@PathVariable final int page, @PathVariable final String userId)
	{
		
		return new ResponseEntity<>(this.postControllerService.getPhotoGallery(userId, page), HttpStatus.OK);
		
	}
	
}