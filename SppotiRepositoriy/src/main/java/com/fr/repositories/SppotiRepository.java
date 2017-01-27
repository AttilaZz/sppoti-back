package com.fr.repositories;

import com.fr.entities.Sppoti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface SppotiRepository extends JpaRepository<Sppoti, Long> {


    Sppoti findByUuid(int uuid);

    List<Sppoti> findByUserSppotiUuid(int id);
}
