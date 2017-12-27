package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.TypeAccountValidation;
import com.fr.commons.utils.SppotiUtils;
import com.fr.service.email.AccountMailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wdjenane on 19/01/2017.
 * <p>
 * This email class:
 * 1- Activate account.
 * 2- Recover password.
 * 3- Confirm email reset.
 **/
@Component
public class AccountMailerImplServiceImpl extends ApplicationMailerServiceImpl implements AccountMailerService
{
	private static final String PATH_TO_ACCOUNT_TEMPLATE = "account/account";
	
	@Value("${mail.accountRecoverPath}")
	private String pathToRecoverAccount;
	@Value("${mail.accountConfirmationPath}")
	private String pathToValidateAccount;
	
	private final TemplateEngine templateEngine;
	
	@Autowired
	public AccountMailerImplServiceImpl(final TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}
	
	@Override
	public void sendCreateAccountConfirmationEmail(final UserDTO to, final String confirmationCode,
												   final TypeAccountValidation type)
	{
		final String activateLink = this.frontRootPath + this.pathToValidateAccount + confirmationCode + "/" + type;
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		final String subject = this.messageSource.getMessage("mail.accountConfirmationSubject", null, language);
		final String content = this.messageSource.getMessage("mail.accountConfirmationMessage", null, language);
		final String buttonText = this.messageSource.getMessage("mail.accountConfirmationButton", null, language);
		
		this.prepareAndSendEmail(to, subject, content, buttonText, activateLink, 1);
	}
	
	@Override
	public void sendRecoverPasswordEmail(final UserDTO to, final String confirmationCode, final Date currentDate)
	{
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final String dateToEncode = formatter.format(currentDate.getTime());
		
		final String recoverLink = this.frontRootPath + this.pathToRecoverAccount + confirmationCode + "/" +
				SppotiUtils.encodeTo64(dateToEncode);
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		final String subject = this.messageSource.getMessage("mail.accountRecoverSubject", null, language);
		final String content = this.messageSource.getMessage("mail.accountRecoverMessage", null, language);
		final String buttonText = this.messageSource.getMessage("mail.accountRecoverButton", null, language);
		
		this.prepareAndSendEmail(to, subject, content, buttonText, recoverLink, 2);
	}
	
	@Override
	public void sendEmailUpdateConfirmation(final String to, final String confirmationCode)
	{
		
	}
	
	private void prepareAndSendEmail(final UserDTO to, final String subject, final String message,
									 final String buttonText, final String activateLinkTag, final int op)
	{
		
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent resourceContent = new MailResourceContent();
		resourceContent.setPath(IMAGES_DIRECTORY + SPPOTI_LOGO_RESOURCE_NAME);
		resourceContent.setResourceName(SPPOTI_LOGO_RESOURCE_NAME);
		resourceContents.add(resourceContent);
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguage()));
		context.setVariable("firstName", to.getFirstName());
		context.setVariable("body", message);
		context.setVariable("buttonLink", activateLinkTag);
		context.setVariable("textButtonLink", buttonText);
		context.setVariable("receiverEmail", to.getEmail());
		context.setVariable("receiverUsername", to.getUsername());
		context.setVariable("imageResourceName", resourceContent.getResourceName());
		
		//Template footer.
		context.setVariable("contactUsLink", this.contactUsLink);
		
		switch (op) {
			case 1:
				context.setVariable("isRecoverPassword", false);
				break;
			case 2:
				context.setVariable("isRecoverPassword", true);
				break;
		}
		final String text = this.templateEngine.process(PATH_TO_ACCOUNT_TEMPLATE, context);
		
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
}
