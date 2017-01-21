package com.fr.exceptions;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 1/21/17.
 */
public class HostMemberNotFoundException extends EntityNotFoundException {

    public HostMemberNotFoundException() {
        super();
    }

    public HostMemberNotFoundException(String message) {
        super(message);
    }
}
