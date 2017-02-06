package com.fr.repositories;

import com.fr.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface TeamRepository extends JpaRepository<Team, Long> {

    @PostFilter("!filterObject.isDeleted() ")
    List<Team> findByUuid(int uuid);

}
