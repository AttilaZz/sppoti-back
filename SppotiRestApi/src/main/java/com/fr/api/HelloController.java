package com.fr.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Wail DJENANE on 26-Nov-16.
 */
@RestController
class HelloController
{
	
	@RequestMapping("/hello/{name}")
	String hello(@PathVariable final String name)
	{
		return "Hello, " + name + "!";
	}
	
}
