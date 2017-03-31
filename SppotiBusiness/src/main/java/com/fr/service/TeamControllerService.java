package com.fr.service;

import com.fr.commons.dto.team.TeamRequestDTO;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.commons.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

@Service
public interface TeamControllerService extends AbstractControllerService {

    /**
     * @param team team to save.
     * @param adminId admin id.
     * @return saved team data.
     */
    TeamResponseDTO saveTeam(TeamRequestDTO team, Long adminId);

    /**
     * @param teamId team id.
     * @return team data.
     */
    TeamResponseDTO getTeamById(int teamId);

    /**
     * Update memeber informations.
     *
     * @param request team to update.
     * @param memberId member team id.
     * @param teamId team id.
     */
    void updateTeamMembers(TeamRequestDTO request, int memberId, int teamId);

    /**
     * @param userId user id.
     * @param page page number.
     * @return all team that user is admin
     */
    List<TeamResponseDTO> getAllTeamsByUserId(int userId, int page);

    /**
     * Member can accept team invitation
     *
     * @param teamId team id.
     * @param uuid user unique id.
     */
    void acceptTeam(int teamId, int uuid);

    /**
     * Member can refuse team invitation
     *
     * @param teamId team id.
     * @param uuid user unique id.
     */
    void refuseTeam(int teamId, int uuid);

    /**
     * Delete a memeber from a team.
     * <p>
     * UserDTO must be admin of team to delete members.
     *
     * @param teamId team id.
     * @param memberId member team id.
     * @param adminId team admin id.
     */
    void deleteMemberFromTeam(int teamId, int memberId, int adminId);

    /**
     * Delete a team.
     * <p>
     * UserDTO must be admin to delete the team
     *
     * @param id team id.
     */
    void deleteTeam(int id);

    /**
     * @param teamId team id.
     * @param userParam member to add.
     * @return Added memeber.
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

    /**
     *
     * @param team team to find.
     * @param sport sport id.
     * @param page page number.
     * @return list of related teams.
     */
    List<TeamResponseDTO> findAllTeamsBySport(String team, Long sport, int page);

    /**
     *
     * @param teamId team id.
     * @param teamRequestDTO team data to update.
     * @return updated team.
     */
    TeamResponseDTO updateTeam(int teamId, TeamRequestDTO teamRequestDTO);

    /**
     *
     * @param teamId team id.
     * @param memberId member id.
     * @param connectedUserId connected user id.
     */
    void updateTeamCaptain(int teamId, int memberId, int connectedUserId);

    /**
     * Get all joined teams, except my teams.
     *
     * @param userId user iod.
     * @param page page number.
     * @return all teams.
     */
    List<TeamResponseDTO> getAllJoinedTeamsByUserId(int userId, int page);

    /**
     *
     * @param userId user id.
     * @param page page number.
     * @return all deleted teams.
     */
    List<TeamResponseDTO> getAllDeletedTeamsByUserId(int userId, int page);
}
