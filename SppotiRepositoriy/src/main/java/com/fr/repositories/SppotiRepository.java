package com.fr.repositories;

import com.fr.entities.Sppoti;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface SppotiRepository extends JpaRepository<Sppoti, Long> {


    Sppoti findByUuid(int uuid);

    @PostFilter("!filterObject.isDeleted() ")
    List<Sppoti> findByUserSppotiUuid(Integer id, Pageable pageable);
}
