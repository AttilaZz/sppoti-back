package com.fr.security;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.UserEntity;
import com.fr.repositories.UserRepository;
import com.fr.transformers.UserTransformer;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
		
		if (!user.isFirstConnexion()) {
			user.setFirstConnexion(true);
			this.userRepository.save(user);
		}
		
		final Gson gson = new Gson();
		response.getWriter().write(gson.toJson(userDTO));
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
}