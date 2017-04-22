package com.fr.repositories;

import com.fr.entities.SppotiAdverseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 4/22/17.
 */
public interface SppotiAdverseRepository extends JpaRepository<SppotiAdverseEntity, Long> {
    List<SppotiAdverseEntity> findByTeamUuidAndFromSppotiAdminTrue(int teamId);
}
