package com.fr.repositories;

import com.fr.entities.TeamMembers;
import com.fr.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/4/17.
 */
public interface TeamMembersRepository extends JpaRepository<TeamMembers, Long> {

    TeamMembers findByUuid(int memberId);

    TeamMembers findByUsersUuid(int memberId);

    TeamMembers findByUsersUuidAndTeamUuid(int memberId, int teamId);

    List<TeamMembers> findByUsersUuidAndAdminTrue(int userId, Pageable pageable);

    TeamMembers findByUsersUuidAndTeamUuidAndAdminTrue(int memberId, int teamId);

    List<TeamMembers> findByUsersUuidAndTeamNameContaining(int id, String team, Pageable pageable);

    TeamMembers findByTeamUuidAndAdminTrue(int teamId);
}
