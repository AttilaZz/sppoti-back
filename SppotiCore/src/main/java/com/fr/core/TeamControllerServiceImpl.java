package com.fr.core;

import com.fr.commons.dto.TeamRequestDTO;
import com.fr.commons.dto.TeamResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.Sport;
import com.fr.entities.Team;
import com.fr.entities.TeamMembers;
import com.fr.entities.UserEntity;
import com.fr.exceptions.HostMemberNotFoundException;
import com.fr.exceptions.MemberNotInAdminTeamException;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.service.TeamControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static utils.EntitytoDtoTransformer.getUserCoverAndAvatar;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class TeamControllerServiceImpl extends AbstractControllerServiceImpl implements TeamControllerService {

    private Logger LOGGER = Logger.getLogger(TeamControllerServiceImpl.class);

    @Value("${key.teamsPerPage}")
    private int teamPageSize;

    @Override
    public TeamResponseDTO saveTeam(TeamRequestDTO team, Long adminId) {

        Sport sport = sportRepository.findOne(team.getSportId());

        if (sport == null) {
            throw new EntityNotFoundException("Sport id not foud");
        }

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
            throw new HostMemberNotFoundException("TeamRequestDTO (members) one of the team dosn't exist");

        }

        teamToSave.setSport(sport);
        Team addedTeam = teamRepository.save(teamToSave);

        return fillTeamResponse(addedTeam, null);

    }

    @Override
    public void updateTeamMembers(TeamRequestDTO request, int memberId, int teamId) {

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
    public TeamResponseDTO getTeamById(int teamId) {

        List<Team> team = teamRepository.findByUuid(teamId);

        if (team == null || team.isEmpty()) {
            throw new EntityNotFoundException("Team id not found");
        }

        return fillTeamResponse(team.get(0), null);
    }

    @Override
    public List<TeamResponseDTO> getAllTeamsByUserId(int userId, int page) {

        Pageable pageable = new PageRequest(page, teamPageSize);

        List<TeamMembers> myTeams = teamMembersRepository.findByUsersUuidAndAdminTrue(userId, pageable);
        List<TeamResponseDTO> teamResponseDTOs = new ArrayList<TeamResponseDTO>();


        for (TeamMembers myTeam : myTeams) {
            teamResponseDTOs.add(fillTeamResponse(myTeam.getTeams(), null));
        }

        return teamResponseDTOs;

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

        //UserDTO deleting the member is admin of the team
        TeamMembers adminTeamMembers = teamMembersRepository.findByUsersUuidAndTeamsUuidAndAdminTrue(adminId, teamId);

        if (adminTeamMembers == null) {
            throw new EntityNotFoundException("Delete not permitted - UserDTO is not an admin");
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

    @Override
    public UserDTO addMember(int teamId, UserDTO userParam) {

        List<UserEntity> usersList = userRepository.getByUuid(userParam.getId());
        UserEntity user;

        if (usersList == null || usersList.isEmpty()) {
            throw new EntityNotFoundException("UserDTO with id (" + userParam.getId() + ") Not found");
        }

        user = usersList.get(0);

        List<Team> teamList = teamRepository.findByUuid(teamId);

        if (teamList == null || teamList.isEmpty()) {
            throw new EntityNotFoundException("Team with id (" + teamId + ") Not found");
        }

        Team team = teamList.get(0);

        TeamMembers teamMembers = new TeamMembers();
        teamMembers.setTeams(team);
        teamMembers.setUsers(user);

        if (StringUtils.isEmpty(userParam.getxPosition())) {
            teamMembers.setxPosition(userParam.getxPosition());
        }

        if (StringUtils.isEmpty(userParam.getyPosition())) {
            teamMembers.setyPosition(userParam.getyPosition());
        }

        TeamMembers newMember = teamMembersRepository.save(teamMembers);

        UserDTO userCoverAndAvatar = getUserCoverAndAvatar(user);

        return new UserDTO(newMember.getUuid(), user.getFirstName(), user.getLastName(), user.getUsername(),
                userCoverAndAvatar.getCover() != null ? userCoverAndAvatar.getCover() : null,
                userCoverAndAvatar.getAvatar() != null ? userCoverAndAvatar.getAvatar() : null,
                userCoverAndAvatar.getCoverType() != null ? userCoverAndAvatar.getCoverType() : null);

    }

    @Override
    public List<TeamResponseDTO> findAllTeams(String team, int id, int page) {
        Pageable pageable = new PageRequest(page, teamPageSize);

        List<TeamMembers> myTeams = teamMembersRepository.findByUsersUuidAndTeamsNameContaining(id, team, pageable);
        List<TeamResponseDTO> teamResponseDTOs = new ArrayList<TeamResponseDTO>();

        for (TeamMembers myTeam : myTeams) {
            teamResponseDTOs.add(fillTeamResponse(myTeam.getTeams(), null));
        }

        return teamResponseDTOs;
    }

}