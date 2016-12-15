package com.fr.repositories;

import com.fr.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 12/9/16.
 */
public interface RoleRepository extends JpaRepository<Roles, Long> {

    Roles getByName(String name);

}