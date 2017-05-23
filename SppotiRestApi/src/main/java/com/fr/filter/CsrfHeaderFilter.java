/**
 *
 */
package com.fr.filter;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by: Wail DJENANE on Jul 6, 2016
 */
public class CsrfHeaderFilter extends OncePerRequestFilter
{
	
	/** CSRF properties. */
	private final CsrfProperties filterProperties;
	
	/** init csrf properties. */
	public CsrfHeaderFilter(final CsrfProperties filterProperties)
	{
		this.filterProperties = filterProperties;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
									final FilterChain filterChain) throws ServletException, IOException
	{
		final CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		if (csrf != null) {
			Cookie cookie = WebUtils.getCookie(request, "X-XSRF-TOKEN");
			final String token = csrf.getToken();
			if (cookie == null || token != null && !token.equals(cookie.getValue())) {
				cookie = new Cookie("X-XSRF-TOKEN", token);
				cookie.setPath(this.filterProperties.getPath());
				cookie.setDomain(this.filterProperties.getDomain());
				cookie.setSecure(this.filterProperties.isSecureConnexion());
				cookie.setMaxAge(this.filterProperties.getMaxAge());
				cookie.setComment(this.filterProperties.getComment());
				response.addCookie(cookie);
			}
		}
		filterChain.doFilter(request, response);
	}
	
}