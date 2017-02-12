package com.fr.rest.service;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.Roles;
import com.fr.entities.Sport;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface AccountControllerService extends AbstractControllerService{

    void saveNewUser(UserEntity user) throws Exception;

    Roles getProfileEntity(String profileType);

    Sport getSportById(Long id);

    boolean isReceivedDataNotEmpty(SignUpRequestDTO user);

    boolean tryActivateAccount(String code);

    boolean sendConfirmationEmail(String email, String code);

    boolean updateUser(UserEntity connected_user);

    void unSelectOldResource(Long userId, int i);

    UserEntity getUserByUsername(String username);

    UserEntity getUserById(Long userId);

    UserDTO fillUserResponse(UserEntity targetUser, UserEntity connected_user);
}