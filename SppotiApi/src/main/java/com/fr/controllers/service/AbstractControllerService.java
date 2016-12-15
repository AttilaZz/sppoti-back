package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.entities.Users;

@Service
public interface AbstractControllerService {

    List<String> getUserRole();

    String getAuthenticationUsername();

    Users getUserFromUsernameType(String loginUser);

    int getUserLoginType(String username);

    Users getUserById(Long id);

}
