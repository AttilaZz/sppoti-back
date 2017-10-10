package com.fr.impl.notification;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * Created by djenanewail on 10/9/17.
 */
public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor
{
	private final String headerName;
	private final String headerValue;
	
	public HeaderRequestInterceptor(final String headerName, final String headerValue) {
		this.headerName = headerName;
		this.headerValue = headerValue;
	}
	
	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
										final ClientHttpRequestExecution execution) throws IOException
	{
		final HttpRequest wrapper = new HttpRequestWrapper(request);
		wrapper.getHeaders().set(this.headerName, this.headerValue);
		return execution.execute(wrapper, body);
	}
}
