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
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by djenanewail on 11/13/17.
 */
@Component
public class CommentMailerServiceImpl extends ApplicationMailerServiceImpl implements CommentMailerService
{
	private static final String PATH_TO_COMMENT_TEMPLATE = "comment/comment";
	private final Logger LOGGER = LoggerFactory.getLogger(CommentMailerServiceImpl.class);
	
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
		
		contributors.stream().filter(c -> !c.getEmail().equals(comment.getAuthorEmail()) &&
				this.userParamService.canReceiveEmail(c.getId()))
				.forEach(c -> prepareAndSendEmail(postDTO, comment, c));
		
	}
	
	private void prepareAndSendEmail(final PostDTO postDTO, final CommentDTO comment, final EmailUserDTO to) {
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
		coverResourceContent.setResourceName(sppotiCoverResourceName);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguage()));
		
		context.setVariable("receiverUsername", to.getUsername());
		context.setVariable("receiverEmail", to.getEmail());
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		
		context.setVariable("commentAuthor", comment.getAuthorUsername());
		context.setVariable("commentText", comment.getText());
		
		//Template footer & header
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String subject = this.messageSource
				.getMessage("mail.commentSubject", null, Locale.forLanguageTag(to.getLanguage()));
		
		final String text = this.templateEngine.process(PATH_TO_COMMENT_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
	
	
}
