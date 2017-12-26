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
import java.util.Locale;

/**
 * Created by djenanewail on 3/23/17.
 */
@Component
public class SppotiMailerImpl extends ApplicationMailerServiceImpl implements SppotiMailerService
{
	
	private final static String PATH_TO_JOIN_SPPOTI_TEMPLATE = "sppoti/sppoti";
	
	@Value("${mail.sppotiJoinLink}")
	private String joinSppotiLink;
	
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private UserParamService userParamService;
	
	private Context context;
	
	/**
	 * Send email to confirm Sppoti creation.
	 *
	 * @param Sppoti
	 * 		ceated Sppoti.
	 */
	@Override
	public void sendAddSppotiEmail(final SppotiDTO Sppoti)
	{
	}
	
	@Override
	public void sendJoinSppotiEmailToSppoters(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		if (this.userParamService.canReceiveEmail(to.getEmail())) {
			this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
			this.context.setVariable("requestFromSppotiAdmin", true);
			
			final Locale language = Locale.forLanguageTag(to.getLanguage());
			
			final String[] params = {from.getUsername()};
			final String content = this.messageSource.getMessage("mail.sppotiMessageToSppoter", params, language);
			this.context.setVariable("messageBody", content);
			
			final String joinSppotiLinkParsed = this.frontRootPath +
					this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
			
			final String subject = this.messageSource.getMessage("mail.sppotiJoinSubject", null, language);
			
			prepareAndSendEmail(this.context, to, from, sppoti, subject, joinSppotiLinkParsed,
					PATH_TO_JOIN_SPPOTI_TEMPLATE, buildSppotiMailResources());
		}
	}
	
	@Override
	public void sendJoinSppotiEmailToSppotiAdmin(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		if (this.userParamService.canReceiveEmail(to.getEmail())) {
			
			this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
			this.context.setVariable("requestFromSppotiAdmin", false);
			
			final Locale language = Locale.forLanguageTag(to.getLanguage());
			
			final String[] params = {from.getUsername()};
			final String content = this.messageSource.getMessage("mail.sppotiMessageToAdmin", params, language);
			this.context.setVariable("messageBody", content);
			
			final String joinSppotiLinkParsed = this.frontRootPath +
					this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
			
			final String subject = this.messageSource.getMessage("mail.sppotiJoinSubject", null, language);
			
			prepareAndSendEmail(this.context, to, from, sppoti, subject, joinSppotiLinkParsed,
					PATH_TO_JOIN_SPPOTI_TEMPLATE, buildSppotiMailResources());
		}
	}
	
	@Override
	public void sendSppotiJoinResponseEmail(final SppotiDTO sppoti, final UserDTO to, final UserDTO from,
											final SppotiResponse sppotiResponse)
	{
		if (this.userParamService.canReceiveEmail(to.getEmail())) {
			
			this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
			this.context.setVariable("requestFromSppotiAdmin", false);
			
			
			final Locale language = Locale.forLanguageTag(to.getLanguage());
			
			final String[] params = {from.getUsername()};
			final String content = this.messageSource.getMessage("mail.sppotiMessageResponseJoin", params, language);
			this.context.setVariable("messageBody", content);
			
			final String subject = this.messageSource
					.getMessage("mail.sppotiJoinSubject", null, Locale.forLanguageTag(to.getLanguage()));
			
			prepareAndSendEmail(this.context, to, from, sppoti, subject, null, PATH_TO_JOIN_SPPOTI_TEMPLATE,
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
		context.setVariable("receiverUsername", to.getUsername());
		context.setVariable("receiverEmail", to.getEmail());
		
		context.setVariable("avatarResourceName", resourceContent.get(0).getResourceName());
		context.setVariable("coverResourceName", resourceContent.get(1).getResourceName());
		
		//Template footer & header
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(templatePath, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
	}
	
}
