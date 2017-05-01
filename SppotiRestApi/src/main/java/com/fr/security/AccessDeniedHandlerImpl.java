/**
 *
 */
package com.fr.security;

import com.fr.filter.HeadersAttributes;
import com.fr.filter.HeadersValues;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by: Wail DJENANE on Jun 19, 2016
 */

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler
{
	
	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response,
					   final AccessDeniedException accessDeniedException) throws IOException, ServletException
	{
		
		response.setHeader(HeadersAttributes.ATTR_ORIGIN.getValue(), HeadersValues.Origins.getValue());
		response.setHeader(HeadersAttributes.ATTR_CREDENTIALS.getValue(), HeadersValues.AllowCredentials.getValue());
		response.setHeader(HeadersAttributes.ATTR_METHODS.getValue(), HeadersValues.AllMethods.getValue());
		response.setHeader(HeadersAttributes.ATTR_AGE.getValue(), HeadersValues.Max_Age.getValue());
		response.setHeader(HeadersAttributes.ATTR_HEADER.getValue(), HeadersValues.Allowed_Headers.getValue());
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		
	}
	
}
