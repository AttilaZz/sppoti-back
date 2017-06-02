package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fr.commons.enumeration.UserRoleTypeEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ROLES")
public class RoleEntity extends AbstractCommonEntity
{
	
	@Column(name = "name", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRoleTypeEnum name;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles", cascade = CascadeType.ALL)
	private Set<UserEntity> users;
	
	public RoleEntity() {
	}
	
	public RoleEntity(final UserRoleTypeEnum definedRole) {
		super();
		this.name = definedRole;
	}
	
	public UserRoleTypeEnum getName() {
		return this.name;
	}
	
	public void setName(final UserRoleTypeEnum name) {
		this.name = name;
	}
	
	public Set<UserEntity> getUsers() {
		return this.users;
	}
	
	public void setUsers(final Set<UserEntity> users) {
		this.users = users;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		
		final RoleEntity that = (RoleEntity) o;
		
		if (this.name != that.name)
			return false;
		return this.users != null ? this.users.equals(that.users) : that.users == null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
		result = 31 * result + (this.users != null ? this.users.hashCode() : 0);
		return result;
	}
}
