/**
 *
 */
package com.fr.commons.enumeration;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */
public enum UserRoleTypeEnum
{
	USER("USER"), ADMIN("ADMIN"), CLIENT("CLIENT"), TEAM_ADMIN("TEAM_ADMIN"), SPPOTI_ADMIN("SPPOTI_ADMIN");
	
	String userRoleType;
	
	UserRoleTypeEnum(final String userProfileType)
	{
		this.userRoleType = userProfileType;
	}
	
	public String getUserProfileType()
	{
		return this.userRoleType;
	}
}
