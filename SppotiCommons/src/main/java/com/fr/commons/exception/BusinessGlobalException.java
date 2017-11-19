package com.fr.commons.exception;

/**
 * Created by djenanewail on 3/9/17.
 */
public class BusinessGlobalException extends RuntimeException
{
	private Object object;
	
	public BusinessGlobalException()
	{
		super();
	}
	
	public BusinessGlobalException(final String message, final Object object)
	{
		super(message);
		this.object = object;
	}
	
	public BusinessGlobalException(final String message)
	{
		super(message);
	}
	
	protected BusinessGlobalException(final String message, final Throwable cause, final boolean enableSuppression,
									  final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public Object getObject() {
		return this.object;
	}
}
