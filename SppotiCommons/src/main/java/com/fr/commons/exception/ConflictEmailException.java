package com.fr.commons.exception;

/**
 * Created by Moi on 02-Dec-16.
 */
public class ConflictEmailException extends RuntimeException {

    public ConflictEmailException() {
        super();
    }

    public ConflictEmailException(String message) {
        super(message);
    }
}
