package com.fr.enums;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */
enum AccountState
{
	
	ACTIVE("Active"), INACTIVE("Inactive"), DELETED("Deleted"), LOCKED("Locked");
	
	private String state;
	
	private AccountState(final String state)
	{
		this.state = state;
	}
	
	public String getState()
	{
		return this.state;
	}
	
	@Override
	public String toString()
	{
		return this.state;
	}
	
	public String getName()
	{
		return this.name();
	}
	
}
