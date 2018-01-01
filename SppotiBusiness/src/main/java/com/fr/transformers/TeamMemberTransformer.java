package com.fr.transformers;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamMemberEntity;

import java.util.List;
import java.util.Set;

/**
 * Created by djenanewail on 1/1/18.
 */
public interface TeamMemberTransformer
{
	List<UserDTO> modelToDto(Set<TeamMemberEntity> memberEntity, SppotiEntity sppoti);
	
	UserDTO modelToDto(TeamMemberEntity memberEntity, SppotiEntity sppoti);
}
