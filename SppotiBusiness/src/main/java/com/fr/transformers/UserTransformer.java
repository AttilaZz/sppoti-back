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
	
	/**
	 * Get selected avatr and cover for the given user.
	 *
	 * @param targetUser
	 * 		user entity to map.
	 *
	 * @return userDto with only avatar and cover.
	 */
	UserDTO getUserCoverAndAvatar(UserEntity targetUser);
	
	/**
	 * Transform sign up DTO to user entity.
	 *
	 * @param dto
	 * 		sign up DTO.
	 *
	 * @return user Entity.
	 */
	UserEntity signUpDtoToEntity(SignUpDTO dto);
}
