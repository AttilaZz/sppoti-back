package com.fr.rest.service;

import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public interface LoginService extends AbstractControllerService{

    UserEntity getByEmail(String username);

    UserEntity getByTelephone(String username);

    UserEntity getByUsername(String username);
}
