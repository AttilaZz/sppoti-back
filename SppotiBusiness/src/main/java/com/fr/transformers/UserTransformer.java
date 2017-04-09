package com.fr.transformers;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 4/9/17.
 */

@Service
public interface UserTransformer extends CommonTransformer<UserDTO, UserEntity> {

    /**
     * Transform sign up DTO to user entity.
     *
     * @param dto sign up DTO.
     * @return user Entity.
     */
    UserEntity signUpDtoToEntity(SignUpRequestDTO dto);
}
