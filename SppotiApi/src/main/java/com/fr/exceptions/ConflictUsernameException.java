package com.fr.exceptions;

/**
 * Created by djenanewail on 12/10/16.
 */
public class ConflictUsernameException extends Exception {

    public ConflictUsernameException() {
        super();
    }

    public ConflictUsernameException(String msg) {
        super(msg);
    }

}
