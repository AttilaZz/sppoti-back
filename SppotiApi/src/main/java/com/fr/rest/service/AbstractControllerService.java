package com.fr.rest.service;

import java.util.List;
import java.util.Set;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.Sppoti;
import com.fr.entities.Team;
import com.fr.entities.TeamMembers;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface AbstractControllerService {

    List<String> getUserRole();

    String getAuthenticationUsername();

    UserEntity getUserFromUsernameType(String loginUser);

    int getUserLoginType(String username);

    UserEntity getUserById(Long id);

    UserEntity getUserByUuId(int id);

    Set<TeamMembers> getTeamMembersEntityFromDto(List<UserDTO> users, Team team, Long adminId, Sppoti sppoti);
}
