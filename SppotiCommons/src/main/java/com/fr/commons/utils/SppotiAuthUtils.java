package com.fr.commons.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 6/10/17.
 */
public class SppotiAuthUtils
{
	/**
	 * Return login type depending on the usernalme used to log in.
	 * 1- TEXT
	 * 2- EMAIL
	 * 3- PHONE
	 *
	 * @param username
	 * 		username used to login.
	 *
	 * @return login type
	 */
	public static int getUserLoginType(final String username)
	{
		final String numberRegex = "[0-9]+";
		final int loginType;
		
		if (username.contains("@")) {
			loginType = 2;
		} else if (username.matches(numberRegex)) {
			loginType = 3;
		} else {
			loginType = 1;
		}
		
		return loginType;
	}
	
	/**
	 * Get username used to authenticate.
	 *
	 * @return auth username.
	 */
	public static String getAuthenticationUsername()
	{
		final String userName;
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
	
	/**
	 * Get authenticated user roles.
	 *
	 * @return list of roles.
	 */
	public List<String> getUserRoles()
	{
		
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final ArrayList<GrantedAuthority> roles;
		
		final List<String> userRoles = new ArrayList<String>();
		
		if (principal instanceof UserDetails) {
			roles = (ArrayList<GrantedAuthority>) ((UserDetails) principal).getAuthorities();
			for (final GrantedAuthority role : roles) {
				userRoles.add(role.getAuthority());
			}
		}
		return userRoles;
	}
}
