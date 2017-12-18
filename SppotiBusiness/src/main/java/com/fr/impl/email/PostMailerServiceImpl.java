package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.PostMailerService;
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
public class PostMailerServiceImpl extends ApplicationMailerServiceImpl implements PostMailerService
{
	private static final String PATH_TO_POST_TEMPLATE = "post/post";
	private final TemplateEngine templateEngine;
	private final UserParamService userParamService;
	
	@Value("${spring.app.mail.post.subject}")
	private String postEmailSubject;
	@Value("${spring.app.mail.post.content}")
	private String postEmailContent;
	
	@Autowired
	public PostMailerServiceImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	@Override
	public void sendEmailToTargetProfileOwner(final UserDTO target, final PostDTO post) {
		
		LOGGER.info("Sending email to profile owner <{}> on which the post has been added", target.getUserId());
		
		prepareAndSendEmail(post, target);
	}
	
	private void prepareAndSendEmail(final PostDTO postDTO, final UserDTO target) {
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent coverResourceContent = new MailResourceContent();
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + sppotiCoverResourceName);
		coverResourceContent.setResourceName(sppotiCoverResourceName);
		resourceContents.add(coverResourceContent);
		
		final Context context = new Context();
		
		context.setVariable("sentToUsername", target.getFirstName());
		context.setVariable("headerResourceName", resourceContents.get(0).getResourceName());
		
		
		this.postEmailContent = this.postEmailContent.replaceAll("%USER_ID%", postDTO.getSender().getUsername());
		
		context.setVariable("content", this.postEmailContent);
		context.setVariable("postText", postDTO.getContent());
		
		
		final String text = this.templateEngine.process(PATH_TO_POST_TEMPLATE, context);
		
		
		super.prepareAndSendEmail(target.getEmail(), this.postEmailSubject, text, resourceContents);
	}
}
