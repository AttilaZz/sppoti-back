package com.fr.repositories;

import com.fr.entities.TeamEntity;
import com.fr.entities.TeamMemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    List<TeamEntity> findByUuidAndDeletedFalse(int uuid);

    List<TeamEntity> findByNameContainingAndDeletedFalse(String team, Pageable pageable);

    List<TeamEntity> findBySportIdAndNameContaining(Long sport, String team, Pageable pageable);
}
