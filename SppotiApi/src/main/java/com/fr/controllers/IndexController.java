package com.fr.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fr.filter.HeadersValues;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@Controller
public class IndexController {


    private static Logger LOGGER = Logger.getLogger(IndexController.class);

    @GetMapping(value = {"/", "/login"})
    public String homePage() {
        return "index";
    }



}
