package com.fr.repositories;

import com.fr.entities.Users_team;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 2/4/17.
 */
public interface TeamMembersRepository extends JpaRepository<Users_team, Long> {

    Users_team findByUuid(int memberId);

    Users_team findByUsersUuid(int memberId);

    Users_team findByUsersUuidAndTeamsUuid(int memberId, int teamId);
}
