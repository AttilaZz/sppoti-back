package com.fr.impl;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.repositories.AccountParamRepository;
import com.fr.service.UserParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 11/10/17.
 */
@Component
public class UserParamServiceImpl implements UserParamService
{
	@Autowired
	private AccountParamRepository accountParamRepository;
	
	@Override
	public boolean canReceiveEmail(final String userId) {
		return this.accountParamRepository.findByUserUuidAndCanReceiveEmailTrue(userId).isPresent();
	}
	
	@Override
	public boolean canReceiveNotification(final String userId) {
		return this.accountParamRepository.findByUserUuidAndCanReceiveNotificationTrue(userId).isPresent();
	}
	
	private AccountUserDetails getConnectedUserInfo() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return (AccountUserDetails) authentication.getPrincipal();
	}
	
}
