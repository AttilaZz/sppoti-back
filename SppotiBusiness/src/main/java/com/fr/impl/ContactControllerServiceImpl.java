package com.fr.impl;

import com.fr.commons.dto.ContactDTO;
import com.fr.service.ContactControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 4/8/17.
 */

@Component
public class ContactControllerServiceImpl extends AbstractControllerServiceImpl implements ContactControllerService {

    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(ContactControllerServiceImpl.class);

    /**
     * Email contact to which send the information.
     */
    @Value("${spring.app.contact.email}")
    private String emailContact;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendContactEmail(ContactDTO contactDTO) {

    }
}
