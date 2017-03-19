package com.fr.rest.service;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.RoleEntity;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface AccountControllerService extends AbstractControllerService{

    void saveNewUser(SignUpRequestDTO user);

    RoleEntity getProfileEntity(String profileType);

    boolean tryActivateAccount(String code);

    boolean updateUser(UserEntity connected_user);

    void unSelectOldResource(Long userId, int i);

    UserEntity getUserByUsername(String username);

    UserDTO fillUserResponse(UserEntity targetUser, UserEntity connected_user);
}