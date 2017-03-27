package com.fr.commons.exception;

/**
 * Created by djenanewail on 1/21/17.
 */
public class TeamMemberNotFoundException extends RuntimeException {

    public TeamMemberNotFoundException() {
        super();
    }

    public TeamMemberNotFoundException(String message) {
        super(message);
    }
}
