package com.fr.entities;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.enumeration.UserRoleTypeEnum;

@Entity
@Table(name = "ROLES")
@JsonInclude(Include.NON_EMPTY)
public class RoleEntity
        extends AbstractCommonEntity {

    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleTypeEnum name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles", cascade = CascadeType.ALL)
    private Set<UserEntity> users;

    public RoleEntity() {
    }

    public RoleEntity(UserRoleTypeEnum definedRole) {
        super();
        this.name = definedRole;
    }

    public UserRoleTypeEnum getName() {
        return name;
    }

    public void setName(UserRoleTypeEnum name) {
        this.name = name;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RoleEntity that = (RoleEntity) o;

        if (name != that.name) return false;
        return users != null ? users.equals(that.users) : that.users == null;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (users != null ? users.hashCode() : 0);
        return result;
    }
}
