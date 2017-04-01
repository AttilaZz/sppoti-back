package com.fr.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by wdjenane on 09/02/2017.
 * <p>
 * Email super class configuration.
 **/
@Component
abstract class ApplicationMailer {

    protected static final String PATH_TO_ACCOUNT_TEMPLATE = "account/account";
    protected static final String PATH_TO_TEAM_TEMPLATE = "team/team";
    protected static final String CHARSET_NAME = "UTF-8";
    protected static final String ERROR_SENDING_MAIL = "Error sending email";

    protected static Logger LOGGER = LoggerFactory.getLogger(ApplicationMailer.class);

    final JavaMailSender sender;
    final MailProperties mailProperties;
    final TemplateEngine templateEngine;

    @Value("${spring.app.originFront}")
    protected String frontRootPath;

    @Autowired
    public ApplicationMailer(JavaMailSender sender, MailProperties mailProperties, TemplateEngine templateEngine) {
        this.sender = sender;
        this.mailProperties = mailProperties;
        this.templateEngine = templateEngine;
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

            helper.setFrom(mailProperties.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);

            helper.setText(content, true);

            this.sender.send(mail);

        } catch (final MessagingException e) {
            LOGGER.error(ERROR_SENDING_MAIL, e);
        }
    }

}
