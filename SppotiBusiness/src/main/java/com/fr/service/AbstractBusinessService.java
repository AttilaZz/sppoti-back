package com.fr.service;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamEntity;
import com.fr.entities.TeamMemberEntity;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface AbstractBusinessService
{
	
	/**
	 * @param id
	 * 		user id.
	 *
	 * @return found userEntity.
	 */
	UserEntity getUserById(Long id);
	
	/**
	 * @param id
	 * 		user id.
	 *
	 * @return found user entity.
	 */
    UserEntity getUserByUuId(String id);
	
	/**
	 * @param users
	 * 		list of users.
	 * @param team
	 * 		team to map.
	 * @param sppoti
	 * 		sppoti to map.
	 *
	 * @return set of USERS_TEAM
	 */
	Set<TeamMemberEntity> getTeamMembersEntityFromDto(List<UserDTO> users, TeamEntity team, SppotiEntity sppoti);
}
