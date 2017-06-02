package com.fr.commons.dto;

import com.fr.commons.enumeration.UserRoleTypeEnum;

/**
 * Created by djenanewail on 6/2/17.
 */
public class RoleDTO extends AbstractCommonDTO
{
	private UserRoleTypeEnum name;
	
	public UserRoleTypeEnum getName() {
		return this.name;
	}
	
	public void setName(final UserRoleTypeEnum name) {
		this.name = name;
	}
}
