package com.fr.security;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;
import com.fr.repositories.UserRepository;
import com.fr.transformers.UserTransformer;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.fr.filter.HeadersAttributes.*;
import static com.fr.filter.HeadersValues.*;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
public class AuthSuccess extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * Class logger.
     */
    private static Logger LOGGER = Logger.getLogger(AuthSuccess.class);

    /**
     * USer repository.
     */
    private final UserRepository userRepository;

    /**
     * User transformer.
     */
    private final UserTransformer userTransformer;

    /**
     * Init repository.
     */
    @Autowired
    public AuthSuccess(UserRepository userRepository, UserTransformer userTransformer) {
        this.userRepository = userRepository;
        this.userTransformer = userTransformer;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        LOGGER.info("UserDTO has been logged :-)");

        response.setHeader(ATTR_ORIGIN.getValue(), Origins.getValue());
        response.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
        response.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
        response.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
        response.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        //Get connected user data.
        UserEntity users = userRepository.getByIdAndDeletedFalse(accountUserDetails.getId());
        UserDTO user = userTransformer.modelToDto(users);
        user.setPassword(null);

        //Save new connexion date and ip address.
        users.getIpHistory().put(new Date(), request.getRemoteAddr());
        userRepository.save(users);

        //Convert data to json.
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(user));

        //Return OK status.
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
