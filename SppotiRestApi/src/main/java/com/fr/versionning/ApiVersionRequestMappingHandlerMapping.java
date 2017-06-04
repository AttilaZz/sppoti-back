package com.fr.versionning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * Created by djenanewail on 6/4/17.
 */
public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping
{
	/** Class logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	/** version prefix. */
	private final String prefix;
	
	/** Init prefix. */
	public ApiVersionRequestMappingHandlerMapping(final String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RequestMappingInfo getMappingForMethod(final Method method, final Class<?> handlerType) {
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		if (info == null)
			return null;
		
		final ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
		if (methodAnnotation != null) {
			final RequestCondition<?> methodCondition = getCustomMethodCondition(method);
			// Concatenate our ApiVersion with the usual request mapping
			info = createApiVersionInfo(methodAnnotation, methodCondition).combine(info);
		} else {
			final ApiVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
			if (typeAnnotation != null) {
				final RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
				// Concatenate our ApiVersion with the usual request mapping
				info = createApiVersionInfo(typeAnnotation, typeCondition).combine(info);
			}
		}
		
		this.LOGGER.info(info.toString());
		return info;
	}
	
	/**
	 * Create Api Version info.
	 */
	private RequestMappingInfo createApiVersionInfo(final ApiVersion annotation,
													final RequestCondition<?> customCondition)
	{
		final String[] values = annotation.value();
		final String[] patterns = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			// Build the URL prefix
			patterns[i] = this.prefix + values[i];
		}
		
		return new RequestMappingInfo(
				new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(),
						useTrailingSlashMatch(), getFileExtensions()), new RequestMethodsRequestCondition(),
				new ParamsRequestCondition(), new HeadersRequestCondition(), new ConsumesRequestCondition(),
				new ProducesRequestCondition(), customCondition);
	}
}
