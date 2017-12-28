package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.entities.UserEntity;
import com.fr.service.UserParamService;
import com.fr.service.email.FriendMailerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by djenanewail on 12/27/17.
 */
@Component
public class FriendMailerServiceImpl extends ApplicationMailerServiceImpl implements FriendMailerService
{
	private static final String PATH_TO_FRIEND_TEMPLATE = "friend/friend";
	private final Logger LOGGER = LoggerFactory.getLogger(FriendMailerServiceImpl.class);
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private UserParamService userParamService;
	
	@Override
	public void onSendFriendRequest(final UserEntity from, final UserEntity to) {
		this.LOGGER.info("Sending email on send friendship request ...");
		if (!this.userParamService.canReceiveEmail(to.getUuid())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		final Locale language = Locale.forLanguageTag(to.getLanguageEnum().name());
		
		final String subject = this.messageSource.getMessage("mail.friendAddSubject", null, language);
		
		final String[] params = {from.getUsername()};
		final String content = this.messageSource.getMessage("mail.friendAddContent", params, language);
		
		prepareAndSendEmail(to, content, subject);
	}
	
	@Override
	public void onAcceptFriendRequest(final UserEntity from, final UserEntity to) {
		this.LOGGER.info("Sending email on accept friendship request ...");
		if (!this.userParamService.canReceiveEmail(to.getUuid())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		final Locale language = Locale.forLanguageTag(to.getLanguageEnum().name());
		
		final String subject = this.messageSource.getMessage("mail.friendAcceptSubject", null, language);
		
		final String[] params = {from.getUsername()};
		final String content = this.messageSource.getMessage("mail.friendAcceptContent", params, language);
		
		prepareAndSendEmail(to, content, subject);
	}
	
	@Override
	public void onRefuseFriendRequest(final UserEntity from, final UserEntity to) {
		this.LOGGER.info("Sending email on refuse friendship request ...");
		if (!this.userParamService.canReceiveEmail(to.getUuid())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		final Locale language = Locale.forLanguageTag(to.getLanguageEnum().name());
		
		final String subject = this.messageSource.getMessage("mail.friendRefuseSubject", null, language);
		
		final String[] params = {from.getUsername()};
		final String content = this.messageSource.getMessage("mail.friendRefuseContent", params, language);
		
		prepareAndSendEmail(to, content, subject);
	}
	
	private void prepareAndSendEmail(final UserEntity to, final String content, final String subject)
	{
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + SPPOTI_COVER_RESOURCE_NAME);
		coverResourceContent.setResourceName(SPPOTI_COVER_RESOURCE_NAME);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguageEnum().name()));
		
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		context.setVariable("receiverUsername", to.getFirstName());
		context.setVariable("content", content);
		
		final String text = this.templateEngine.process(PATH_TO_FRIEND_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
	
}
