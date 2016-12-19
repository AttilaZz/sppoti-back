package com.fr.controllers.service;

import com.fr.entities.Roles;
import com.fr.entities.Sport;
import com.fr.entities.Users;
import com.fr.models.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface SignUpService {

    void saveNewUser(Users user) throws Exception;

    Roles getProfileEntity(String profileType);

    Sport getSportById(Long id);

    boolean isReceivedDataNotEmpty(SignUpRequest user);

    boolean tryActivateAccount(String code);

    boolean sendConfirmationEmail(String email, String code);

    Users getUserById(int id);

    boolean updateUser(Users connected_user);
}
