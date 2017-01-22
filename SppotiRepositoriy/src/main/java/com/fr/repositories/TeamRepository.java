package com.fr.repositories;

import com.fr.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface TeamRepository extends JpaRepository<Team, Long> {
}
