package com.fr.repositories;

import com.fr.entities.SppotiMemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */
public interface SppotiMembersRepository extends JpaRepository<SppotiMemberEntity, Long> {


    SppotiMemberEntity findByTeamMemberUsersUuidAndSppotiUuid(int userId, int sppotiId);

    List<SppotiMemberEntity> findByTeamMemberUsersUuidAndSppotiSportId(int uuid, Long id);

    List<SppotiMemberEntity> findByTeamMemberUsersUuid(int userId, Pageable pageable);

    SppotiMemberEntity findByUuid(Integer sppotiRatedId);
}
