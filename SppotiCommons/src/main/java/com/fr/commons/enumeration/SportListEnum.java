package com.fr.commons.enumeration;

/**
 * Created by: Wail DJENANE on Aug 18, 2016
 */
public enum SportListEnum
{
	
	FOOTBALL("Football"), TENNIS("Tennis"), BASKETBALL("Basketball"), BOX("Box"), Rugby("Rugby"), HANDBALL("Handball"),
	VOLEYBALL("Voleyball"), NATATION("Natation"), GOLF("Golf"), JUDO("Judo"), JOGGING("Jogging");
	
	String sportType;
	
	SportListEnum(final String sportName)
	{
		this.sportType = sportName;
	}
	
	public String getSportType()
	{
		return this.sportType;
	}
}
