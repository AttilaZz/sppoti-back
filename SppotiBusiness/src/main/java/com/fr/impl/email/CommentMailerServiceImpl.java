package com.fr.impl.email;

import com.fr.commons.dto.UserDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.CommentMailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import java.util.List;

/**
 * Created by djenanewail on 11/13/17.
 */
@Component
public class CommentMailerServiceImpl extends ApplicationMailerServiceImpl implements CommentMailerService
{
	private final TemplateEngine templateEngine;
	private final UserParamService userParamService;
	
	@Autowired
	public CommentMailerServiceImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	@Override
	public void sendEmailToPostContributors(final List<UserDTO> contributors) {
		if (this.userParamService.canReceiveEmail()) {
		
		}
	}
}
