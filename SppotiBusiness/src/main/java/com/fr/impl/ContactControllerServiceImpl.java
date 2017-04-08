package com.fr.impl;

import com.fr.commons.dto.ContactDTO;
import com.fr.mail.ContactMailer;
import com.fr.service.ContactControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 4/8/17.
 */

@Component
public class ContactControllerServiceImpl extends AbstractControllerServiceImpl implements ContactControllerService {

    /**
     * Contact mailer.
     */
    private final ContactMailer contactMailer;

    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(ContactControllerServiceImpl.class);

    /**
     * Init class dependencies.
     */
    @Autowired
    public ContactControllerServiceImpl(ContactMailer contactMailer) {
        this.contactMailer = contactMailer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendGlobalContactEmail(ContactDTO contactDTO) {
        this.sendContactEmail(contactDTO);
    }

    /**
     * Common method for sending contact emails.
     *
     * @param contact contact data.
     */
    private void sendContactEmail(ContactDTO contact) {
        Thread thread = new Thread(() -> {
            this.contactMailer.sendContactEmail(contact);
            LOGGER.info("Confirmation email has been sent successfully !");
        });
        thread.start();
    }
}
