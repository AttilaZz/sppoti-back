package com.fr.service;

import com.fr.commons.dto.ContactDTO;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 4/8/17.
 */
@Service
public interface ContactControllerService extends AbstractControllerService{

    /**
     * Contact sppoti admins for a bug or information.
     *
     * @param contactDTO contact data.
     */
    void sendContactEmail(ContactDTO contactDTO);
}
