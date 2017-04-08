package com.fr.repositories;

import com.fr.entities.SppotiEntity;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface SppotiRepository extends JpaRepository<SppotiEntity, Long> {


    SppotiEntity findByUuid(int uuid);

    /**
     *
     * @param id sppoti creator id.
     * @param adverseTeamStatus adverse team status to exclude from search.
     * @param pageable page number.
     * @return return all found sppoties.
     */
    @PostFilter("!filterObject.isDeleted()")
    List<SppotiEntity> findByUserSppotiUuidAndTeamAdverseStatusEnumNot(Integer id, GlobalAppStatusEnum adverseTeamStatus, Pageable pageable);
}
