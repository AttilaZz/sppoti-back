package com.fr.mail;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Created by djenanewail on 3/23/17.
 * <p>
 * Send emails about sppoti activities.
 */
@Component
public class SppotiMailer extends ApplicationMailer
{
	
	/** Notify sppoti admin about his ne sppoti. */
	@Value("${spring.app.mail.sppoti.add.message}")
	private String addSppotiMessage;
	@Value("${spring.app.mail.sppoti.add.subject}")
	private String addSppotiSubject;
	
	/** Notify a sppoter about sppoti invitation. */
	@Value("${spring.app.mail.sppoti.join.message}")
	private String joinSppotiMessage;
	@Value("${spring.app.mail.sppoti.join.subject}")
	private String joinSppotiSubject;
	
	/** Notify sppoti admin when an invitation is accepted or refused. */
	@Value("${spring.app.mail.sppoti.confirm.message}")
	private String confirmJoinSppotiMessage;
	@Value("${spring.app.mail.sppoti.confirm.subject}")
	private String confirmJoinSppotiSubject;
	
	/** Redirection link to the front app. */
	@Value("${spring.app.mail.sppoti.join.link}")
	private String joinSppotiLink;
	
	/**
	 * Init mail configuration.
	 */
	@Autowired
	public SppotiMailer(final JavaMailSender sender, final MailProperties mailProperties,
						final TemplateEngine templateEngine)
	{
		super(sender, mailProperties, templateEngine);
	}
	
	
	/**
	 * Send email to confirm Sppoti creation.
	 *
	 * @param Sppoti
	 * 		ceated Sppoti.
	 */
	public void sendAddSppotiEmail(final SppotiDTO Sppoti)
	{
	
	}
	
	/**
	 * Send email to the Sppoti members.
	 *
	 * @param to
	 * 		sppoti memeber.
	 * @param from
	 * 		sppoti admin.
	 * @param sppoti
	 * 		sppoti data.
	 */
	public void sendJoinSppotiEmail(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		
		final ResourceContent resourceContent = new ResourceContent();
		resourceContent.setPath(IMAGES_DIRECTORY + teamDefaultAvatarResourceName);
		resourceContent.setResourceName(teamDefaultAvatarResourceName);
		
		final String joinSppotiLinkParsed = this.frontRootPath +
				this.joinSppotiLink.replace("%SppotiId%", sppoti.getId() + "");
		prepareAndSendEmail(to, from, sppoti, this.joinSppotiSubject, this.joinSppotiMessage, joinSppotiLinkParsed,
				resourceContent);
	}
	
	/**
	 * Send email to confirm Sppoti sppoti.
	 *
	 * @param sppoti
	 * 		sppoti data.
	 * @param sppoter
	 * 		sppoti member.
	 */
	public void sendConfirmJoinSppotiEmail(final SppotiDTO sppoti, final UserDTO sppoter)
	{
	
	}
	
	/**
	 * Send email.
	 */
	private void prepareAndSendEmail(final UserDTO to, final UserDTO from, final SppotiDTO Sppoti, final String subject,
									 final String content, final String joinSppotiLink,
									 final ResourceContent resourceContent)
	{
		
		final Context context = new Context();
		context.setVariable("title", to.getFirstName());
		
		context.setVariable("sentFromName", from.getFirstName() + " " + from.getLastName());
		
		if (!StringUtils.isEmpty(joinSppotiLink)) {
			context.setVariable("sentToValidationLink", joinSppotiLink);
		}
		
		context.setVariable("body", content);
		context.setVariable("sentToSppotiName", Sppoti.getName());
		context.setVariable("sentToFirstName", to.getFirstName());
		context.setVariable("sentToEmail", to.getEmail());
		context.setVariable("sentToUsername", to.getUsername());
		
		//Template Footer.
		context.setVariable("emailIntendedForMessageText", this.emailIntendedForMessage);
		context.setVariable("notYourAccountMessageText", this.notYourAccountMessage);
		context.setVariable("contactUsMessageText", this.contactUsMessage);
		context.setVariable("contactUsLink", this.contactUsLink);
		context.setVariable("sentToText", this.sentToTextMessage);
		
		final String text = this.templateEngine.process(PATH_TO_SPPOTI_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
	}
	
}
