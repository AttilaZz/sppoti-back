package com.fr.rest.service;

import java.util.List;
import java.util.Set;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamEntity;
import com.fr.entities.TeamMemberEntity;
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

    Set<TeamMemberEntity> getTeamMembersEntityFromDto(List<UserDTO> users, TeamEntity team, Long adminId, SppotiEntity sppoti);
}
