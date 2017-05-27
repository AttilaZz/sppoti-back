/**
 *
 */
package com.fr.api.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by: Wail DJENANE on Nov 13, 2016
 */

@Controller
class WebSocketController
{
	
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(final HelloMessage message) throws Exception
	{
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + message.getName() + "!");
	}
	
	//	@MessageMapping("/notification")
	//	@SendToUser("/queue/position-updates")
	//	HelloMessage executeTrade(final HelloMessage message, final Principal principal) throws InterruptedException
	//	{
	//		// ...
	//		Thread.sleep(1000);
	//		return message;
	//
	//
	//	}
	
	private static class Greeting
	{
		
		private String content;
		
		public Greeting()
		{
		}
		
		public Greeting(final String content)
		{
			this.content = content;
		}
		
		public String getContent()
		{
			return this.content;
		}
		
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
