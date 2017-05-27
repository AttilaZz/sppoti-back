package com.fr.api.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 5/26/17.
 */
@RestController
public class SocketTestController
{
	private final SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	SocketTestController(final SimpMessagingTemplate messagingTemplate)
	{
		this.messagingTemplate = messagingTemplate;
	}
	
	
	@PostMapping(value = "/send")
	ResponseEntity getSentMessages(@RequestBody final HelloMessage message)
	{
		this.messagingTemplate.convertAndSendToUser(message.getEmail(), "/queue/notify", message);
		return ResponseEntity.ok().build();
	}
	
	private static class HelloMessage
	{
		
		private String name;
		private String email;
		
		public String getName()
		{
			return this.name;
		}
		
		public void setName(final String name)
		{
			this.name = name;
		}
		
		public String getEmail() {
			return this.email;
		}
		
		public void setEmail(final String email) {
			this.email = email;
		}
	}
}