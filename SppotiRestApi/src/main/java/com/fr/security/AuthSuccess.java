package com.fr.security;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.UserEntity;
import com.fr.repositories.UserRepository;
import com.fr.service.LoginBusinessService;
import com.fr.transformers.UserTransformer;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.fr.filter.HeadersAttributes.*;
import static com.fr.filter.HeadersValues.*;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
public class AuthSuccess extends SimpleUrlAuthenticationSuccessHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountUserDetails.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserTransformer userTransformer;
	
	@Autowired
	private LoginBusinessService loginService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
										final Authentication authentication) throws IOException, ServletException
	{
		final String[] allowedHeaders = Origins.getValue().split(",");
		
		for (final String allowedHeader : allowedHeaders) {
			if (allowedHeader.equals(request.getHeader("origin"))) {
				response.setHeader(ATTR_ORIGIN.getValue(), request.getHeader("origin"));
			}
		}
		
		response.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
		response.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
		response.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
		response.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		//Get connected user data.
		final UserEntity user = this.userRepository.getByIdAndDeletedFalseAndConfirmedTrue(accountUserDetails.getId());
		final UserDTO userDTO = this.userTransformer.modelToDto(user);
		userDTO.setPassword(null);
		
		final Gson gson = new Gson();
		response.getWriter().write(gson.toJson(userDTO));
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		final String firebaseToken = getUserFirebaseToken();
		if (StringUtils.hasText(firebaseToken)) {
			LOGGER.info("Firebase key sent with login is: {}", firebaseToken);
			this.loginService.updateUserDeviceToConnectedStatus(getUserFirebaseToken(), getConnectedUserEmail());
		}
	}
	
	private String getUserFirebaseToken() {
		final AccountUserDetails accountUserDetails = getAccountUserDetails();
		return accountUserDetails.getFirebaseToken();
	}
	
	private AccountUserDetails getAccountUserDetails() {
		return (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	private String getConnectedUserEmail() {
		final AccountUserDetails accountUserDetails = getAccountUserDetails();
		return accountUserDetails.getUsername();
	}
}