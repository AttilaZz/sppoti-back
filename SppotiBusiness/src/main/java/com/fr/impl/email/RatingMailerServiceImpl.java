package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.entities.SppotiEntity;
import com.fr.entities.UserEntity;
import com.fr.service.UserParamService;
import com.fr.service.email.RatingMailerService;
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
 * Created by djenanewail on 11/10/17.
 */
@Component
public class RatingMailerServiceImpl extends ApplicationMailerServiceImpl implements RatingMailerService
{
	private static final String PATH_TO_RATING_TEMPLATE = "sppoti/rating";
	private final Logger LOGGER = LoggerFactory.getLogger(FriendMailerServiceImpl.class);
	
	@Autowired
	private final TemplateEngine templateEngine;
	
	@Autowired
	private final UserParamService userParamService;
	
	public RatingMailerServiceImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	@Override
	public void onRatingUser(final UserEntity from, final UserEntity to, final SppotiEntity sppoti) {
		this.LOGGER.info("Sending rating email to: {}", to.getUsername());
		if (!this.userParamService.canReceiveEmail(to.getUuid())) {
			this.LOGGER.info("User <{}> has deactivated emails", to.getUsername());
			return;
		}
		final Locale language = Locale.forLanguageTag(to.getLanguageEnum().name());
		
		final String subject = this.messageSource.getMessage("mail.ratingAddSubject", null, language);
		
		final String[] params = {from.getUsername(), sppoti.getName()};
		final String content = this.messageSource.getMessage("mail.ratingAddContent", params, language);
		
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
		
		//Template footer & header
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(PATH_TO_RATING_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
}
