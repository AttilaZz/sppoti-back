package com.fr.api.post.like;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.entities.PostEntity;
import com.fr.service.LikeControllerService;
import com.fr.service.PostControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by djenanewail on 2/19/17.
 */

@RestController
@RequestMapping("/like")
class GetAllPostLikersController
{
	
	/**
	 * Posts Controller service.
	 */
	private final PostControllerService postDataService;
	/**
	 * Likes Controller service.
	 */
	private final LikeControllerService likeControllerService;
	/**
	 * Class logger.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(GetAllPostLikersController.class);
	
	/**
	 * Init services.
	 */
	@Autowired
	public GetAllPostLikersController(final PostControllerService postDataService,
									  final LikeControllerService likeControllerService)
	{
		this.postDataService = postDataService;
		this.likeControllerService = likeControllerService;
	}
	
	/**
	 * @param id
	 * 		post id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of people who liked the post.
	 */
	@GetMapping(value = "/post/{id}/{page}")
	ResponseEntity<PostDTO> getPostLikers(@PathVariable("id") final int id, @PathVariable("page") final int page)
	{
		
		final PostEntity currentPost = this.postDataService.findPost(id);
		
		if (currentPost == null) {
			// post not fount
			this.LOGGER.info("POST_LIKERS_LIST: Failed to retreive the post id" + id);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final List<UserDTO> likersList = this.likeControllerService.getPostLikersList(id, page);
		
		final PostDTO pr = new PostDTO();
		pr.setLikers(likersList);
		pr.setLikeCount(currentPost.getLikes().size());
		
		return new ResponseEntity<>(pr, HttpStatus.OK);
	}
	
	
}
