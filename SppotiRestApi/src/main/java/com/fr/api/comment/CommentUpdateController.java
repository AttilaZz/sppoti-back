package com.fr.api.comment;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistoryEntity;
import com.fr.service.CommentControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping("/comment")
@ApiVersion("1")
class CommentUpdateController
{
	/** Comment service. */
	private CommentControllerService commentDataService;
	
	/** Init comment service. */
	@Autowired
	void setCommentDataService(final CommentControllerService commentDataService)
	{
		this.commentDataService = commentDataService;
	}
	
	@PutMapping(value = "/{id}")
	ResponseEntity<CommentDTO> updateComment(@PathVariable final int id, @RequestBody final CommentDTO newComment)
	{
		
		final CommentEntity commentEntityToEdit = this.commentDataService.findComment(id);
		
		final EditHistoryEntity commentEditRow = new EditHistoryEntity();
		final ContentEditedResponseDTO edit = new ContentEditedResponseDTO();
		
		// Required attributes
		if (commentEntityToEdit == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		commentEditRow.setComment(commentEntityToEdit);
		
		// test the received attributes content
		if (newComment.getText() != null && newComment.getText().trim().length() > 0) {
			commentEditRow.setText(newComment.getText());
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// if all argument are correctly assigned - edit post
		if (this.commentDataService.updateComment(commentEditRow)) {
			
			edit.setId(commentEntityToEdit.getId());
			edit.setDateTime(commentEditRow.getDatetimeEdited());
			edit.setText(commentEditRow.getText());
			return new ResponseEntity<>(newComment, HttpStatus.ACCEPTED);
			
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
}
