package com.fr.exceptions;

/**
 * Created by Moi on 02-Dec-16.
 */
public class ConflictEmailException extends Exception {

    public ConflictEmailException() {
        super();
    }

    public ConflictEmailException(String message) {
        super(message);
    }
}
