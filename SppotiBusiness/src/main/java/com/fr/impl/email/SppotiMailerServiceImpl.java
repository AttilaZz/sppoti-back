package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.enums.SppotiResponse;
import com.fr.service.UserParamService;
import com.fr.service.email.SppotiMailerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SppotiMailerServiceImpl extends ApplicationMailerServiceImpl implements SppotiMailerService
{
	private final Logger LOGGER = LoggerFactory.getLogger(SppotiMailerServiceImpl.class);
	
	private final static String PATH_TO_SPPOTI_TEMPLATE = "sppoti/sppoti";
	
	@Value("${mail.sppotiJoinLink}")
	private String joinSppotiLink;
	
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private UserParamService userParamService;
	
	private Context context;
	
	@Override
	public void onCreateSppoti(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		this.LOGGER.info("Sending join sppoti email to {}", to.getUsername());
		if (!this.userParamService.canReceiveEmail(to.getId())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
		this.context.setVariable("requestFromSppotiAdmin", true);
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		
		final String[] params = {from.getUsername()};
		final String content = this.messageSource.getMessage("mail.sppotiMessageToSppoter", params, language);
		this.context.setVariable("messageBody", content);
		
		final String joinSppotiLinkParsed = this.frontRootPath +
				this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
		
		final String subject = this.messageSource.getMessage("mail.sppotiJoinSubject", null, language);
		
		prepareAndSendEmail(this.context, to, from, sppoti, subject, joinSppotiLinkParsed, PATH_TO_SPPOTI_TEMPLATE);
	}
	
	@Override
	public void onSendingJoinRequestToSppoti(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		this.LOGGER.info("Sending join sppoti email to sppoti admin: {}", to.getUsername());
		if (!this.userParamService.canReceiveEmail(to.getId())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
		this.context.setVariable("requestFromSppotiAdmin", false);
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		
		final String[] params = {from.getUsername()};
		final String content = this.messageSource.getMessage("mail.sppotiMessageToAdmin", params, language);
		this.context.setVariable("messageBody", content);
		
		final String joinSppotiLinkParsed = this.frontRootPath +
				this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
		
		final String subject = this.messageSource.getMessage("mail.sppotiJoinSubject", null, language);
		
		prepareAndSendEmail(this.context, to, from, sppoti, subject, joinSppotiLinkParsed, PATH_TO_SPPOTI_TEMPLATE);
	}
	
	//TODO: adapt this method to mailing plan
	@Override
	public void onRespondingToSppotiJoinRequestFromSppoter(final SppotiDTO sppoti, final UserDTO to, final UserDTO from,
														   final SppotiResponse sppotiResponse)
	{
		this.LOGGER.info("Sending join sppoti response email to sppoti admin from {}", from.getUsername());
		if (!this.userParamService.canReceiveEmail(to.getId())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
		this.context.setVariable("requestFromSppotiAdmin", false);
		
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		
		final String acceptanceStatus;
		
		if (sppotiResponse.equals(SppotiResponse.ACCEPTED)) {
			acceptanceStatus = this.messageSource.getMessage("mail.hasBeenAccepted", null, language);
		} else {
			acceptanceStatus = this.messageSource.getMessage("mail.hasBeenRefused", null, language);
		}
		final String[] params = {sppoti.getName(), acceptanceStatus, from.getUsername()};
		
		final String content = this.messageSource
				.getMessage("mail.sppotiMessageResponseJoinBySppoterContent", params, language);
		this.context.setVariable("messageBody", content);
		
		final String subject = this.messageSource.getMessage("mail.sppotiJoinSubject", null, language);
		
		prepareAndSendEmail(this.context, to, from, sppoti, subject, null, PATH_TO_SPPOTI_TEMPLATE);
	}
	
	@Override
	public void onRespondingToSppotiJoinRequestFromSppotiAdmin(final SppotiDTO sppoti, final UserDTO to,
															   final UserDTO from, final SppotiResponse sppotiResponse)
	{
		this.LOGGER.info("Sending join sppoti response email from sppoti admin to: {}", to.getUsername());
		
	}
	
	
	@Override
	public void onSppotiEdit(final SppotiDTO sppoti, final UserDTO from)
	{
		this.LOGGER.info("Sending email on sppoti edit ...");
		
		final List<UserDTO> sppotiMembersMailingList = sppoti.getSppotiMailingList();
		
		sppotiMembersMailingList.stream().filter(m -> !m.getId().equals(from.getId()) && m.getCanReceiveEmails())
				.forEach(m -> {
					final Locale language = Locale.forLanguageTag(m.getLanguage());
					
					final String[] params = {from.getUsername()};
					final String content = this.messageSource.getMessage("mail.sppotiEditedContent", params, language);
					this.context.setVariable("messageBody", content);
					
					final String subject = this.messageSource.getMessage("mail.sppotiEditedSubject", null, language);
					
					prepareAndSendEmail(this.context, m, from, sppoti, subject, null, PATH_TO_SPPOTI_TEMPLATE);
				});
	}
	
	@Override
	public void onSppotiDeleted(final SppotiDTO sppoti, final UserDTO from)
	{
		this.LOGGER.info("Sending email on sppoti deleted ...");
		
		final List<UserDTO> sppotiMembersMailingList = sppoti.getSppotiMailingList();
		
		sppotiMembersMailingList.stream().filter(m -> !m.getId().equals(from.getId()) && m.getCanReceiveEmails())
				.forEach(m -> {
					final Locale language = Locale.forLanguageTag(m.getLanguage());
					
					final String[] params = {from.getUsername()};
					final String content = this.messageSource.getMessage("mail.sppotiDeletedContent", params, language);
					this.context.setVariable("messageBody", content);
					
					final String subject = this.messageSource.getMessage("mail.sppotiDeletedSubject", null, language);
					
					prepareAndSendEmail(this.context, m, from, sppoti, subject, null, PATH_TO_SPPOTI_TEMPLATE);
				});
	}
	
	private List<MailResourceContent> buildSppotiMailResources() {
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent avatarResourceContent = new MailResourceContent(), coverResourceContent
				= new MailResourceContent();
		avatarResourceContent.setPath(IMAGES_DIRECTORY + TEAM_AVATAR_RESOURCE_NAME);
		avatarResourceContent.setResourceName(TEAM_AVATAR_RESOURCE_NAME);
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + SPPOTI_COVER_RESOURCE_NAME);
		coverResourceContent.setResourceName(SPPOTI_COVER_RESOURCE_NAME);
		
		resourceContents.add(avatarResourceContent);
		resourceContents.add(coverResourceContent);
		return resourceContents;
	}
	
	/**
	 * Send email.
	 */
	private void prepareAndSendEmail(final Context context, final UserDTO to, final UserDTO from,
									 final SppotiDTO Sppoti, final String subject, final String joinSppotiLink,
									 final String templatePath)
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
		
		final List<MailResourceContent> mailResourceContents = buildSppotiMailResources();
		context.setVariable("avatarResourceName", mailResourceContents.get(0).getResourceName());
		context.setVariable("coverResourceName", mailResourceContents.get(1).getResourceName());
		
		//Template footer & header
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(templatePath, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, mailResourceContents);
	}
	
}
