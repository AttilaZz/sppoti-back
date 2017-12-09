/**
 *
 */
package com.fr.commons.dto.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by: Wail DJENANE on Jul 2, 2016
 */
public interface MyUserDetails extends UserDetails
{
	Long getId();
	
	String getUuid();
	
	String getTimeZone();
	
	boolean hasActivatedEmail();
	
	boolean hasActivatedNotifications();
	
	String getFirebaseToken();
}
