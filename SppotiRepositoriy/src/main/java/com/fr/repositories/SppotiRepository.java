package com.fr.repositories;

import com.fr.entities.SppotiEntity;
import com.fr.entities.SppotiMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface SppotiRepository extends JpaRepository<SppotiEntity, Long> {


    SppotiEntity findByUuid(int uuid);

    @PostFilter("!filterObject.isDeleted() ")
    List<SppotiEntity> findByUserSppotiUuid(Integer id, Pageable pageable);
}
