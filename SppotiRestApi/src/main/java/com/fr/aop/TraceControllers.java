/**
 *
 */
package com.fr.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Aspect
@Component
/*
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
 */ public class TraceControllers
{
	
	// @Before( "traceInvocationPointcut()" )
	public void afficherDebutTrace(final JoinPoint joinpoint) throws Throwable
	{
		final Object[] args = joinpoint.getArgs();
		final StringBuffer sb = new StringBuffer();
		
		sb.append(joinpoint.getSignature().toString());
		sb.append(" avec les parametres : (");
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (i < args.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
	}
	
	// @After( "traceInvocationPointcut()" ) //for all situations
	// @AfterReturning //when return success
	// @AfterThrowing //when throws error
	public void afficherFinTrace(final StaticPart staticPart, final Object result) throws Throwable
	{
		final String nomMethode = staticPart.getSignature().toLongString();
	}
	
	@Around("traceInvocationPointcut()")
	public Object afficherTrace(final ProceedingJoinPoint joinpoint) throws Throwable
	{
		final String nomMethode = joinpoint.getTarget().getClass().getSimpleName() + "." +
				joinpoint.getSignature().getName();
		final Object[] args = joinpoint.getArgs();
		final StringBuffer sb = new StringBuffer();
		
		sb.append(joinpoint.getSignature().toString());
		sb.append(" avec les parametres : (");
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (i < args.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
		
		Object obj = null;
		try {
			obj = joinpoint.proceed();
		} finally {
		}
		return obj;
	}
	
	/*
	 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html
	 * /aop.html
	 */
	@Pointcut("execution(* com.dz.controllers.*.*(..))")
	public void traceInvocationPointcut()
	{
	}
}