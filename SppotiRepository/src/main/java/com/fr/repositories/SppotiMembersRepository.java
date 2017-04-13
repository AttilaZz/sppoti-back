package com.fr.repositories;

import com.fr.entities.SppoterEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */
public interface SppotiMembersRepository extends JpaRepository<SppoterEntity, Long> {


    /**
     * find sppoter by his global user id and sppoti.
     * (Expect only one result)
     *
     * @param userId   user id.
     * @param sppotiId sppoti id.
     * @return sppoter related to a defined team and a sppoti.
     */
    SppoterEntity findByTeamMemberUsersUuidAndSppotiUuid(int userId, int sppotiId);

    /**
     * Find sppoter by team and sport.
     * (Expect multiple results)
     *
     * @param uuid user id.
     * @param id   sport id.
     * @return all joined sppoties for a particular sport.
     */
    List<SppoterEntity> findByTeamMemberUsersUuidAndSppotiSportId(int uuid, Long id);

    /**
     * Find sppoter by his global user id.
     * (Expect multiple results)
     *
     * @param userId   user id.
     * @param pageable page number.
     * @return return all joined sppoties, unless refused ones.
     */
    List<SppoterEntity> findByTeamMemberUsersUuid(int userId, Pageable pageable);

}
