package com.fr.repositories;

import com.fr.entities.TeamEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    @PostFilter("!filterObject.isDeleted() ")
    List<TeamEntity> findByUuid(int uuid);

    @PostFilter("!filterObject.isDeleted() ")
    List<TeamEntity> findByName(String team, Pageable pageable);
}
