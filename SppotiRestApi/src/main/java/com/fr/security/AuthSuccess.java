package com.fr.security;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.UserEntity;
import com.fr.repositories.UserRepository;
import com.fr.transformers.UserTransformer;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
	
	/** Class logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthSuccess.class);
	
	/** USer repository. */
	private final UserRepository userRepository;
	
	/** User transformer. */
	private final UserTransformer userTransformer;
	
	/** Init repository. */
	@Autowired
	public AuthSuccess(final UserRepository userRepository, final UserTransformer userTransformer)
	{
		this.userRepository = userRepository;
		this.userTransformer = userTransformer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
										final Authentication authentication) throws IOException, ServletException
	{
		
		response.setHeader(ATTR_ORIGIN.getValue(), Origins.getValue());
		response.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
		response.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
		response.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
		response.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		//Get connected user data.
		final UserEntity users = this.userRepository.getByIdAndDeletedFalseAndConfirmedTrue(accountUserDetails.getId());
		final UserDTO user = this.userTransformer.modelToDto(users);
		user.setPassword(null);
		
		//Convert data to json.
		final Gson gson = new Gson();
		response.getWriter().write(gson.toJson(user));
		
		//Return OK status.
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
}
