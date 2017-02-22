package com.fr.exceptions;

/**
 * Created by djenanewail on 2/23/17.
 */
public class NotTeamAdminException extends RuntimeException{

    public NotTeamAdminException() {
        super();
    }

    public NotTeamAdminException(String message) {
        super(message);
    }
}
