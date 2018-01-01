package com.fr.impl.email;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.LikeMailerService;
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
public class LikeMailerServiceImpl extends ApplicationMailerServiceImpl implements LikeMailerService
{
	private static final String PATH_TO_LIKE_TEMPLATE = "post/like";
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private UserParamService userParamService;
	
	@Override
	public void sendEmailToPostOwner(final UserDTO to, final PostDTO post) {
		LOGGER.info("Sending email to notify to about a post on his profile");
		if (!this.userParamService.canReceiveEmail(to.getId())) {
			LOGGER.info("User <{}> has deactivated receiving emails", to.getEmail());
			return;
		}
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		
		final String subject = this.messageSource.getMessage("mail.likePostSubject", null, language);
		
		final String[] params = {post.getSender().getUsername()};
		final String content = this.messageSource.getMessage("mail.likePostContent", params, language);
		
		prepareAndSendEmail(to, content, subject);
	}
	
	@Override
	public void sendEmailToCommentOwner(final UserDTO to, final CommentDTO comment) {
		LOGGER.info("Sending email to notify to about a post on his profile");
		if (!this.userParamService.canReceiveEmail(to.getId())) {
			LOGGER.info("User <{}> has deactivated receiving emails", to.getEmail());
			return;
		}
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		
		final String subject = this.messageSource.getMessage("mail.likeCommentSubject", null, language);
		
		final String[] params = {comment.getAuthorUsername()};
		final String content = this.messageSource.getMessage("mail.likePostContent", params, language);
		
		prepareAndSendEmail(to, content, subject);
	}
	
	private void prepareAndSendEmail(final UserDTO to, final String content, final String mailSubject)
	{
		
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + SPPOTI_COVER_RESOURCE_NAME);
		coverResourceContent.setResourceName(SPPOTI_COVER_RESOURCE_NAME);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguage()));
		
		context.setVariable("receiverUsername", to.getFirstName());
		context.setVariable("receiverEmail", to.getEmail());
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		
		context.setVariable("content", content);
		
		//Template footer.
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(PATH_TO_LIKE_TEMPLATE, context);
		
		
		super.prepareAndSendEmail(to.getEmail(), mailSubject, text, resourceContents);
	}
}
