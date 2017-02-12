package com.fr.core;

import com.fr.entities.UserEntity;
import com.fr.rest.service.LoginService;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class LoginServiceImpl extends AbstractControllerServiceImpl implements LoginService {
    @Override
    public UserEntity getByEmail(String username) {
        return userRepository.getByEmail(username);
    }

    @Override
    public UserEntity getByTelephone(String username) {
        return userRepository.getByTelephone(username);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
