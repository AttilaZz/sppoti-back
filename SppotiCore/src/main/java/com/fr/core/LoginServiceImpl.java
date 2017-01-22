package com.fr.core;

import com.fr.controllers.service.LoginService;
import com.fr.entities.Users;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class LoginServiceImpl extends AbstractControllerServiceImpl implements LoginService {
    @Override
    public Users getByEmail(String username) {
        return userRepository.getByEmail(username);
    }

    @Override
    public Users getByTelephone(String username) {
        return userRepository.getByTelephone(username);
    }

    @Override
    public Users getByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
