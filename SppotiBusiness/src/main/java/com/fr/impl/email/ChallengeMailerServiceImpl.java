package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamEntity;
import com.fr.entities.UserEntity;
import com.fr.service.UserParamService;
import com.fr.service.email.ChallengeMailerService;
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
 * Created by djenanewail on 12/29/17.
 */
@Component
public class ChallengeMailerServiceImpl extends ApplicationMailerServiceImpl implements ChallengeMailerService
{
	private static final String PATH_TO_CHALLENGE_TEMPLATE = "challenge/challenge";
	
	private final Logger LOGGER = LoggerFactory.getLogger(ChallengeMailerServiceImpl.class);
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private UserParamService userParamService;
	
	@Override
	public void onSendChallenge(final TeamEntity from, final TeamEntity to, final SppotiEntity sppoti) {
		this.LOGGER.info("Sending email on creating a challenge ..");
	}
	
	@Override
	public void onAcceptChallenge(final TeamEntity from, final TeamEntity to, final SppotiEntity sppoti) {
		this.LOGGER.info("Sending email on accepting a challenge ..");
		
	}
	
	@Override
	public void onRefuseChallenge(final TeamEntity from, final TeamEntity to, final SppotiEntity sppoti) {
		this.LOGGER.info("Sending email on refusing a challenge ..");
		
	}
	
	private void prepareAndSendEmail(final String subject, final String content, final UserEntity to,
									 final UserEntity from)
	{
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + SPPOTI_COVER_RESOURCE_NAME);
		coverResourceContent.setResourceName(SPPOTI_COVER_RESOURCE_NAME);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguageEnum().name()));
		
		context.setVariable("receiverUsername", to.getUsername());
		
		//Template footer
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(PATH_TO_CHALLENGE_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
	
}
