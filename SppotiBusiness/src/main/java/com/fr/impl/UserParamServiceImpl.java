package com.fr.impl;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.service.UserParamService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 11/10/17.
 */
@Component
public class UserParamServiceImpl implements UserParamService
{
	@Override
	public boolean canReceiveEmail() {
		return getConnectedUserInfo().hasActivatedEmails();
	}
	
	@Override
	public boolean canReceiveNotification() {
		return getConnectedUserInfo().hasActivatedNotifications();
	}
	
	private AccountUserDetails getConnectedUserInfo() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return (AccountUserDetails) authentication.getPrincipal();
	}
	
}
