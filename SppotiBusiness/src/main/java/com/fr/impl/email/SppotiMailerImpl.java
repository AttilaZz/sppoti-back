package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.enums.SppotiResponse;
import com.fr.service.UserParamService;
import com.fr.service.email.SppotiMailerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 3/23/17.
 */
@Component
public class SppotiMailerImpl extends ApplicationMailerServiceImpl implements SppotiMailerService
{
	
	/** Sppoti email templates */
	private final static String PATH_TO_JOIN_SPPOTI_TEMPLATE = "sppoti/join_sppoti";
	
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
	
	@Value("${spring.app.mail.sppoti.message.to.sppoter}")
	private String invitationMessageSentToSppoter;
	
	@Value("${spring.app.mail.sppoti.message.to.admin}")
	private String invitationMessageSentToSppotiAdmin;
	
	@Value("${spring.app.mail.sppoti.message.response.join}")
	private String responseToSppotiInvitationMessage;
	
	@Value("${spring.app.mail.accepted}")
	private String messageSppotiAccepted;
	
	@Value("${spring.app.mail.refused}")
	private String messageSppotiRefused;
	
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
	
	@Override
	public void sendJoinSppotiEmailToSppoters(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		if (this.userParamService.canReceiveEmail()) {
			final Context context = new Context();
			context.setVariable("sentToValidationLinkMessage", this.joinMessage);
			context.setVariable("requestFromSppotiAdmin", true);
			context.setVariable("toJoinSppotiMessage", this.invitationMessageSentToSppoter);
			
			this.invitationMessageSentToSppoter = this.invitationMessageSentToSppoter
					.replace("%SPPOTI_ADMIN%", from.getUsername());
			context.setVariable("messageBody", this.invitationMessageSentToSppoter);
			
			
			final String joinSppotiLinkParsed = this.frontRootPath +
					this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
			prepareAndSendEmail(context, to, from, sppoti, this.joinSppotiSubject, joinSppotiLinkParsed,
					PATH_TO_JOIN_SPPOTI_TEMPLATE, buildSppotiMailResources());
		}
	}
	
	@Override
	public void sendJoinSppotiEmailToSppotiAdmin(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		if (this.userParamService.canReceiveEmail()) {
			
			final Context context = new Context();
			context.setVariable("requestFromSppotiAdmin", false);
			
			this.invitationMessageSentToSppotiAdmin = this.invitationMessageSentToSppotiAdmin
					.replace("%SPPOTER%", from.getUsername());
			context.setVariable("messageBody", this.invitationMessageSentToSppotiAdmin);
			
			
			final String joinSppotiLinkParsed = this.frontRootPath +
					this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
			
			prepareAndSendEmail(context, to, from, sppoti, this.joinSppotiSubject, joinSppotiLinkParsed,
					PATH_TO_JOIN_SPPOTI_TEMPLATE, buildSppotiMailResources());
		}
	}
	
	@Override
	public void sendSppotiJoinResponseEmail(final SppotiDTO sppoti, final UserDTO to, final UserDTO from,
											final SppotiResponse sppotiResponse)
	{
		if (this.userParamService.canReceiveEmail()) {
			
			final Context context = new Context();
			context.setVariable("requestFromSppotiAdmin", false);
			this.responseToSppotiInvitationMessage = this.responseToSppotiInvitationMessage
					.replace("%SPPOTER%", from.getUsername());
			
			switch (sppotiResponse) {
				case ACCEPTED:
					this.responseToSppotiInvitationMessage = this.responseToSppotiInvitationMessage
							.replace("%RESPONSE%", this.messageSppotiAccepted);
					break;
				case REJECTED:
					this.responseToSppotiInvitationMessage = this.responseToSppotiInvitationMessage
							.replace("%RESPONSE%", this.messageSppotiRefused);
					break;
				default:
					break;
			}
			
			context.setVariable("messageBody", this.responseToSppotiInvitationMessage);
			
			prepareAndSendEmail(context, to, from, sppoti, this.joinSppotiSubject, null, PATH_TO_JOIN_SPPOTI_TEMPLATE,
					buildSppotiMailResources());
		}
	}
	
	private List<MailResourceContent> buildSppotiMailResources() {
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent avatarResourceContent = new MailResourceContent(), coverResourceContent
				= new MailResourceContent();
		avatarResourceContent.setPath(IMAGES_DIRECTORY + teamDefaultAvatarResourceName);
		avatarResourceContent.setResourceName(teamDefaultAvatarResourceName);
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
		coverResourceContent.setResourceName(sppotiCoverResourceName);
		
		resourceContents.add(avatarResourceContent);
		resourceContents.add(coverResourceContent);
		return resourceContents;
	}
	
	/**
	 * Send email.
	 */
	private void prepareAndSendEmail(final Context context, final UserDTO to, final UserDTO from,
									 final SppotiDTO Sppoti, final String subject, final String joinSppotiLink,
									 final String templatePath, final List<MailResourceContent> resourceContent)
	{
		context.setVariable("title", to.getFirstName());
		
		context.setVariable("sentFromName", from.getFirstName() + " " + from.getLastName());
		
		if (!StringUtils.isEmpty(joinSppotiLink)) {
			context.setVariable("sentToValidationLink", joinSppotiLink);
		}
		
		context.setVariable("sentToSppotiName", Sppoti.getName());
		context.setVariable("sentToFirstName", to.getFirstName());
		context.setVariable("sentToEmail", to.getEmail());
		context.setVariable("sentToUsername", to.getUsername());
		
		context.setVariable("avatarResourceName", resourceContent.get(0).getResourceName());
		context.setVariable("coverResourceName", resourceContent.get(1).getResourceName());
		
		context.setVariable("globalInformationAboutSppoti", this.sppotiConcept);
		
		context.setVariable("learnMoreMessage", this.learnMoreMessage);
		context.setVariable("andPrepositionMessage", this.andPrepositionMessage);
		context.setVariable("otherPrepositionMessage", this.otherPrepositionMessage);
		
		//Template Footer.
		context.setVariable("emailIntendedForMessageText", this.emailIntendedForMessage);
		context.setVariable("notYourAccountMessageText", this.notYourAccountMessage);
		context.setVariable("contactUsMessageText", this.contactUsMessage);
		context.setVariable("contactUsLink", this.contactUsLink);
		context.setVariable("sentToText", this.sentToTextMessage);
		
		final String text = this.templateEngine.process(templatePath, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
	}
	
}
