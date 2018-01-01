package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.PostMailerService;
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
public class PostMailerServiceImpl extends ApplicationMailerServiceImpl implements PostMailerService
{
	private static final String PATH_TO_POST_TEMPLATE = "post/post";
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private UserParamService userParamService;
	
	@Override
	public void sendEmailToTargetProfileOwner(final UserDTO target, final PostDTO post) {
		
		LOGGER.info("Sending email to profile owner <{}> on which the post has been added", target.getUserId());
		
		if (!this.userParamService.canReceiveEmail(target.getId())) {
			LOGGER.info("{} has deactivated emails", target.getUsername());
			return;
		}
		prepareAndSendEmail(post, target);
	}
	
	private void prepareAndSendEmail(final PostDTO postDTO, final UserDTO to) {
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + SPPOTI_COVER_RESOURCE_NAME);
		coverResourceContent.setResourceName(SPPOTI_COVER_RESOURCE_NAME);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguage()));
		
		context.setVariable("receiverUsername", to.getFirstName());
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		context.setVariable("receiverEmail", to.getEmail());
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		final String[] params = {postDTO.getSender().getUsername()};
		final String content = this.messageSource.getMessage("mail.postContent", params, language);
		
		context.setVariable("content", content);
		context.setVariable("postText", postDTO.getContent());
		
		//Template footer & header
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(PATH_TO_POST_TEMPLATE, context);
		
		final String subject = this.messageSource
				.getMessage("mail.postSubject", null, Locale.forLanguageTag(to.getLanguage()));
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContents);
	}
}
