package com.fr.api.comment.like;

import com.fr.commons.dto.HeaderDataDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.entities.CommentEntity;
import com.fr.service.CommentControllerService;
import com.fr.service.LikeControllerService;
import org.apache.log4j.Logger;
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
class GetAllCommentLikesController
{
	
	/** Comment service. */
	private final CommentControllerService commentControllerService;
	/** Like service. */
	private final LikeControllerService likeControllerService;
	
	/** Init services. */
	GetAllCommentLikesController(CommentControllerService commentControllerService,
								 LikeControllerService likeControllerService)
	{
		this.commentControllerService = commentControllerService;
		this.likeControllerService = likeControllerService;
	}
	
	
	/**
	 * @param id
	 * 		comment id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of likers for a comment
	 */
	@GetMapping(value = "/comment/{id}/{page}")
	ResponseEntity<PostDTO> getCommentLikers(@PathVariable("id") int id, @PathVariable("page") int page)
	{
		
		CommentEntity currentCommentEntity = commentControllerService.findComment(id);
		
		if (currentCommentEntity == null) {
			// post not fount
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<HeaderDataDTO> likersList = likeControllerService.getCommentLikersList(id, page);
		
		if (likersList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		PostDTO pr = new PostDTO();
		pr.setLikers(likersList);
		pr.setLikeCount(currentCommentEntity.getLikes().size());
		
		return new ResponseEntity<>(pr, HttpStatus.OK);
	}
}
