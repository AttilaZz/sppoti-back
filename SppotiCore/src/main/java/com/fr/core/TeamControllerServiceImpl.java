package com.fr.core;

import com.fr.commons.dto.TeamRequestDTO;
import com.fr.commons.dto.TeamResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.SportEntity;
import com.fr.entities.TeamEntity;
import com.fr.entities.TeamMemberEntity;
import com.fr.entities.UserEntity;
import com.fr.exceptions.MemberNotInAdminTeamException;
import com.fr.exceptions.NotTeamAdminException;
import com.fr.models.GlobalAppStatus;
import com.fr.models.NotificationType;
import com.fr.repositories.TeamMembersRepository;
import com.fr.rest.service.TeamControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.EntitytoDtoTransformer.getUserCoverAndAvatar;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class TeamControllerServiceImpl extends AbstractControllerServiceImpl implements TeamControllerService {

    private Logger LOGGER = Logger.getLogger(TeamControllerServiceImpl.class);

    @Value("${key.teamsPerPage}")
    private int teamPageSize;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TeamResponseDTO saveTeam(TeamRequestDTO team, Long adminId) {

        SportEntity sportEntity = sportRepository.findOne(team.getSportId());

        if (sportEntity == null) {
            throw new EntityNotFoundException("SportEntity id not found");
        }

        TeamEntity teamToSave = new TeamEntity();

        teamToSave.setName(team.getName());

        if (team.getCoverPath() != null && !team.getCoverPath().isEmpty()) {
            teamToSave.setCoverPath(team.getCoverPath());
        }

        if (team.getLogoPath() != null && !team.getLogoPath().isEmpty()) {
            teamToSave.setLogoPath(team.getLogoPath());
        }

        teamToSave.setTeamMembers(getTeamMembersEntityFromDto(team.getMembers(), teamToSave, null));

        teamToSave.setSport(sportEntity);
        TeamEntity addedTeam = teamRepository.save(teamToSave);

        return fillTeamResponse(addedTeam, null);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void updateTeamMembers(TeamRequestDTO request, int memberId, int teamId) {

        TeamMemberEntity usersTeam = teamMembersRepository.findByUsersUuidAndTeamUuid(memberId, teamId);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamResponseDTO getTeamById(int teamId) {

        List<TeamEntity> team = teamRepository.findByUuid(teamId);

        if (team == null || team.isEmpty()) {
            throw new EntityNotFoundException("TeamEntity id not found");
        }

        return fillTeamResponse(team.get(0), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TeamResponseDTO> getAllTeamsByUserId(int userId, int page) {

        Pageable pageable = new PageRequest(page, teamPageSize);

        List<TeamMemberEntity> myTeams = teamMembersRepository.findByUsersUuidAndAdminTrue(userId, pageable);
        List<TeamResponseDTO> teamResponseDTOs = new ArrayList<TeamResponseDTO>();


        for (TeamMemberEntity myTeam : myTeams) {
            teamResponseDTOs.add(fillTeamResponse(myTeam.getTeam(), null));
        }

        return teamResponseDTOs;

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void acceptTeam(int teamId, int userId) {

        TeamMemberEntity teamMembers = teamMembersRepository.findByUsersUuidAndTeamUuid(userId, teamId);

        if (teamMembers == null) {
            throw new EntityNotFoundException("TeamEntity not found");
        }

        teamMembers.setStatus(GlobalAppStatus.CONFIRMED.name());

        if (teamMembersRepository.save(teamMembers) != null) {
            addNotification(NotificationType.X_ACCEPTED_YOUR_TEAM_INVITATION, teamMembersRepository.findByTeamUuidAndAdminTrue(teamId).getUsers(), teamMembers.getUsers());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void refuseTeam(int teamId, int userId) {

        TeamMemberEntity teamMembers = teamMembersRepository.findByUsersUuidAndTeamUuid(userId, teamId);

        if (teamMembers == null) {
            throw new EntityNotFoundException("TeamEntity not found");
        }

        teamMembers.setStatus(GlobalAppStatus.REFUSED.name());

        if (teamMembersRepository.save(teamMembers) != null) {
            addNotification(NotificationType.X_REFUSED_YOUR_TEAM_INVITATION, teamMembersRepository.findByTeamUuidAndAdminTrue(teamId).getUsers(), teamMembers.getUsers());

        }

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteMemberFromTeam(int teamId, int memberId, int adminId) {

        //UserDTO deleting the member is admin of the team
        TeamMemberEntity adminTeamMembers = teamMembersRepository.findByUsersUuidAndTeamUuidAndAdminTrue(adminId, teamId);

        if (adminTeamMembers == null) {
            throw new EntityNotFoundException("Delete not permitted - UserDTO is not an admin");
        }

        TeamMemberEntity targetTeamMember = teamMembersRepository.findByUsersUuidAndTeamUuid(memberId, teamId);

        if (targetTeamMember == null) {
            throw new EntityNotFoundException("Member to delete not foundn");
        }

        //Admin and member to delete are in the same team
        if (adminTeamMembers.getTeam().getId().equals(targetTeamMember.getTeam().getId())) {
            teamMembersRepository.delete(targetTeamMember);
        } else {
            throw new MemberNotInAdminTeamException("permission denied for admin with id(" + adminId + ") to delete the memeber with id (" + memberId + ")");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteTeam(int id) {

        List<TeamEntity> team = teamRepository.findByUuid(id);

        if (team == null || team.isEmpty()) {
            throw new EntityNotFoundException("TeamEntity not found");
        }

        team.get(0).setDeleted(true);
        teamRepository.save(team);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public UserDTO addMember(int teamId, UserDTO userParam) {

        List<UserEntity> usersList = userRepository.getByUuid(userParam.getId());
        UserEntity user;

        if (usersList == null || usersList.isEmpty()) {
            throw new EntityNotFoundException("UserDTO with id (" + userParam.getId() + ") Not found");
        }

        user = usersList.get(0);

        List<TeamEntity> teamList = teamRepository.findByUuid(teamId);

        if (teamList == null || teamList.isEmpty()) {
            throw new EntityNotFoundException("TeamEntity with id (" + teamId + ") Not found");
        }

        TeamEntity team = teamList.get(0);

        TeamMemberEntity teamMembers = new TeamMemberEntity();
        teamMembers.setTeam(team);
        teamMembers.setUsers(user);

        if (StringUtils.isEmpty(userParam.getxPosition())) {
            teamMembers.setxPosition(userParam.getxPosition());
        }

        if (StringUtils.isEmpty(userParam.getyPosition())) {
            teamMembers.setyPosition(userParam.getyPosition());
        }

        TeamMemberEntity newMember = teamMembersRepository.save(teamMembers);

        UserDTO userCoverAndAvatar = getUserCoverAndAvatar(user);

        return new UserDTO(newMember.getUuid(), user.getFirstName(), user.getLastName(), user.getUsername(),
                userCoverAndAvatar.getCover() != null ? userCoverAndAvatar.getCover() : null,
                userCoverAndAvatar.getAvatar() != null ? userCoverAndAvatar.getAvatar() : null,
                userCoverAndAvatar.getCoverType() != null ? userCoverAndAvatar.getCoverType() : null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TeamResponseDTO> findAllTeams(String team, int page) {
        Pageable pageable = new PageRequest(page, teamPageSize);

        List<TeamEntity> myTeams = teamRepository.findByNameContaining(team, pageable);

        return myTeams.stream()
                .map(t -> fillTeamResponse(t, null))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TeamResponseDTO> findAllTeamsBySport(String team, Long sport, int page) {
        Pageable pageable = new PageRequest(page, teamPageSize);

        List<TeamMemberEntity> myTeams = teamMembersRepository.findByTeamSportIdAndUsersUuidAndTeamNameContaining(sport,
                getConnectedUser().getUuid(), team, pageable);

        return myTeams.stream()
                .map(t -> fillTeamResponse(t.getTeam(), null))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamResponseDTO updateTeam(int teamId, TeamRequestDTO teamRequestDTO) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTeamCaptain(int teamId, int memberId, int connectedUserId) {

        if (teamMembersRepository.findByUsersUuidAndTeamUuidAndAdminTrue(connectedUserId, teamId) == null) {
            throw new NotTeamAdminException("User is not admin of this team (" + teamId + ") to change captain");
        }

        List<TeamMemberEntity> teamMemberEntity = teamMembersRepository.findByTeamUuid(teamId);

        teamMemberEntity.forEach(t -> {
            if (t.getUuid() == memberId) {
                t.setTeamCaptain(true);
            } else {
                t.setTeamCaptain(false);
            }
        });
        teamMembersRepository.save(teamMemberEntity);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TeamResponseDTO> findAllMyTeams(String team, int page) {
        Pageable pageable = new PageRequest(page, teamPageSize);

        List<TeamMemberEntity> myTeams = teamMembersRepository.findByUsersUuidAndTeamNameContaining(getConnectedUser()
                .getUuid(), team, pageable);

        return myTeams.stream()
                .map(t -> fillTeamResponse(t.getTeam(), null))
                .collect(Collectors.toList());

    }

}