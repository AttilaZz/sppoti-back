package com.fr.core;

import com.fr.commons.dto.TeamRequest;
import com.fr.commons.dto.TeamResponse;
import com.fr.entities.Team;
import com.fr.entities.TeamMembers;
import com.fr.exceptions.HostMemberNotFoundException;
import com.fr.exceptions.MemberNotInAdminTeamException;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.service.TeamControllerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class TeamControllerServiceImpl extends AbstractControllerServiceImpl implements TeamControllerService {

    @Value("${key.teamsPerPage}")
    private int teamPageSize;

    @Override
    public void saveTeam(TeamRequest team, Long adminId) {
        Team teamToSave = new Team();

        teamToSave.setName(team.getName());

        if (team.getCoverPath() != null && !team.getCoverPath().isEmpty()) {
            teamToSave.setCoverPath(team.getCoverPath());
        }

        if (team.getLogoPath() != null && !team.getLogoPath().isEmpty()) {
            teamToSave.setLogoPath(team.getLogoPath());
        }

        try {

            teamToSave.setTeamMemberss(getTeamMembersEntityFromDto(team.getMembers(), teamToSave, adminId, null));

        } catch (RuntimeException e) {
            LOGGER.error("One of the team id not found: " + e.getMessage());
            throw new HostMemberNotFoundException("TeamRequest (members) one of the team dosn't exist");

        }

        teamRepository.save(teamToSave);

    }

    @Override
    public void updateTeamMembers(TeamRequest request, int memberId, int teamId) {

        TeamMembers usersTeam = teamMembersRepository.findByUsersUuidAndTeamsUuid(memberId, teamId);

        if (usersTeam == null) {
            throw new EntityNotFoundException("Member not found");
        }

        if (request.getStatus() != null || !request.getStatus().equals(0)) {
            for (GlobalAppStatus status : GlobalAppStatus.values()) {
                if (status.getValue() == request.getStatus()) {
                    usersTeam.setStatus(status.name());
                }
            }
        }

        if (request.getxPosition() != null && request.getyPosition() != null) {
            usersTeam.setyPosition(request.getyPosition());
            usersTeam.setxPosition(request.getxPosition());
        }

        teamMembersRepository.save(usersTeam);
    }

    @Override
    public TeamResponse getTeamById(int teamId) {

        List<Team> team = teamRepository.findByUuid(teamId);

        if (team == null || team.isEmpty()) {
            throw new EntityNotFoundException("Team id not found");
        }

        return fillTeamResponse(team.get(0), null);
    }

    @Override
    public List<TeamResponse> getAllTeamsByUserId(int userId, int page) {

        Pageable pageable = new PageRequest(page, teamPageSize);

        List<TeamMembers> myTeams = teamMembersRepository.findByUsersUuidAndAdminTrue(userId, pageable);
        List<TeamResponse> teamResponses = new ArrayList<TeamResponse>();


        for (TeamMembers myTeam : myTeams) {
            teamResponses.add(fillTeamResponse(myTeam.getTeams(), null));
        }

        return teamResponses;

    }

    /**
     * Accept friend invitation
     *
     * @param teamId
     * @param userId
     */
    @Override
    public void acceptTeam(int teamId, int userId) {

        TeamMembers teamMembers = teamMembersRepository.findByUsersUuidAndTeamsUuid(userId, teamId);

        if (teamMembers == null) {
            throw new EntityNotFoundException("Team not found");
        }

        teamMembers.setStatus(GlobalAppStatus.CONFIRMED.name());

        teamMembersRepository.save(teamMembers);

    }

    /**
     * Refuse an invitation for a given team;
     *
     * @param teamId
     * @param userId
     */
    @Override
    public void refuseTeam(int teamId, int userId) {

        TeamMembers teamMembers = teamMembersRepository.findByUsersUuidAndTeamsUuid(userId, teamId);

        if (teamMembers == null) {
            throw new EntityNotFoundException("Team not found");
        }

        teamMembers.setStatus(GlobalAppStatus.REFUSED.name());

        teamMembersRepository.save(teamMembers);

    }

    /**
     * Delete member from a given team.
     * user who delete must be admin of the team
     *
     * @param teamId
     * @param memberId
     */
    @Override
    public void deleteMemberFromTeam(int teamId, int memberId, int adminId) {

        //User deleting the member is admin of the team
        TeamMembers adminTeamMembers = teamMembersRepository.findByUsersUuidAndTeamsUuidAndAdminTrue(adminId, teamId);

        if (adminTeamMembers == null) {
            throw new EntityNotFoundException("Delete not permitted - User is not an admin");
        }

        TeamMembers targetTeamMember = teamMembersRepository.findByUsersUuidAndTeamsUuid(memberId, teamId);

        if (targetTeamMember == null) {
            throw new EntityNotFoundException("Member to delete not foundn");
        }

        //Admin and memeber to delete are in the same team
        if (adminTeamMembers.getTeams().getId().equals(targetTeamMember.getTeams().getId())) {
            teamMembersRepository.delete(targetTeamMember);
        } else {
            throw new MemberNotInAdminTeamException("permission denied for admin with id(" + adminId + ") to delete the memeber with id (" + memberId + ")");
        }
    }

    @Override
    public void deleteTeam(int id) {

        List<Team> team = teamRepository.findByUuid(id);

        if (team == null || team.isEmpty()) {
            throw new EntityNotFoundException("Team not found");
        }

        team.get(0).setDeleted(true);
        teamRepository.save(team);

    }

}