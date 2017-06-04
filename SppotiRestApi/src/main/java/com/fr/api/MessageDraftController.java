/**
 *
 */
package com.fr.api;

import com.fr.entities.MessageEntity;
import com.fr.service.MessageControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@RestController
@RequestMapping("/message/draft")
@ApiVersion("1")
class MessageDraftController
{
	
	@Autowired
	private MessageControllerService messageControllerService;
	
	@PutMapping(value = "/update/{id}")
	ResponseEntity<MessageEntity> updateUser(@PathVariable("id") final Long id, @RequestBody final MessageEntity newMsg)
	{
		
		if (id > 0) {
			final MessageEntity oldMsg = this.messageControllerService.findMessageById(id);
			if (oldMsg != null) {
				final MessageEntity updated = new MessageEntity();
				// prepare data for update
				if (newMsg.getContent() != null) {
					updated.setContent(newMsg.getContent());
				}
				if (newMsg.getObject() != null) {
					updated.setObject(newMsg.getObject());
				}
				
				// if (messageControllerService.updateMessage(updated)) {
				// LOGGER.info("UPDATE: success");
				// return new ResponseEntity<MessageEntity>(updated, HttpStatus.OK);
				// }
				
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		// BAD MESSAGE ID
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
