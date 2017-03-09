package com.fr.exceptions;

/**
 * Created by djenanewail on 3/9/17.
 */
public class BusinessGlobalException extends RuntimeException{

    public BusinessGlobalException() {
        super();
    }

    public BusinessGlobalException(String message) {
        super(message);
    }
}
