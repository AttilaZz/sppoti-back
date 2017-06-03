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
 *
 * Send emails about sppoti activities.
 */
@Component
public class SppotiMailer extends ApplicationMailer
{
	
	/** Sppoti mail templates */
	private final static String PATH_TO_JOIN_SPPOTI_TEMPLATE = "sppoti/join_sppoti";
	private final static String PATH_TO_CREATE_SPPOTI_TEMPLATE = "sppoti/create_sppoti";
	private final static String PATH_TO_RESPOND_TO_SPPOTI_TEMPLATE = "sppoti/respond_sppoti";
	/** Notify sppoti admin about his ne sppoti. */
	@Value("${spring.app.mail.sppoti.add.subject}")
	private String addSppotiSubject;
	/** Notify a sppoter about sppoti invitation. */
	@Value("${spring.app.mail.sppoti.join.subject}")
	private String joinSppotiSubject;
	/** Notify sppoti admin when an invitation is accepted or refused. */
	@Value("${spring.app.mail.sppoti.confirm.subject}")
	private String confirmJoinSppotiSubject;
	/** Redirection link to the front app. */
	@Value("${spring.app.mail.sppoti.join.link}")
	private String joinSppotiLink;
	/** Explain sppoti concept. */
	@Value("${spring.app.mail.sppoti.description}")
	private String sppotiConcept;
	/** translate to join sppoti message. */
	@Value("${spring.app.mail.sppoti.invited.by.join.sppoti}")
	private String toJoinSppotiMessage;
	
	/** Init mail configuration. */
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
		
		final ResourceContent avatarResourceContent = new ResourceContent(), coverResourceContent
				= new ResourceContent();
		avatarResourceContent.setPath(IMAGES_DIRECTORY + teamDefaultAvatarResourceName);
		avatarResourceContent.setResourceName(teamDefaultAvatarResourceName);
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
		coverResourceContent.setResourceName(sppotiCoverResourceName);
		
		final String joinSppotiLinkParsed = this.frontRootPath +
				this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
		prepareAndSendEmail(to, from, sppoti, this.joinSppotiSubject, joinSppotiLinkParsed, coverResourceContent,
				avatarResourceContent);
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
									 final String joinSppotiLink, final ResourceContent... resourceContent)
	{
		
		final Context context = new Context();
		context.setVariable("title", to.getFirstName());
		
		context.setVariable("sentFromName", from.getFirstName() + " " + from.getLastName());
		
		if (!StringUtils.isEmpty(joinSppotiLink)) {
			context.setVariable("sentToValidationLink", joinSppotiLink);
		}
		
		context.setVariable("sentToSppotiName", Sppoti.getName());
		context.setVariable("sentToFirstName", to.getFirstName());
		context.setVariable("sentToEmail", to.getEmail());
		context.setVariable("sentToUsername", to.getUsername());
		
		context.setVariable("coverResourceName", resourceContent[0].getResourceName());
		context.setVariable("avatarResourceName", resourceContent[1].getResourceName());
		
		context.setVariable("toJoinSppotiMessage", this.toJoinSppotiMessage);
		context.setVariable("globalInformationAboutSppoti", this.sppotiConcept);
		
		context.setVariable("learnMoreMessage", this.learnMoreMessage);
		context.setVariable("joinMessage", this.joinMessage);
		context.setVariable("invitedByMessage", this.invitedByMessage);
		context.setVariable("andPrepositionMessage", this.andPrepositionMessage);
		context.setVariable("otherPrepositionMessage", this.otherPrepositionMessage);
		
		//Template Footer.
		context.setVariable("emailIntendedForMessageText", this.emailIntendedForMessage);
		context.setVariable("notYourAccountMessageText", this.notYourAccountMessage);
		context.setVariable("contactUsMessageText", this.contactUsMessage);
		context.setVariable("contactUsLink", this.contactUsLink);
		context.setVariable("sentToText", this.sentToTextMessage);
		
		final String text = this.templateEngine.process(PATH_TO_JOIN_SPPOTI_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
	}
	
}
