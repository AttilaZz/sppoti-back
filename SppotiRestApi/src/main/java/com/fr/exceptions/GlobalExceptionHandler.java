package com.fr.exceptions;

import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NotAdminException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 3/7/17.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = NotAdminException.class)
    public ResponseEntity notTeamAdmin(NotAdminException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(value = BusinessGlobalException.class)
    public ResponseEntity globalException(BusinessGlobalException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity globalException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
