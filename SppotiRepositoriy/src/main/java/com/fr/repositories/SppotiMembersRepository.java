package com.fr.repositories;

import com.fr.entities.SppotiMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */
public interface SppotiMembersRepository extends JpaRepository<SppotiMember, Long> {


    SppotiMember findByUsersTeamUsersUuidAndSppotiUuid(int userId, int sppotiId);

    List<SppotiMember> findByUsersTeamUsersUuidAndSppotiSportId(int uuid, Long id);
}
