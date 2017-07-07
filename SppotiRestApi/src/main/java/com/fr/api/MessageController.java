package com.fr.api;

import com.fr.commons.dto.message.MessageRequestDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.MessageEntity;
import com.fr.entities.UserEntity;
import com.fr.service.MessageControllerService;
import com.fr.transformers.MessageTransformer;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */
@RestController
@RequestMapping("/message")
@ApiVersion("1")
class MessageController
{
	/** message service. */
	private final MessageControllerService messageControllerService;
	
	/** Message transformer. */
	@Autowired
	private MessageTransformer messageTransformer;
	
	/** Init class. */
	@Autowired
	MessageController(final MessageControllerService messageControllerService)
	{
		this.messageControllerService = messageControllerService;
	}
	
	@GetMapping(value = "/sent/{bottomMajId}")
	ResponseEntity<MessageRequestDTO> getSentMessages(@PathVariable("bottomMajId") final int bottomMajId,
													  final Authentication authentication)
	{
		
		final MessageRequestDTO response = new MessageRequestDTO();
		
		// page number is not valid
		if (bottomMajId < 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final List<MessageEntity> sentMessages = this.messageControllerService.getSentUserMessages(userId, bottomMajId);
		
		if (sentMessages.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		//		response.setSentMessages(sentMessages);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/received/{bottomMajId}")
	ResponseEntity<MessageRequestDTO> getReceivedMessages(@PathVariable("bottomMajId") final int bottomMajId,
														  final Authentication authentication)
	{
		
		final MessageRequestDTO response = new MessageRequestDTO();
		
		// page number is not valid
		if (bottomMajId < 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		final List<MessageEntity> receivedMessages = this.messageControllerService
				.getReceivedUserMessages(userId, bottomMajId);
		
		//		response.setReceivedMessages(receivedMessages);
		
		if (receivedMessages.isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/send")
	ResponseEntity<MessageEntity> addPost(@RequestBody final MessageRequestDTO newMessage,
										  final Authentication authentication)
	{
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final UserEntity user = this.messageControllerService.getUserById(userId);
		
		final MessageEntity newMsg;
		
		if (newMessage.getMsg() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		newMsg = new MessageEntity(this.messageTransformer.dtoToModel(newMessage.getMsg()));
		newMsg.setUserMessage(user);
		
		if (newMessage.getReceivedId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		newMsg.setReceiverId(newMessage.getReceivedId());
		
		if (this.messageControllerService.saveMessage(newMsg)) {
			return new ResponseEntity<>(newMsg, HttpStatus.CREATED);
		}
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	ResponseEntity<Void> deleteUser(@PathVariable("id") final Long id)
	{
		
		// bad argument parameter
		if (id < 1) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// nothing to delete
		if (this.messageControllerService.findMessageById(id) == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		if (this.messageControllerService.deteleMessageById(id)) {
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		// database problem
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}
}
