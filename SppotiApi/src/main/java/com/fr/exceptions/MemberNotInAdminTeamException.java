package com.fr.exceptions;

/**
 * Created by djenanewail on 2/5/17.
 */
public class MemberNotInAdminTeamException extends RuntimeException{

    public MemberNotInAdminTeamException() {
        super();
    }

    public MemberNotInAdminTeamException(String message) {
        super(message);
    }
}
