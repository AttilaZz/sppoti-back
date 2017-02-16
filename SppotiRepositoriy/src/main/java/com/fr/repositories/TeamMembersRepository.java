package com.fr.repositories;

import com.fr.entities.TeamMemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/4/17.
 */
public interface TeamMembersRepository extends JpaRepository<TeamMemberEntity, Long> {

    TeamMemberEntity findByUuid(int memberId);

    TeamMemberEntity findByUsersUuid(int memberId);

    TeamMemberEntity findByUsersUuidAndTeamUuid(int memberId, int teamId);

    List<TeamMemberEntity> findByUsersUuidAndAdminTrue(int userId, Pageable pageable);

    TeamMemberEntity findByUsersUuidAndTeamUuidAndAdminTrue(int memberId, int teamId);

    List<TeamMemberEntity> findByUsersUuidAndTeamNameContaining(int id, String team, Pageable pageable);

    TeamMemberEntity findByTeamUuidAndAdminTrue(int teamId);
}
