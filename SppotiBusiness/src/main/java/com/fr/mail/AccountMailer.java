package com.fr.mail;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.utils.SppotiUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wdjenane on 19/01/2017.
 * <p>
 * This email class:
 * 1- Activate account.
 * 2- Recover password.
 * 3- Confirm email reset.
 **/
@Component
public class AccountMailer extends ApplicationMailer
{
	
	// Password recover
	@Value("${spring.app.mail.account.recover.path}")
	private String pathToRecoverAccount;
	@Value("${spring.app.mail.account.recover.message}")
	private String recoverAccountMessage;
	@Value("${spring.app.mail.account.recover.subject}")
	private String recoverAccountSubject;
	@Value("${spring.app.mail.account.recover.button}")
	private String recoverAccountButtonText;
	@Value("${spring.app.mail.account.recover.information}")
	private String recoverAccountConcatUsMessage;
	
	// path to activate account and validate new email address
	@Value("${spring.app.mail.account.confirmation.path}")
	private String pathToValidateAccount;
	@Value("${spring.app.mail.account.confirmation.message}")
	private String confirmationAccountMessage;
	@Value("${spring.app.mail.account.confirmation.subject}")
	private String confirmationAccountSubject;
	@Value("${spring.app.mail.account.confirmation.button}")
	private String confirmationAccountButtonText;
	
	/**
	 * Init email params.
	 */
	public AccountMailer(final JavaMailSender sender, final MailProperties mailProperties,
						 final TemplateEngine templateEngine)
	{
		super(sender, mailProperties, templateEngine);
	}
	
	/**
	 * @param to
	 * 		receiver.
	 * @param confirmationCode
	 * 		Send email to user to confirm account
	 */
	public void sendCreateAccountConfirmationEmail(final UserDTO to, final String confirmationCode)
	{
		
		final String activateLink = this.frontRootPath + this.pathToValidateAccount + confirmationCode;
		
		this.prepareAndSendEmail(to, this.confirmationAccountSubject, this.confirmationAccountMessage,
				this.confirmationAccountButtonText, activateLink, 1);
	}
	
	/**
	 * @param to
	 * 		receiver.
	 * @param confirmationCode
	 * 		Send email to use with a token to recover the account.
	 * @param currentDate
	 * 		link expiry date.
	 */
	public void sendRecoverPasswordEmail(final UserDTO to, final String confirmationCode, final Date currentDate)
	{
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final String dateToEncode = formatter.format(currentDate.getTime());
		
		final String recoverLink = this.frontRootPath + this.pathToRecoverAccount + confirmationCode + "/" +
				SppotiUtils.encodeTo64(dateToEncode);
		
		this.prepareAndSendEmail(to, this.recoverAccountSubject, this.recoverAccountMessage,
				this.recoverAccountButtonText, recoverLink, 2);
	}
	
	/**
	 * @param to
	 * 		receiver.
	 * @param confirmationCode
	 * 		send email to user to confirm the new email.
	 */
	public void sendEmailUpdateConfirmation(final String to, final String confirmationCode)
	{
	
	}
	
	/**
	 * Prepare email to send
	 *
	 * @param to
	 * 		receiver.
	 * @param subject
	 * 		email subject.
	 * @param message
	 * 		message to send.
	 * @param buttonText
	 * 		button text.
	 * @param activateLinkTag
	 * 		activation link.
	 */
	public void prepareAndSendEmail(final UserDTO to, final String subject, final String message,
									final String buttonText, final String activateLinkTag, final int op)
	{
		
		final ResourceContent resourceContent = new ResourceContent();
		resourceContent.setPath(IMAGES_DIRECTORY + logoResourceName);
		resourceContent.setResourceName(logoResourceName);
		
		final Context context = new Context();
		context.setVariable("title", to.getFirstName());
		context.setVariable("body", message);
		context.setVariable("buttonLink", activateLinkTag);
		context.setVariable("textButtonLink", buttonText);
		context.setVariable("receiverEmail", to.getEmail());
		context.setVariable("receiverUsername", to.getUsername());
		context.setVariable("imageResourceName", resourceContent.getResourceName());
		
		context.setVariable("recoverAccountConcatUsMessage", this.recoverAccountConcatUsMessage);
		
		//Template footer.
		context.setVariable("emailIntendedForMessageText", this.emailIntendedForMessage);
		context.setVariable("notYourAccountMessageText", this.notYourAccountMessage);
		context.setVariable("contactUsMessageText", this.contactUsMessage);
		context.setVariable("contactUsLink", this.contactUsLink);
		context.setVariable("sentToText", this.sentToTextMessage);
		
		switch (op) {
			case 1:
				context.setVariable("isRecoverPassword", false);
				break;
			case 2:
				context.setVariable("isRecoverPassword", true);
				break;
		}
		final String text = this.templateEngine.process(PATH_TO_ACCOUNT_TEMPLATE, context);
		
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
		
	}
}
