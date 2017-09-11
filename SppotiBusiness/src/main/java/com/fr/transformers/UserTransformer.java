package com.fr.transformers;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 4/9/17.
 */
@Service
public interface UserTransformer extends CommonTransformer<UserDTO, UserEntity>
{
	UserDTO getUserCoverAndAvatar(UserEntity targetUser);
	
	UserEntity signUpDtoToEntity(SignUpDTO dto);
}
