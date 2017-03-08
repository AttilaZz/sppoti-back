package com.fr.exceptions;

/**
 * Created by djenanewail on 2/23/17.
 */
public class NotAdminException extends RuntimeException{

    public NotAdminException() {
        super();
    }

    public NotAdminException(String message) {
        super(message);
    }
}
