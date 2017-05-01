/**
 *
 */
package com.fr.filter;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
public enum CustomHttpStatus
{
	
	FAILED_TO_SAVED(40, "Save Failed"), NOT_VALID_ENTITY_ID(41, "Entity ID is not valid");
	
	private final int value;
	
	private final String reasonPhrase;
	
	CustomHttpStatus(final int value, final String reasonPhrase)
	{
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}
	
	/**
	 * Return the integer value of this status code.
	 */
	public int value()
	{
		return this.value;
	}
	
	/**
	 * Return the reason phrase of this status code.
	 */
	public String getReasonPhrase()
	{
		return this.reasonPhrase;
	}
	
}
