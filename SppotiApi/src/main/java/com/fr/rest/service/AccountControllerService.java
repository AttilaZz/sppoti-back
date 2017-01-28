package com.fr.rest.service;

import com.fr.commons.dto.SignUpRequest;
import com.fr.commons.dto.User;
import com.fr.entities.Roles;
import com.fr.entities.Sport;
import com.fr.entities.Users;
import org.springframework.stereotype.Component;

@Component
public interface AccountControllerService extends AbstractControllerService{

    void saveNewUser(Users user) throws Exception;

    Roles getProfileEntity(String profileType);

    Sport getSportById(Long id);

    boolean isReceivedDataNotEmpty(SignUpRequest user);

    boolean tryActivateAccount(String code);

    boolean sendConfirmationEmail(String email, String code);

    boolean updateUser(Users connected_user);

    void unSelectOldResource(Long userId, int i);

    Users getUserByUsername(String username);

    Users getUserById(Long userId);

    User fillUserResponse(Users targetUser, Users connected_user);
}