package com.fr.controllers.service;

import com.fr.entities.Roles;
import com.fr.entities.Sport;
import com.fr.entities.Users;
import com.fr.models.SignUpRequest;
import com.fr.models.User;
import org.springframework.stereotype.Service;

@Service
public interface AccountControllerService{

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

    User fillUserResponse(Users targetUser);
}
