package com.fr.repositories;

import com.fr.entities.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * Created by djenanewail on 3/12/17.
 */
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    Optional<Set<RatingEntity>> findByRatedSppoterUuidAndSppotiEntityUuid(String userId, String sppotiId);
}
