package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.ScoreDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.UserEntity;
import com.fr.service.UserParamService;
import com.fr.service.email.ScoreMailerService;
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
public class ScoreMailerServiceImpl extends ApplicationMailerServiceImpl implements ScoreMailerService
{
	private static final String PATH_TO_SCORE_TEMPLATE = "score/score";
	private final Logger LOGGER = LoggerFactory.getLogger(ScoreMailerServiceImpl.class);
	
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private UserParamService userParamService;
	
	@Override
	public void sendAddScoreEmail(final ScoreDTO score, final UserEntity to, final UserEntity from,
								  final SppotiEntity sppoti)
	{
		this.LOGGER.info("Sending email to: <{}> admin admin after adding a score <{}>, to SPPOTI <{}>", to, score,
				score.getSppotiId());
		
		if (this.userParamService.canReceiveEmail(to.getUuid())) {
			final Locale language = Locale.forLanguageTag(to.getLanguageEnum().name());
			final String subject = this.messageSource.getMessage("mail.scoreAddSubject", null, language);
			
			final String[] params = {from.getUsername(), sppoti.getName()};
			final String content = this.messageSource.getMessage("mail.scoreAcceptContent", params, language);
			prepareAndSendEmail(score, subject, content, to, from);
		}
	}
	
	@Override
	public void sendAcceptScoreEmail(final ScoreDTO score, final UserEntity from, final List<UserEntity> receivers,
									 final SppotiEntity sppoti)
	{
		this.LOGGER
				.info("Sending email to sppoti members <{}> after accepting the score of SPPOTI <{}> by the team adverse",
						score.getSppotiId(), receivers);
		
		receivers.stream().filter(r -> this.userParamService.canReceiveEmail(r.getUuid()) &&
				!r.getEmail().equals(from.getEmail())).forEach(r -> {
			
			final Locale language = Locale.forLanguageTag(r.getLanguageEnum().name());
			
			final String subject = this.messageSource.getMessage("mail.scoreAcceptSubject", null, language);
			
			final String[] params = {from.getUsername(), sppoti.getName()};
			final String content = this.messageSource.getMessage("mail.scoreAddContent", params, language);
			
			prepareAndSendEmail(score, subject, content, r, from);
		});
	}
	
	@Override
	public void sendRefuseScoreEmail(final ScoreDTO score, final UserEntity from, final List<UserEntity> receivers,
									 final SppotiEntity sppoti)
	{
		
		this.LOGGER
				.info("Sending email to sppoti members <{}> after refusing the score of SPPOTI <{}> by the team adverse",
						score.getSppotiId(), receivers);
		
		receivers.stream().filter(r -> this.userParamService.canReceiveEmail(r.getUuid()) &&
				!r.getEmail().equals(from.getEmail())).forEach(r -> {
			final Locale language = Locale.forLanguageTag(r.getLanguageEnum().name());
			
			final String subject = this.messageSource.getMessage("mail.scoreRefuseSubject", null, language);
			
			final String[] params = {from.getUsername(), sppoti.getName()};
			final String content = this.messageSource.getMessage("mail.scoreRefuseContent", params, language);
			
			prepareAndSendEmail(score, subject, content, r, from);
		});
	}
	
	private void prepareAndSendEmail(final ScoreDTO score, final String subject, final String content,
									 final UserEntity to, final UserEntity from)
	{
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
		coverResourceContent.setResourceName(sppotiCoverResourceName);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguageEnum().name()));
		
		context.setVariable("receiverUsername", to.getUsername());
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		context.setVariable("receiverEmail", to.getEmail());
		
		context.setVariable("content", content);
		
		//Template footer & header
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(PATH_TO_SCORE_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
}
