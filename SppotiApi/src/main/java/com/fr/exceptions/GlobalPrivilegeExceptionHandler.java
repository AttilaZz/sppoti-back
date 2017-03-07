package com.fr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by djenanewail on 3/7/17.
 */

@ControllerAdvice
public class GlobalPrivilegeExceptionHandler {

    @ExceptionHandler(value = NotTeamAdminException.class)
    public ResponseEntity notTeamAdmin(NotTeamAdminException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

}
