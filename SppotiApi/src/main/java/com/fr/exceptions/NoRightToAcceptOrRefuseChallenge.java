package com.fr.exceptions;

/**
 * Created by djenanewail on 2/16/17.
 */
public class NoRightToAcceptOrRefuseChallenge extends RuntimeException{

    public NoRightToAcceptOrRefuseChallenge() {
        super();
    }

    public NoRightToAcceptOrRefuseChallenge(String message) {
        super(message);
    }
}
