/**
 *
 */
package com.fr.security;

import com.fr.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by: Wail DJENANE on Jul 2, 2016
 */
public interface MyUserDetails extends UserDetails {

    Long getId();

    int getUuid();

    UserEntity getConnectedUserDetails();
}