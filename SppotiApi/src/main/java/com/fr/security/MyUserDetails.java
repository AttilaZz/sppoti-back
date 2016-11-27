/**
 * 
 */
package com.fr.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by: Wail DJENANE on Jul 2, 2016
 */
public interface MyUserDetails extends UserDetails{

	public Long getId();
	
}
