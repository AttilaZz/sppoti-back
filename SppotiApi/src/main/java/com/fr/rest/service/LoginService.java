package com.fr.rest.service;

import com.fr.entities.Users;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public interface LoginService extends AbstractControllerService{

    Users getByEmail(String username);

    Users getByTelephone(String username);

    Users getByUsername(String username);
}
