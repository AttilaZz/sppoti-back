package com.fr.repositories;

import com.fr.entities.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link ScoreEntity}.
 * <p>
 * Created by wdjenane on 04/04/2017.
 */
public interface ScoreRepository extends JpaRepository<ScoreEntity, Long>{
    ScoreEntity findByUuid(Integer id);
}
