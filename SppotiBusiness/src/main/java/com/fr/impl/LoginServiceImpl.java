package com.fr.impl;

import com.fr.entities.UserEntity;
import com.fr.service.LoginService;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
class LoginServiceImpl extends AbstractControllerServiceImpl implements LoginService {
    @Override
    public UserEntity getByEmail(String username) {
        return userRepository.getByEmailAndDeletedFalse(username);
    }

    @Override
    public UserEntity getByTelephone(String username) {
        return userRepository.getByTelephoneAndDeletedFalse(username);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.getByUsernameAndDeletedFalse(username);
    }
}
