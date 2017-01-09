package com.fr.repositories;

import com.fr.entities.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by djenanewail on 12/8/16.
 */
public interface SportRepository extends JpaRepository<Sport, Long> {

    Sport getById(Long id);

    @Query("SELECT s FROM Sport s WHERE s.id = :id")
    Sport getSportById(@Param("id") Long sportId);
}
