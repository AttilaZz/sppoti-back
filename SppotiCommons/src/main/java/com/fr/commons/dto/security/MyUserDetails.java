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
	/**
	 * Get user technical id.
	 *
	 * @return user id.
	 */
	Long getId();
	
	/**
	 * Get user unique id.
	 *
	 * @return user unique id.
	 */
	String getUuid();
	
	/**
	 * Get user timezone.
	 *
	 * @return timezone.
	 */
	String getTimeZone();
	
	/**
	 * @return true if user can receive emails, false otherwise.
	 */
	boolean hasActivatedEmails();
	
	/**
	 * @return true if user can receive notifications, false otherwise.
	 */
	boolean hasActivatedNotifications();
}
