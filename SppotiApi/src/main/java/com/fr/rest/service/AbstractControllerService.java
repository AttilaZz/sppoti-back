package com.fr.rest.service;

import java.util.List;
import java.util.Set;

import com.fr.commons.dto.User;
import com.fr.entities.Sppoti;
import com.fr.entities.Team;
import com.fr.entities.TeamMembers;
import org.springframework.stereotype.Service;

import com.fr.entities.Users;

@Service
public interface AbstractControllerService {

    List<String> getUserRole();

    String getAuthenticationUsername();

    Users getUserFromUsernameType(String loginUser);

    int getUserLoginType(String username);

    Users getUserById(Long id);

    Users getUserByUuId(int id);

    User getUserCoverAndAvatar(Users targetUser);

    Set<TeamMembers> getTeamMembersEntityFromDto(List<User> users, Team team, Long adminId, Sppoti sppoti);
}
