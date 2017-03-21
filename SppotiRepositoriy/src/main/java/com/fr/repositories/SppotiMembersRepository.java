package com.fr.repositories;

import com.fr.entities.SppotiMemberEntity;
import com.fr.models.GlobalAppStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */
public interface SppotiMembersRepository extends JpaRepository<SppotiMemberEntity, Long> {


    /**
     *
     * @param userId user id.
     * @param sppotiId sppoti id.
     * @return sppoter related to a defined team and a sppoti.
     */
    SppotiMemberEntity findByTeamMemberUsersUuidAndSppotiUuid(int userId, int sppotiId);

    /**
     *
     * @param uuid user id.
     * @param id sport id.
     * @return all joined sppoties for a particular sport.
     */
    List<SppotiMemberEntity> findByTeamMemberUsersUuidAndSppotiSportId(int uuid, Long id);

    /**
     *
     * @param userId user id.
     * @param refused sppoti status.
     * @param pageable page number.
     * @return return all joined sppoties, unless refused ones.
     */
    List<SppotiMemberEntity> findByTeamMemberUsersUuidAndStatusNot(int userId, GlobalAppStatus refused, Pageable pageable);
}
