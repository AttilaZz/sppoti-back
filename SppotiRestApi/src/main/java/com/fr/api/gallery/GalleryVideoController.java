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
 * Created by wdjenane on 22/02/2017.
 */
@RestController
@RequestMapping("/gallery/video/{userId}")
@ApiVersion("1")
class GalleryVideoController
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
	 * @return all videos posted by user.
	 */
	@GetMapping(value = "/{page}")
	ResponseEntity<List<PostDTO>> videoGallery(@PathVariable final int page, @PathVariable final int userId)
	{
		
		return new ResponseEntity<>(this.postControllerService.getVideoGallery(userId, page), HttpStatus.OK);
		
	}
}
