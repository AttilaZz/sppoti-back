package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.SppotiMailerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Created by djenanewail on 3/23/17.
 */
@Component
public class SppotiMailerImpl extends ApplicationMailerServiceImpl implements SppotiMailerService
{
	
	/** Sppoti email templates */
	private final static String PATH_TO_JOIN_SPPOTI_TEMPLATE = "sppoti/join_sppoti";
	private final static String PATH_TO_CREATE_SPPOTI_TEMPLATE = "sppoti/create_sppoti";
	private final static String PATH_TO_RESPOND_TO_SPPOTI_TEMPLATE = "sppoti/respond_sppoti";
	
	@Value("${spring.app.mail.sppoti.add.subject}")
	private String addSppotiSubject;
	
	@Value("${spring.app.mail.sppoti.join.subject}")
	private String joinSppotiSubject;
	
	@Value("${spring.app.mail.sppoti.confirm.subject}")
	private String confirmJoinSppotiSubject;
	
	@Value("${spring.app.mail.sppoti.join.link}")
	private String joinSppotiLink;
	
	@Value("${spring.app.mail.sppoti.description}")
	private String sppotiConcept;
	
	@Value("${spring.app.mail.sppoti.invited.by.join.sppoti}")
	private String toJoinSppotiMessage;
	
	private final TemplateEngine templateEngine;
	
	private final UserParamService userParamService;
	
	@Autowired
	public SppotiMailerImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	/**
	 * Send email to confirm Sppoti creation.
	 *
	 * @param Sppoti
	 * 		ceated Sppoti.
	 */
	@Override
	public void sendAddSppotiEmail(final SppotiDTO Sppoti)
	{
		if (this.userParamService.canReceiveEmail()) {
		
		}
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
	@Override
	public void sendJoinSppotiEmail(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		if (this.userParamService.canReceiveEmail()) {
			final MailResourceContent avatarResourceContent = new MailResourceContent(), coverResourceContent
					= new MailResourceContent();
			avatarResourceContent.setPath(IMAGES_DIRECTORY + teamDefaultAvatarResourceName);
			avatarResourceContent.setResourceName(teamDefaultAvatarResourceName);
			
			coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
			coverResourceContent.setResourceName(sppotiCoverResourceName);
			
			final String joinSppotiLinkParsed = this.frontRootPath +
					this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
			prepareAndSendEmail(to, from, sppoti, this.joinSppotiSubject, joinSppotiLinkParsed, coverResourceContent,
					avatarResourceContent);
		}
	}
	
	/**
	 * Send email to confirm Sppoti sppoti.
	 *
	 * @param sppoti
	 * 		sppoti data.
	 * @param sppoter
	 * 		sppoti member.
	 */
	@Override
	public void sendConfirmJoinSppotiEmail(final SppotiDTO sppoti, final UserDTO sppoter)
	{
		if (this.userParamService.canReceiveEmail()) {
		
		}
	}
	
	/**
	 * Send email.
	 */
	private void prepareAndSendEmail(final UserDTO to, final UserDTO from, final SppotiDTO Sppoti, final String subject,
									 final String joinSppotiLink, final MailResourceContent... resourceContent)
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
