package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.service.email.ChallengeMailerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 12/29/17.
 */
@Component
public class ChallengeMailerServiceImpl extends ApplicationMailerServiceImpl implements ChallengeMailerService
{
	private static final String PATH_TO_CHALLENGE_TEMPLATE = "sppoti/challenge";
	
	private final Logger LOGGER = LoggerFactory.getLogger(ChallengeMailerServiceImpl.class);
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Override
	public void onSendChallenge(final TeamDTO from, final TeamDTO to, final SppotiDTO sppoti) {
		this.LOGGER.info("Sending email on creating a challenge ..");
		final List<UserDTO> teamToAdmin = to.getMembers().stream()
				.filter(m -> m.getTeamAdmin() && m.getCanReceiveEmails()).collect(Collectors.toList());
		
		if (CollectionUtils.isEmpty(teamToAdmin)) {
			this.LOGGER.info("All team admin have deactivated mailing");
			return;
		}
		
		teamToAdmin.forEach(m -> {
			//send mail
			final Locale language = Locale.forLanguageTag(m.getLanguage());
			
			final String[] params = {from.getName(), sppoti.getName()};
			final String subject = this.messageSource.getMessage("mail.challengeSentSubject", null, language);
			final String content = this.messageSource.getMessage("mail.challengeSentContent", params, language);
			
			prepareAndSendEmail(subject, content, m);
		});
	}
	
	@Override
	public void onAcceptChallenge(final TeamDTO from, final SppotiDTO sppoti) {
		this.LOGGER.info("Sending email to all sppoti members (Challenge refused) ...");
		
		sppoti.getSppotiMailingList().stream()
				.filter(m -> m.getCanReceiveEmails() && !m.getUserId().equals(from.getTeamAdmin().getUserId()))
				.forEach(m -> {
					//send email
					final Locale language = Locale.forLanguageTag(m.getLanguage());
					
					final String[] params = {from.getName(), sppoti.getName()};
					final String subject = this.messageSource
							.getMessage("mail.challengeAcceptedSubject", null, language);
					final String content = this.messageSource
							.getMessage("mail.challengeAcceptedContent", params, language);
					
					prepareAndSendEmail(subject, content, m);
				});
	}
	
	@Override
	public void onRefuseChallenge(final TeamDTO from, final TeamDTO to, final SppotiDTO sppoti) {
		this.LOGGER.info("Sending email on refusing a challenge ...");
		
		final List<UserDTO> teamToAdmin = to.getMembers().stream()
				.filter(m -> m.getTeamAdmin() && m.getCanReceiveEmails()).collect(Collectors.toList());
		
		if (CollectionUtils.isEmpty(teamToAdmin)) {
			this.LOGGER.info("All team admin have deactivated mailing");
			return;
		}
		
		teamToAdmin.forEach(m -> {
			//send mail
			final Locale language = Locale.forLanguageTag(m.getLanguage());
			
			final String[] params = {from.getName(), sppoti.getName()};
			final String subject = this.messageSource.getMessage("mail.challengeRefusedSubject", null, language);
			final String content = this.messageSource.getMessage("mail.challengeRefusedContent", params, language);
			
			prepareAndSendEmail(subject, content, m);
		});
	}
	
	@Override
	public void onCancelChallenge(final TeamDTO from, final TeamDTO to, final SppotiDTO sppoti) {
		this.LOGGER.info("Sending email to all sppoti members (Challenge refused) ..");
		final List<UserDTO> teamToAdmin = to.getMembers().stream()
				.filter(m -> m.getTeamAdmin() && m.getCanReceiveEmails()).collect(Collectors.toList());
		
		if (CollectionUtils.isEmpty(teamToAdmin)) {
			this.LOGGER.info("All team admin have deactivated mailing");
			return;
		}
		
		teamToAdmin.forEach(m -> {
			//send mail
			final Locale language = Locale.forLanguageTag(m.getLanguage());
			
			final String[] params = {from.getName(), sppoti.getName()};
			final String subject = this.messageSource.getMessage("mail.challengeCancelledSubject", null, language);
			final String content = this.messageSource.getMessage("mail.challengeCancelledContent", params, language);
			
			prepareAndSendEmail(subject, content, m);
		});
	}
	
	private void prepareAndSendEmail(final String subject, final String content, final UserDTO to)
	{
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + SPPOTI_COVER_RESOURCE_NAME);
		coverResourceContent.setResourceName(SPPOTI_COVER_RESOURCE_NAME);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguage()));
		context.setVariable("receiverUsername", to.getUsername());
		context.setVariable("content", content);
		
		//Template footer
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(PATH_TO_CHALLENGE_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
	
}