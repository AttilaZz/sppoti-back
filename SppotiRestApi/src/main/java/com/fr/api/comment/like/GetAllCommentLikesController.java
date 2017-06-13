package com.fr.api.comment.like;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.entities.CommentEntity;
import com.fr.service.CommentControllerService;
import com.fr.service.LikeControllerService;
import com.fr.versionning.ApiVersion;
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
@ApiVersion("1")
class GetAllCommentLikesController
{
	
	/** Comment service. */
	private final CommentControllerService commentControllerService;
	/** Like service. */
	private final LikeControllerService likeControllerService;
	
	/** Init services. */
	GetAllCommentLikesController(final CommentControllerService commentControllerService,
								 final LikeControllerService likeControllerService)
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
	ResponseEntity<PostDTO> getCommentLikers(@PathVariable("id") final String id, @PathVariable("page") final int page)
	{
		
		final CommentEntity currentCommentEntity = this.commentControllerService.findComment(id);
		
		if (currentCommentEntity == null) {
			// post not fount
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final List<UserDTO> likersList = this.likeControllerService.getCommentLikersList(id, page);
		
		final PostDTO pr = new PostDTO();
		pr.setLikers(likersList);
		pr.setLikeCount(currentCommentEntity.getLikes().size());
		
		return new ResponseEntity<>(pr, HttpStatus.OK);
	}
}
