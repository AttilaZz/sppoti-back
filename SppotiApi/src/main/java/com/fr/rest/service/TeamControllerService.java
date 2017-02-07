package com.fr.rest.service;

import com.fr.commons.dto.TeamRequest;
import com.fr.commons.dto.TeamResponse;
import com.fr.commons.dto.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

@Service
public interface TeamControllerService extends AbstractControllerService {

    /**
     * @param team
     * @param adminId
     * @return saved team data.
     */
    TeamResponse saveTeam(TeamRequest team, Long adminId);

    /**
     * @param teamId
     * @return team data.
     */
    TeamResponse getTeamById(int teamId);

    /**
     * Update memeber informations.
     *
     * @param request
     * @param memberId
     * @param teamId
     */
    void updateTeamMembers(TeamRequest request, int memberId, int teamId);

    /**
     * @param userId
     * @param page
     * @return all team that user is admin
     */
    List<TeamResponse> getAllTeamsByUserId(int userId, int page);

    /**
     * Member can accept team invitation
     *
     * @param teamId
     * @param uuid
     */
    void acceptTeam(int teamId, int uuid);

    /**
     * Member can refuse team invitation
     *
     * @param teamId
     * @param uuid
     */
    void refuseTeam(int teamId, int uuid);

    /**
     * Delete a memeber from a team.
     * <p>
     * User must be admin of team to delete members.
     *
     * @param teamId
     * @param memberId
     * @param adminId
     */
    void deleteMemberFromTeam(int teamId, int memberId, int adminId);

    /**
     * Delete a team.
     * <p>
     * User must be admin to delete the team
     *
     * @param id
     */
    void deleteTeam(int id);

    /**
     * @param teamId
     * @param userParam
     * @return Added memeber
     */
    User addMember(int teamId, User userParam);

    /**
     * @param team
     * @param id
     * @param page
     * @return List of all found teams with theirs members
     */
    List<TeamResponse> findAllTeams(String team, int id, int page);
}
