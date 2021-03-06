package com.fr.api.post;

import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.entities.AddressEntity;
import com.fr.entities.EditHistoryEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.SportEntity;
import com.fr.service.PostBusinessService;
import com.fr.versionning.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.SortedSet;

/**
 * Created by djenanewail on 4/9/17.
 */

@RestController
@RequestMapping("/post")
@ApiVersion("1")
public class PostUpdateController
{
	private final Logger LOGGER = LoggerFactory.getLogger(PostUpdateController.class);
	
	private final PostBusinessService postDataService;
	
	@Autowired
	public PostUpdateController(final PostBusinessService postDataService)
	{
		this.postDataService = postDataService;
	}
	
	@PutMapping(value = "/{postId}/{visibility}")
	ResponseEntity<Void> editVisibility(@PathVariable final String id, @PathVariable final int visibility)
	{
		this.LOGGER.info("Request sent to update visibility of the post={} to {}", id, visibility);
		this.postDataService.editPostVisibility(id, visibility);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	@PutMapping(value = "/{postId}")
	ResponseEntity<ContentEditedResponseDTO> updatePost(@PathVariable("postId") final String postId,
														@RequestBody final ContentEditedResponseDTO newData)
	{
		this.LOGGER.info("Request sent to update post ID={}", postId);
		final PostEntity postToEdit = this.postDataService.findPost(postId);
		
		final List<EditHistoryEntity> lastPostEditList = this.postDataService.getLastModification(postId);
		EditHistoryEntity lastPostEdit = null;
		final boolean isAlreadyEdited = !lastPostEditList.isEmpty();
		
		final EditHistoryEntity postEditRow = new EditHistoryEntity();
		final SortedSet<AddressEntity> postEditAddress = null;
		
		// Required attributes
		if (postToEdit == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		postEditRow.setPost(postToEdit);
		
		// if post has been edited before, get the latest entry
		if (isAlreadyEdited) {
			lastPostEdit = lastPostEditList.get(0);
		}
		
		// test the received attributes content
		/*
		 * If post content has been edited -> New row in EDITHISTORY is created
		 * Otherwise: we just add a new row in address table related to the
		 * targeted post
		 */
		if (newData.getText() != null && newData.getText().trim().length() > 0) {
			
			postEditRow.setText(newData.getText());

            /*
			 related SportDTO can be modified
             */
			if (isAlreadyEdited) {
				postEditRow.setSport(lastPostEdit.getSport());
			} else {
				postEditRow.setSport(postToEdit.getSport());
			}
			
		} else if (newData.getSportId() != null) {
			// SportDTO modification
			final SportEntity sp = this.postDataService.getSportById(newData.getSportId());
			if (sp != null) {
				postEditRow.setSport(sp);
				
				if (isAlreadyEdited) {
					postEditRow.setText(lastPostEdit.getText());
				}
				
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// if all arguments are correctly assigned - edit post
		this.postDataService.updatePost(postEditRow, postEditAddress, postId);
		
		final ContentEditedResponseDTO postResponse = new ContentEditedResponseDTO();
		postResponse.setId(postToEdit.getId());
		postResponse.setDateTime(postEditRow.getDatetimeEdited());
		postResponse.setText(postEditRow.getText());
		
		postResponse.setLatitude(newData.getLatitude());
		postResponse.setLongitude(newData.getLongitude());
		
		return new ResponseEntity<>(postResponse, HttpStatus.ACCEPTED);
	}
}
