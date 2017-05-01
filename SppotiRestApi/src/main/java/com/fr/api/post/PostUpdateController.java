package com.fr.api.post;

import com.fr.aop.TraceAuthentification;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.entities.AddressEntity;
import com.fr.entities.EditHistoryEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.SportEntity;
import com.fr.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by djenanewail on 4/9/17.
 */

@RestController
@RequestMapping("/post")
public class PostUpdateController
{
	
	/**
	 * Post controller service.
	 */
	private final PostControllerService postDataService;
	
	/**
	 * Class logger.
	 */
	private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);
	
	/**
	 * Init services.
	 */
	@Autowired
	public PostUpdateController(PostControllerService postDataService)
	{
		this.postDataService = postDataService;
	}
	
	/**
	 * Edit post visibility.
	 */
	@PutMapping(value = "/{postId}/{visibility}")
	ResponseEntity<Void> editVisibility(@PathVariable int id, @PathVariable int visibility)
	{
		
		try {
			postDataService.editPostVisibility(id, visibility);
		} catch (EntityNotFoundException e) {
			LOGGER.info("POST_EDIT_VISIBILITY: Can't update visibility!!", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		LOGGER.info("POST_EDIT_VISIBILITY: PostEntity VISIBILITY has been changed for postId: " + id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * Update post information.
	 */
	@PutMapping(value = "/{postId}")
	ResponseEntity<ContentEditedResponseDTO> updatePost(@PathVariable("postId") int postId,
														@RequestBody ContentEditedResponseDTO newData)
	{
		
		PostEntity postToEdit = postDataService.findPost(postId);
		
		List<EditHistoryEntity> lastPostEditList = postDataService.getLastModification(postId);
		EditHistoryEntity lastPostEdit = null;
		boolean isAlreadyEdited = !lastPostEditList.isEmpty();
		
		EditHistoryEntity postEditRow = new EditHistoryEntity();
		SortedSet<AddressEntity> postEditAddress = null;
		
		// Required attributes
		if (postToEdit == null) {
			LOGGER.info("POST_UPDATE: Failed to retreive the post");
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
			SportEntity sp = postDataService.getSportById(newData.getSportId());
			if (sp != null) {
				postEditRow.setSport(sp);
				
				if (isAlreadyEdited) {
					postEditRow.setText(lastPostEdit.getText());
				}
				
			} else {
				LOGGER.info("POST_UPDATE: Failed to retrieve the SportDTO to update");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			LOGGER.info("POST_UPDATE: No much found for the arguments sent");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// if all arguments are correctly assigned - edit post
		if (postDataService.updatePost(postEditRow, postEditAddress, postId)) {
			LOGGER.info("POST_UPDATE: success");
			
			ContentEditedResponseDTO edit = new ContentEditedResponseDTO();
			edit.setId(postToEdit.getId());
			edit.setDateTime(postEditRow.getDatetimeEdited());
			edit.setText(postEditRow.getText());
			
			edit.setLatitude(newData.getLatitude());
			edit.setLongitude(newData.getLongitude());
			
			return new ResponseEntity<>(edit, HttpStatus.ACCEPTED);
		} else {
			LOGGER.info("POST_UPDATE: Failed when trying to update the post in DB");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
	}
}
