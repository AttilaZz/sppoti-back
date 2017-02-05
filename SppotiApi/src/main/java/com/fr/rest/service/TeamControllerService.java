package com.fr.rest.service;

import com.fr.commons.dto.TeamRequest;
import com.fr.commons.dto.TeamResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

@Service
public interface TeamControllerService extends AbstractControllerService{

    void saveTeam(TeamRequest team, Long adminId);

    TeamResponse getTeamById(int teamId);

    void updateTeamMembers(TeamRequest request, int memberId, int teamId);

    List<TeamResponse> getAllTeamsByUserId(int userId, int page);

    void acceptTeam(int teamId, int uuid);

    void refuseTeam(int teamId, int uuid);

    void deleteMemberFromTeam(int teamId, int memberId, int adminId);
}
