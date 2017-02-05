package com.fr.repositories;

import com.fr.entities.TeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 2/4/17.
 */
public interface TeamMembersRepository extends JpaRepository<TeamMembers, Long> {

    TeamMembers findByUuid(int memberId);

    TeamMembers findByUsersUuid(int memberId);

    TeamMembers findByUsersUuidAndTeamsUuid(int memberId, int teamId);
}
