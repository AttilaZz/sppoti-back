package com.fr.impl.email;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.EmailUserDTO;
import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.post.PostDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.CommentMailerService;
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
 * Created by djenanewail on 11/13/17.
 */
@Component
public class CommentMailerServiceImpl extends ApplicationMailerServiceImpl implements CommentMailerService
{
	private static final String PATH_TO_COMMENT_TEMPLATE = "comment/comment";
	private final Logger LOGGER = LoggerFactory.getLogger(CommentMailerServiceImpl.class);
	
	@Value("${spring.app.mail.comment.subject}")
	private String commentEmailSubject;
	@Value("${spring.app.mail.comment.content}")
	private String commentEmailContent;
	
	private final TemplateEngine templateEngine;
	private final UserParamService userParamService;
	
	@Autowired
	public CommentMailerServiceImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	@Override
	public void sendEmailToPostContributors(final PostDTO postDTO, final CommentDTO comment,
											final List<EmailUserDTO> contributors)
	{
		this.LOGGER.info("Sending email to all POST contributors, postId: <{}>, contributors: {}", postDTO.getId(),
				contributors);
		
		contributors.stream().filter(c -> this.userParamService.canReceiveEmail(c.getId()))
				.forEach(c -> prepareAndSendEmail(postDTO, comment, c));
		
	}
	
	private void prepareAndSendEmail(final PostDTO postDTO, final CommentDTO comment, final EmailUserDTO receiver) {
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
		coverResourceContent.setResourceName(sppotiCoverResourceName);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context();
		
		context.setVariable("sentToUsername", receiver.getFirstName());
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		
		
		this.commentEmailContent = this.commentEmailContent.replaceAll("%USER_ID%", comment.getAuthorUsername());
		
		context.setVariable("content", this.commentEmailContent);
		context.setVariable("commentText", comment.getText());
		
		
		final String text = this.templateEngine.process(PATH_TO_COMMENT_TEMPLATE, context);
		
		
		super.prepareAndSendEmail(receiver.getEmail(), this.commentEmailSubject, text, resourceContents);
	}
	
	
}
