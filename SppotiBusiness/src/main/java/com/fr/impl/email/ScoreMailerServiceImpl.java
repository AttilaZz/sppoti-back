package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.ScoreDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.UserEntity;
import com.fr.repositories.SppotiRepository;
import com.fr.service.UserParamService;
import com.fr.service.email.ScoreMailerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 11/10/17.
 */
@Component
public class ScoreMailerServiceImpl extends ApplicationMailerServiceImpl implements ScoreMailerService
{
	private static final String PATH_TO_SCORE_TEMPLATE = "score/score";
	private final Logger LOGGER = LoggerFactory.getLogger(ScoreMailerServiceImpl.class);
	
	@Value("${spring.app.mail.score.add.subject}")
	private String addScoreEmailSubject;
	@Value("${spring.app.mail.score.accept.subject}")
	private String acceptScoreEmailSubject;
	@Value("${spring.app.mail.score.refuse.subject}")
	private String refuseScoreEmailSubject;
	
	@Value("${spring.app.mail.score.add.content}")
	private String addScoreEmailContent;
	@Value("${spring.app.mail.score.accept.content}")
	private String acceptScoreEmailContent;
	@Value("${spring.app.mail.score.refuse.content}")
	private String refuseScoreEmailContent;
	
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private UserParamService userParamService;
	
	@Autowired
	private SppotiRepository sppotiRepository;
	
	@Override
	public void sendAddScoreEmail(final ScoreDTO score, final UserEntity to, final UserEntity from) {
		this.LOGGER.info("Sending email to: <{}> admin admin after adding a score <{}>, to SPPOTI <{}>", to, score,
				score.getSppotiId());
		
		if (this.userParamService.canReceiveEmail(to.getUuid())) {
			prepareAndSendEmail(score, this.addScoreEmailSubject, this.addScoreEmailContent, to, from);
		}
	}
	
	@Override
	public void sendAcceptScoreEmail(final ScoreDTO score, final UserEntity sender, final List<UserEntity> receivers) {
		this.LOGGER
				.info("Sending email to sppoti members <{}> after accepting the score of SPPOTI <{}> by the team adverse",
						score.getSppotiId(), receivers);
		
		receivers.stream().filter(r -> this.userParamService.canReceiveEmail(r.getUuid()) &&
				!r.getEmail().equals(sender.getEmail())).forEach(
				r -> prepareAndSendEmail(score, this.acceptScoreEmailSubject, this.acceptScoreEmailContent, r, sender));
	}
	
	@Override
	public void sendRefuseScoreEmail(final ScoreDTO score, final UserEntity sender, final List<UserEntity> receivers) {
		
		this.LOGGER
				.info("Sending email to sppoti members <{}> after refusing the score of SPPOTI <{}> by the team adverse",
						score.getSppotiId(), receivers);
		
		receivers.stream().filter(r -> this.userParamService.canReceiveEmail(r.getUuid()) &&
				!r.getEmail().equals(sender.getEmail())).forEach(
				r -> prepareAndSendEmail(score, this.refuseScoreEmailSubject, this.refuseScoreEmailContent, r, sender));
	}
	
	private void prepareAndSendEmail(final ScoreDTO score, final String subject, final String content,
									 final UserEntity to, final UserEntity from)
	{
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
		coverResourceContent.setResourceName(sppotiCoverResourceName);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context();
		
		context.setVariable("receiverUsername", to.getUsername());
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		
		context.setVariable("content", formatContent(content, score.getSppotiId(), from.getUsername()));
		
		//Template footer.
		context.setVariable("emailIntendedForMessageText", this.emailIntendedForMessage);
		context.setVariable("notYourAccountMessageText", this.notYourAccountMessage);
		context.setVariable("contactUsMessageText", this.contactUsMessage);
		context.setVariable("contactUsLink", this.contactUsLink);
		context.setVariable("sentToText", this.sentToTextMessage);
		
		final String text = this.templateEngine.process(PATH_TO_SCORE_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
	
	private String formatContent(String content, final String sppotiId, final String fromUsername)
	{
		final SppotiEntity sppoti = this.sppotiRepository.findByUuid(sppotiId);
		final String sppotiName = sppoti.getName();
		
		content = content.replaceAll("%USER_FROM%", fromUsername);
		
		content = content.replaceAll("%SPPOTI_NAME%", sppotiName);
		
		return content;
	}
	
	enum ScoreTypeEmail
	{
		ADD, ACCEPT, REFUSE
	}
}
