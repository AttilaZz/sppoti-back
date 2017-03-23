package com.fr.mail;

import com.fr.exceptions.BusinessGlobalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by wdjenane on 19/01/2017.
 * <p>
 * Cette classe Envoie les emails suivant:
 * 1- Activation de compte
 * 2- Récupération de compte
 * 3- Confirmation du changement d'adresse email principale
 **/
@Component
public class AccountMailer
    extends ApplicationMailer
{

    private static final String ACTIVATE_ACCOUNT_BUTTON_TEXT = "ACTIVER LE COMPTE";
    private static final String RECOVER_ACCOUNT_BUTTON_TEXT = "RÉCUPÉRER LE COMPTE";
    private static final String VALIDATE_EMAIL_BUTTON_TEXT = "VALIDER LA NOUVELLLE ADRESSE";
    // public static final String ACTIVATION_ACCOUNT_MESSAGE_BODY = "Veuillez cliquer sur le boutton
    // ci-dessous pour activer votre compte REGISTRE";

    // Password recover
    @Value("${spring.app.mail.account.path.recover}")
    private String pathToRecoverAccount;
    @Value("${spring.app.mail.account.recover.message}")
    private String recoverPasswordMessage;
    @Value("${spring.app.mail.account.recover.subject}")
    private String recoverAccountSubject;

    // path to activate account and validate new email address
    @Value("${spring.app.mail.account.path.validate.account}")
    private String pathToValidateAccount;
    // Account confirmation
    @Value("${spring.app.mail.account.confirmation.message}")
    private String confirmationAccountMessage;
    @Value("${spring.app.mail.account.confirmation.subject}")
    private String confirmationAccountSubject;

    @Value("${spring.app.mail.account.path.validate.email}")
    private String pathToValidateEmail;
    // Email update confirmation
    @Value("${spring.app.mail.update_email.confirmation.message}")
    private String updateAccountEmailMessage;
    @Value("${spring.app.mail.update_email.confirmation.subject}")
    private String updateAccountEmailSubject;

    @Value("${spring.app.originFront}")
    private String frontRootPath;

    public AccountMailer(JavaMailSender sender) {
        super(sender);
    }

    /**
     * @param to receiver.
     * @param confirmationCode Send email to user to confirm account
     */
    public void sendAccountConfirmationEmail(final String to, final String confirmationCode)
    {

        final String activateLink = this.frontRootPath + this.pathToValidateAccount;
        final String activateLinkTag = "<a target='_blank' href='" + activateLink + "'>"
                + ACTIVATE_ACCOUNT_BUTTON_TEXT + "</a>";

        this.prepareAndSendEmail(to, this.confirmationAccountSubject,
                this.confirmationAccountMessage, PATH_TO_EMAIL_TEMPLATE, activateLinkTag);
    }

    /**
     * @param to récepteur.
     * @param confirmationCode send email to user to confirm the new email
     */
    public void sendEmailUpdateConfirmation(final String to, final String confirmationCode)
    {

    }

    /**
     * @param to récepteur.
     * @param confirmationCode Send email to use with a token to recover the account
     */
    public void sendRecoverPasswordEmail(final String to, final String confirmationCode)
    {

    }

    /**
     * Prepare email to send
     *
     * @param to récepteur.
     * @param subject sujet.
     * @param message message à envoyer.
     * @param mailFile fichier de template.
     * @param activateLinkTag lien d'activation.
     */
    private void prepareAndSendEmail(final String to, final String subject, final String message,
            final String mailFile, final String activateLinkTag)
    {

        String content = null;
        try {

            content = new Scanner(new ClassPathResource(mailFile).getInputStream(), CHARSET_NAME)
                    .useDelimiter("\\Z").next();

            // content = content.replaceAll("(.*)LINK_HATVP_EMAIL_MESSAGE(.*)",
            // ACTIVATION_ACCOUNT_MESSAGE_BODY);
            content = content.replaceAll("(.*)LINK_HATVP_EMAIL_BUTTON(.*)", activateLinkTag);

            Thread.sleep(2000);

            content = content.replaceAll("(.*)LINK_HATVP_EMAIL_MESSAGE(.*)", message);

        }
        catch (final IOException e) {

            LOGGER.error(LECTURE_TEMPLATE_EMAIL_IMPOSSIBLE, e);
            throw new BusinessGlobalException(
                    UN_PROBLÈME_A_SURVENU_LORS_DE_L_ENVOI_DU_MAIL_VEUILLEZ_RÉESSAYER_DANS_QUELQUES_INSTANTS);

        }
        catch (final InterruptedException e) {
            LOGGER.error("Problème dans l'exécution du thread d'attente", e);
        }
        super.prepareAndSendEmail(to, subject, content);

    }

}
