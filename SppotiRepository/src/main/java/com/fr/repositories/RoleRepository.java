package com.fr.repositories;

import com.fr.commons.enumeration.UserRoleTypeEnum;
import com.fr.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 12/9/16.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity getByName(UserRoleTypeEnum name);

}