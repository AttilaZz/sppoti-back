package com.fr.rest.service;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.RoleEntity;
import com.fr.entities.SportEntity;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface AccountControllerService extends AbstractControllerService{

    void saveNewUser(UserEntity user) throws Exception;

    RoleEntity getProfileEntity(String profileType);

    SportEntity getSportById(Long id);

    boolean isReceivedDataNotEmpty(SignUpRequestDTO user);

    boolean tryActivateAccount(String code);

    boolean sendConfirmationEmail(String email, String code);

    boolean updateUser(UserEntity connected_user);

    void unSelectOldResource(Long userId, int i);

    UserEntity getUserByUsername(String username);

    UserDTO fillUserResponse(UserEntity targetUser, UserEntity connected_user);
}