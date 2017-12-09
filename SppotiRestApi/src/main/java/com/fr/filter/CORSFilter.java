package com.fr.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.fr.filter.HeadersAttributes.*;
import static com.fr.filter.HeadersValues.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
@Order(HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter
{
	private static final String BROWSER = "browser";
	private final Logger LOGGER = LoggerFactory.getLogger(CORSFilter.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException
	{
		final HttpServletResponse response = (HttpServletResponse) res;
		final HttpServletRequest request = (HttpServletRequest) req;
		
		if (StringUtils.hasText(Origins.getValue())) {
			final String[] allowedHeaders = Origins.getValue().split(",");
			
			for (final String allowedHeader : allowedHeaders) {
				if (allowedHeader.equals(request.getHeader("origin"))) {
					response.setHeader(ATTR_ORIGIN.getValue(), allowedHeader);
					this.LOGGER.info("Request header origin is secured, allowing access to: {}", allowedHeader);
					break;
				}
			}
		} else {
			this.LOGGER.info("NO origin filter found in properties file, Origin will be set to <*>");
			response.setHeader(ATTR_ORIGIN.getValue(), "*");
		}
		
		response.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
		response.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
		response.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
		response.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());
		response.setHeader(ATTR_EXPOSE_HEADERS.getValue(), AccessControlExposeHeaders.getValue());
		
		if (!Objects.equals(request.getMethod(), "OPTIONS")) {
			chain.doFilter(req, res);
		}
		
	}
	
	@Override
	public void init(final FilterConfig filterConfig)
	{
	}
	
	@Override
	public void destroy()
	{
	}
	
}