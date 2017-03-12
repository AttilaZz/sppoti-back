package com.fr.repositories;

import com.fr.entities.SppotiMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */
public interface SppotiMembersRepository extends JpaRepository<SppotiMember, Long> {


    SppotiMember findByTeamMemberUsersUuidAndSppotiUuid(int userId, int sppotiId);

    List<SppotiMember> findByTeamMemberUsersUuidAndSppotiSportId(int uuid, Long id);

    List<SppotiMember> findByTeamMemberUsersUuid(int userId, Pageable pageable);

    SppotiMember findByUuid(Integer sppotiRatedId);
}
