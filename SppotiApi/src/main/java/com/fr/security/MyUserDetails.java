/**
 *
 */
package com.fr.security;

import com.fr.entities.Friend;
import com.fr.entities.Users;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by: Wail DJENANE on Jul 2, 2016
 */
public interface MyUserDetails extends UserDetails {

    public Long getId();

    Friend getUserAsFriend();

    Users getConnectedUserDetails();
}
