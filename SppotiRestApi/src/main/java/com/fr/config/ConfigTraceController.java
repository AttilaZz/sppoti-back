package com.fr.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Aspect
@Component
/**
 * Common AspectJ annotations :
 *
 * @Before – Run before the method execution
 *
 * @After – Run after the method returned a result
 *
 * @AfterReturning – Run after the method returned a result, intercept the
 * returned result as well.
 *
 * @AfterThrowing – Run after the method throws an exception
 *
 * @Around – Run around the method execution, combine all three advices above.
 */

public class ConfigTraceController
{
	private final Logger LOGGER = LoggerFactory.getLogger(ConfigTraceController.class);
	
	@Before("traceInvocationPointcut()")
	public void displayTraceBeginning(final JoinPoint joinpoint) throws Throwable
	{
		final Object[] args = joinpoint.getArgs();
		final StringBuilder sb = new StringBuilder();
		printDevicePlatform();
		
		sb.append("Request sent to ");
		sb.append(joinpoint.getSignature().getName());
		sb.append(" with params : (");
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (i < args.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
		this.LOGGER.info("{}", sb.toString());
	}
	
	@After("traceInvocationPointcut()") //for all situations
	public void displayLastTrace(final StaticPart staticPart) throws Throwable
	{
		final String fullPackageName = staticPart.getSignature().getDeclaringType().getName();
		final String[] className = fullPackageName.split("\\.");
		this.LOGGER.info("Result has been returned from: <{}>", className[className.length - 1]);
	}
	
	/*
	 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html
	 * /config.html
	 */
	@Pointcut(value = "execution(* com.fr.api..*.*(..))")
	public void traceInvocationPointcut()
	{
	}
	
	private void printDevicePlatform() {
		final RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
		if (attribs instanceof NativeWebRequest) {
			final HttpServletRequest request = (HttpServletRequest) ((NativeWebRequest) attribs).getNativeRequest();
			final Device currentDevice = (Device) request.getAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE);
			this.LOGGER.info("Request received from this platform: {}", currentDevice.getDevicePlatform());
		}
	}
}