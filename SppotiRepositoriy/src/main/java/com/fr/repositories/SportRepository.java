package com.fr.repositories;

import com.fr.entities.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 12/8/16.
 */
public interface SportRepository extends JpaRepository<Sport, Long> {

    Sport getById(Long id);

}
