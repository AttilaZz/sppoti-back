package com.fr.rest.service;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface AccountControllerService extends AbstractControllerService{

    void saveNewUser(SignUpRequestDTO user);
    
    boolean tryActivateAccount(String code);

    void updateUser(UserDTO userDTO);

    void unSelectOldResource(Long userId, int i);

    UserEntity getUserByUsername(String username);

    UserDTO fillUserResponse(UserEntity targetUser, UserEntity connected_user);

    void updateAvatarAndCover(UserDTO user);
}