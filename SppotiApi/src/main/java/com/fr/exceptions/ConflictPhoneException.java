package com.fr.exceptions;

/**
 * Created by djenanewail on 12/10/16.
 */
public class ConflictPhoneException extends RuntimeException {

    public ConflictPhoneException() {
        super();
    }

    public ConflictPhoneException(String msg) {
        super(msg);
    }

}
