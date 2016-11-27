package com.fr.controllers.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Service
public interface IndexService {
    public boolean logOut(HttpServletRequest request, HttpServletResponse response);
}
