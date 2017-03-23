package com.fr.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 *
 * Created by wdjenane on 09/02/2017.
 * <p>
 * Cette classe contient les paramètres commun d'envoi de mail
 **/
@Component
public abstract class ApplicationMailer {

    protected static final String UN_PROBLÈME_A_SURVENU_LORS_DE_L_ENVOI_DU_MAIL_VEUILLEZ_RÉESSAYER_DANS_QUELQUES_INSTANTS = "Un problème a survenu lors de l'envoi de l'email de confirmation, Veuillez réessayer dans quelques instants";
    protected static final String PATH_TO_EMAIL_TEMPLATE = "email/email_template.html";
    protected static final String CHARSET_NAME = "UTF-8";
    protected static final String LECTURE_TEMPLATE_EMAIL_IMPOSSIBLE = "Impossible de lire le fichier de modèle de mail";
    protected static final String ERREUR_D_ENVOI_DE_MAIL = "Erreur d'envoi de mail: ";
    protected static Logger LOGGER = LoggerFactory.getLogger(ApplicationMailer.class);

    private final JavaMailSender sender;

    @Autowired
    public ApplicationMailer(JavaMailSender sender) {
        this.sender = sender;
    }

    /**
     * @param to      email receiver.
     * @param subject email subject.
     * @param content email content.
     */
    protected void prepareAndSendEmail(final String to, final String subject, final String content) {

        final MimeMessage mail = this.sender.createMimeMessage();

        try {
            final MimeMessageHelper helper = new MimeMessageHelper(mail, false, CHARSET_NAME);

            helper.setTo(to);
            helper.setSubject(subject);

            helper.setText(content, true);

            this.sender.send(mail);

        } catch (final MessagingException e) {
            LOGGER.error(ERREUR_D_ENVOI_DE_MAIL, e);
        }
    }

}
