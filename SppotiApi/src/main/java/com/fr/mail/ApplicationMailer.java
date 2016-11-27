/**
 * 
 */
package com.fr.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Created by: Wail DJENANE on Aug 7, 2016
 */
@Component("mailService")
public class ApplicationMailer {

	@Autowired
	private MailSender mailSender;

	@Autowired
	private SimpleMailMessage preConfiguredMessage;

	@Autowired
	private JavaMailSenderImpl sender;

	/**
	 * This method will send compose and send the message
	 */
	public void sendMail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		mailSender.send(message);
	}

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
		// File("C:/Users/Moi/Desktop/dhak.jpg"));
		// helper.addInline("identifier1234", res);

		sender.send(message);
	}

	/**
	 * This method will send a pre-configured message
	 */
	public void sendPreConfiguredMail(String message) {
		SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
		mailMessage.setText(message);
		mailSender.send(mailMessage);
	}
}
