/**
 *
 */
package com.fr.commons.exception;

/**
 * Created by: Wail DJENANE on Jul 12, 2016
 */
public class EmptyArgumentException extends RuntimeException
{
	
	private static final long serialVersionUID = 4552507363386252439L;
	
	public EmptyArgumentException()
	{
		super();
	}
	
	public EmptyArgumentException(final String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
