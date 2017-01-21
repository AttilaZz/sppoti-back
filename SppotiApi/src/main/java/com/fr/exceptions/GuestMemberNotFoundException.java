package com.fr.exceptions;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 1/21/17.
 */
public class GuestMemberNotFoundException extends EntityNotFoundException{
    public GuestMemberNotFoundException() {
        super();
    }

    public GuestMemberNotFoundException(String message) {
        super(message);
    }
}
