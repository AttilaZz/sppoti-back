/**
 *
 */
package com.fr.security;

import com.fr.entities.UserEntity;
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
	int getUuid();
	
	/**
	 * Get user account details.
	 *
	 * @return user details.
	 */
	UserEntity getConnectedUserDetails();
	
	/**
	 * Get user timezone.
	 *
	 * @return timezone.
	 */
	String getTimeZone();
}
