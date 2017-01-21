package com.fr.exceptions;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 1/21/17.
 */
public class SportNotFoundException extends EntityNotFoundException{

    public SportNotFoundException() {
        super();
    }

    public SportNotFoundException(String message) {
        super(message);
    }
}
