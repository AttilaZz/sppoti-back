package com.fr.repositories;

import com.fr.entities.SppotiMembers;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 2/5/17.
 */
public interface SppotiMembersRepository extends JpaRepository<SppotiMembers, Long> {


    SppotiMembers findByUsersTeamUsersUuidAndSppotiUuid(int userId, int sppotiId);
}
