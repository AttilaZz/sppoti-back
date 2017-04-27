package com.fr.repositories;

import com.fr.entities.SppotiEntity;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface SppotiRepository extends JpaRepository<SppotiEntity, Long> {

    /**
     * Find sppoti by uuid.
     * @param uuid sppoti uuid.
     * @return sppoti entity.
     */
    SppotiEntity findByUuid(int uuid);

    /**
     *
     * @param id sppoti creator id.
     * @param pageable page number.
     * @return return all found sppoties.
     */
    @PostFilter("!filterObject.isDeleted()")
    List<SppotiEntity> findByUserSppotiUuid(Integer id, Pageable pageable);
}
