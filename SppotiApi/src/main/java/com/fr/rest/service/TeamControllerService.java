package com.fr.rest.service;

import com.fr.commons.dto.TeamRequestDTO;
import com.fr.commons.dto.TeamResponseDTO;
import com.fr.commons.dto.UserDTO;
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
    TeamResponseDTO saveTeam(TeamRequestDTO team, Long adminId);

    /**
     * @param teamId
     * @return team data.
     */
    TeamResponseDTO getTeamById(int teamId);

    /**
     * Update memeber informations.
     *
     * @param request
     * @param memberId
     * @param teamId
     */
    void updateTeamMembers(TeamRequestDTO request, int memberId, int teamId);

    /**
     * @param userId
     * @param page
     * @return all team that user is admin
     */
    List<TeamResponseDTO> getAllTeamsByUserId(int userId, int page);

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
     * UserDTO must be admin of team to delete members.
     *
     * @param teamId
     * @param memberId
     * @param adminId
     */
    void deleteMemberFromTeam(int teamId, int memberId, int adminId);

    /**
     * Delete a team.
     * <p>
     * UserDTO must be admin to delete the team
     *
     * @param id
     */
    void deleteTeam(int id);

    /**
     * @param teamId
     * @param userParam
     * @return Added memeber
     */
    UserDTO addMember(int teamId, UserDTO userParam);

    /**
     * @param team team to find.
     * @param page page number.
     * @return List of all teams that i am in.
     */
    List<TeamResponseDTO> findAllMyTeams(String team, int page);

    /**
     * @param team team to find.
     * @param page page number.
     * @return List of all teams.
     */
    List<TeamResponseDTO> findAllTeams(String team, int page);

}
