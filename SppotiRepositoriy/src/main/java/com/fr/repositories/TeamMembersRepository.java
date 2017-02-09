package com.fr.repositories;

import com.fr.entities.TeamMembers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/4/17.
 */
public interface TeamMembersRepository extends JpaRepository<TeamMembers, Long> {

    TeamMembers findByUuid(int memberId);

    TeamMembers findByUsersUuid(int memberId);

    TeamMembers findByUsersUuidAndTeamsUuid(int memberId, int teamId);

    List<TeamMembers> findByUsersUuidAndAdminTrue(int userId, Pageable pageable);

    TeamMembers findByUsersUuidAndTeamsUuidAndAdminTrue(int memberId, int teamId);

    List<TeamMembers> findByUsersUuidAndAdminFalseAndTeamsNameContaining(int id, String team, Pageable pageable);

    List<TeamMembers> findByUsersUuidAndAdminFalse(int id, Pageable pageable);

    List<TeamMembers> findByUsersUuidAndTeamsNameContaining(int id, String team, Pageable pageable);
}
