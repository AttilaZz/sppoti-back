package com.fr.api.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.service.CommentBusinessService;
import com.fr.versionning.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping("/comment")
@ApiVersion("1")
class CommentAddController
{
	private final Logger LOGGER = LoggerFactory.getLogger(CommentAddController.class);
	
	private CommentBusinessService commentDataService;
	
	@Autowired
	void setCommentDataService(final CommentBusinessService commentDataService)
	{
		this.commentDataService = commentDataService;
	}
	
	@PostMapping
	ResponseEntity<CommentDTO> addComment(@RequestBody final CommentDTO newComment)
	{
		this.LOGGER.info("Request sent to add comment: {}", newComment);
		
		final CommentDTO savedComment = this.commentDataService.saveComment(newComment);
		
		return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
	}
	
}
