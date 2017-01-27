package com.fr.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
