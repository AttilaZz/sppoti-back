package com.fr.controllers;

import com.fr.controllers.serviceImpl.AbstractControllerServiceImpl;
import com.fr.entities.Sport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Djenane Wail on 12/10/16.
 */

@RestController
public class SportController extends AbstractControllerServiceImpl {

    @GetMapping(value = "/sport/all")
    public ResponseEntity<Object> getAllSports() {

        List<Sport> allSports = sportRepository.findAll();

        if (allSports.isEmpty()) {
            return new ResponseEntity<>(allSports, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(allSports, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/sport")
    public ResponseEntity<Object> addSports() {

        return new ResponseEntity<>(HttpStatus.OK);

    }

}