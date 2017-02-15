package com.fr.exceptions;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 1/21/17.
 */
public class TeamMemberNotFoundException extends EntityNotFoundException {

    public TeamMemberNotFoundException() {
        super();
    }

    public TeamMemberNotFoundException(String message) {
        super(message);
    }
}
