package com.fr.mail;

import com.fr.commons.dto.UserDTO;
import com.fr.exceptions.BusinessGlobalException;
import org.springframework.beans.factory.annotation.Value;
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
        extends ApplicationMailer {

    // Password recover
    @Value("${spring.app.mail.account.recover.path}")
    private String pathToRecoverAccount;
    @Value("${spring.app.mail.account.recover.message}")
    private String recoverAccountMessage;
    @Value("${spring.app.mail.account.recover.subject}")
    private String recoverAccountSubject;
    @Value("${spring.app.mail.account.recover.button}")
    private String recoverAccountButtonText;

    // path to activate account and validate new email address
    @Value("${spring.app.mail.account.confirmation.path}")
    private String pathToValidateAccount;
    // Account confirmation
    @Value("${spring.app.mail.account.confirmation.message}")
    private String confirmationAccountMessage;
    @Value("${spring.app.mail.account.confirmation.subject}")
    private String confirmationAccountSubject;
    @Value("${spring.app.mail.account.confirmation.button}")
    private String confirmationAccountButtonText;


    @Value("${spring.app.originFront}")
    private String frontRootPath;

    public AccountMailer(JavaMailSender sender) {
        super(sender);
    }

    /**
     * @param to               receiver.
     * @param confirmationCode Send email to user to confirm account
     */
    public void sendAccountConfirmationEmail(final UserDTO to, final String confirmationCode) {

        final String activateLink = this.frontRootPath + this.pathToValidateAccount + confirmationCode;

        this.prepareAndSendEmail(to, this.confirmationAccountSubject,
                this.confirmationAccountMessage, PATH_TO_ACCOUNT_TEMPLATE,
                this.confirmationAccountButtonText, activateLink);
    }

    /**
     * @param to               receiver.
     * @param confirmationCode Send email to use with a token to recover the account.
     */
    public void sendRecoverPasswordEmail(final UserDTO to, final String confirmationCode) {
        final String recoverLink = this.frontRootPath + this.pathToRecoverAccount + confirmationCode;

        this.prepareAndSendEmail(to, this.recoverAccountSubject,
                this.recoverAccountMessage, PATH_TO_ACCOUNT_TEMPLATE,
                this.recoverAccountButtonText, recoverLink);
    }

    /**
     * @param to               receiver.
     * @param confirmationCode send email to user to confirm the new email.
     */
    public void sendEmailUpdateConfirmation(final String to, final String confirmationCode) {

    }

    /**
     * Prepare email to send
     *
     * @param to              receiver.
     * @param subject         email subject.
     * @param message         message to send.
     * @param mailFile        email tmplate file.
     * @param buttonText      button text.
     * @param activateLinkTag activation link.
     */
    private void prepareAndSendEmail(final UserDTO to, final String subject, final String message,
                                     final String mailFile, final String buttonText, final String activateLinkTag) {

        try {

            String content = new Scanner(new ClassPathResource(mailFile).getInputStream(), CHARSET_NAME)
                    .useDelimiter("\\Z").next();

            String emailTitle = "<h3 class=\"email-title\">" + to.getFirstName() + ", Just one more step...</h3>";
            content = content.replaceAll("(.*)EMAIL_TITLE(.*)", emailTitle);

            Thread.sleep(300);
            String messageToWrite = "<p class=\"lead account-message\" >" + message + "</p>";
            content = content.replaceAll("(.*)ACTIVATE_ACCOUNT_MESSAGE(.*)", messageToWrite);

            Thread.sleep(300);
            String button = "<p class=\"reset button-text\"><a href=\"" + activateLinkTag + "\" class=\"link-account-path\">"
                    + buttonText + "</a></p>";
            content = content.replaceAll("(.*)ACCOUNT_BUTTON_TEXT(.*)", button);

            Thread.sleep(300);
            String notYourAccountMessage = "<span class=\"remove-your-email\"> This message was sent to " + to.getEmail()
                    + " and intended for " + to.getUsername() + ". Not your account? Remove your email from this account.</span>";
            content = content.replaceAll("(.*)REMOVE_YOUR_EMAIL(.*)", notYourAccountMessage);

            super.prepareAndSendEmail(to.getEmail(), subject, content);

        } catch (final IOException e) {
            LOGGER.error(LECTURE_TEMPLATE_EMAIL_IMPOSSIBLE, e);
            throw new BusinessGlobalException(EMAIL_SENDING_PROBLEM);
        } catch (final InterruptedException e) {
            LOGGER.error("Problème dans l'exécution du thread d'attente", e);
        }

    }

}
