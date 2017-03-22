package com.fr.repositories;

import com.fr.entities.TeamMemberEntity;
import com.fr.models.GlobalAppStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 2/4/17.
 */
public interface TeamMembersRepository extends JpaRepository<TeamMemberEntity, Long> {

    /**
     *
     * @param memberId member team id.
     * @param teamId team id.
     * @return found team.
     */
    TeamMemberEntity findByUsersUuidAndTeamUuid(int memberId, int teamId);

    /**
     *
     * @param userId user id.
     * @param pageable page number.
     * @return list of found team.
     */
    List<TeamMemberEntity> findByUsersUuidAndAdminTrue(int userId, Pageable pageable);

    /**
     *
     * @param memberId member id.
     * @param teamId team id.
     * @return found team.
     */
    TeamMemberEntity findByUsersUuidAndTeamUuidAndAdminTrue(int memberId, int teamId);

    /**
     *
     * @param id user id.
     * @param team team id.
     * @param pageable page number.
     * @return list of teams.
     */
    List<TeamMemberEntity> findByUsersUuidAndTeamNameContaining(int id, String team, Pageable pageable);

    /**
     *
     * @param teamId team id.
     * @return found team.
     */
    TeamMemberEntity findByTeamUuidAndAdminTrue(int teamId);

    /**
     *
     * @param sport sport id.
     * @param user user id.
     * @param team team id.
     * @param pageable page number.
     * @return list of teams.
     */
    List<TeamMemberEntity> findByTeamSportIdAndUsersUuidAndTeamNameContaining(Long sport, int user, String team, Pageable pageable);

    /**
     *
     * @param teamId team id;
     * @return all team members.
     */
    List<TeamMemberEntity> findByTeamUuid(int teamId);

    /**
     *
     * @param teamId team id.
     * @param memberId member id.
     * @return team member.
     */
    TeamMemberEntity findByTeamUuidAndTeamCaptainTrue(int teamId, int memberId);

    /**
     *
     * @param userId user id.
     * @param confirmed team status.
     * @param pageable page number.
     * @return all entries.
     */
    List<TeamMemberEntity> findByUsersUuidAndStatus(int userId, GlobalAppStatus confirmed, Pageable pageable);
}
