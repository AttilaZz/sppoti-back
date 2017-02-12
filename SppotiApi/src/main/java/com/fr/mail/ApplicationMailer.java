/**
 *
 */
package com.fr.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by: Wail DJENANE on Aug 7, 2016
 */
@Component
public class ApplicationMailer {


    @Autowired
    private JavaMailSenderImpl sender;

    /**
     * This method will send compose and send the message
     */

    public void sendMail2(String to, String subject, String body) throws MessagingException {

        // sender.setHost("mail.host.com");

        MimeMessage message = sender.createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        helper.setTo(to);

        // use the true flag to indicate the text included is HTML
        helper.setText(body, true);

        helper.setSubject(subject);
        // let's include the infamous windows Sample file (this time copied to
        // c:/)
        // FileSystemResource res = new FileSystemResource(new
        // File("C:/UserEntity/Moi/Desktop/dhak.jpg"));
        // helper.addInline("identifier1234", res);

        sender.send(message);
    }

}
