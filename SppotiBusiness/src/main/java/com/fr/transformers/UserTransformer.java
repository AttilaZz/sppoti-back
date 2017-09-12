package com.fr.transformers;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;

/**
 * Created by djenanewail on 4/9/17.
 */
public interface UserTransformer extends CommonTransformer<UserDTO, UserEntity>
{
	UserDTO getUserCoverAndAvatar(UserEntity targetUser);
	
	UserEntity signUpDtoToEntity(SignUpDTO dto);
}
