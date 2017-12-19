package com.fr.impl.email;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.LikeMailerService;
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
public class LikeMailerServiceImpl extends ApplicationMailerServiceImpl implements LikeMailerService
{
	private static final String PATH_TO_LIKE_TEMPLATE = "like/like";
	
	@Value("${spring.app.mail.like.post.subject}")
	private String postLikeEmailSubject;
	@Value("${spring.app.mail.like.post.content}")
	private String postLikeEmailContent;
	
	@Value("${spring.app.mail.like.comment.subject}")
	private String commentLikeEmailSubject;
	@Value("${spring.app.mail.like.comment.content}")
	private String commentLikeEmailContent;
	
	private final TemplateEngine templateEngine;
	
	private final UserParamService userParamService;
	
	@Autowired
	public LikeMailerServiceImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	@Override
	public void sendEmailToPostOwner(final UserDTO user, final PostDTO post) {
		LOGGER.info("Sending email to notify user about a post on his profile");
		if (!this.userParamService.canReceiveEmail(user.getId())) {
			LOGGER.info("User <{}> has deactivated receiving emails", user.getEmail());
		}
		
		prepareAndSendEmail(user, this.postLikeEmailContent, this.postLikeEmailSubject, post.getSender().getUsername());
	}
	
	@Override
	public void sendEmailToCommentOwner(final UserDTO user, final CommentDTO comment) {
		LOGGER.info("Sending email to notify user about a post on his profile");
		if (!this.userParamService.canReceiveEmail(user.getId())) {
			LOGGER.info("User <{}> has deactivated receiving emails", user.getEmail());
		}
		
		prepareAndSendEmail(user, this.commentLikeEmailContent, this.commentLikeEmailSubject,
				comment.getAuthorUsername());
	}
	
	private void prepareAndSendEmail(final UserDTO target, String mailContent, final String mailSubject,
									 final String senderUsername)
	{
		
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
		coverResourceContent.setResourceName(sppotiCoverResourceName);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context();
		
		context.setVariable("sentToUsername", target.getFirstName());
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		
		
		mailContent = mailContent.replaceAll("%USER_ID%", senderUsername);
		
		context.setVariable("content", mailContent);
		
		
		final String text = this.templateEngine.process(PATH_TO_LIKE_TEMPLATE, context);
		
		
		super.prepareAndSendEmail(target.getEmail(), mailSubject, text, resourceContents);
	}
}
