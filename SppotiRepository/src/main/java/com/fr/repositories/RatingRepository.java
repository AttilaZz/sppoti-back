package com.fr.repositories;

import com.fr.entities.SppotiRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * Created by djenanewail on 3/12/17.
 */
public interface RatingRepository extends JpaRepository<SppotiRatingEntity, Long> {

    Optional<Set<SppotiRatingEntity>> findByRatedSppoterUuidAndSppotiEntityUuid(String userId, String sppotiId);
}
