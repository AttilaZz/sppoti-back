package com.fr.impl.email;

import com.fr.commons.dto.UserDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.LikeMailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

/**
 * Created by djenanewail on 11/10/17.
 */
@Component
public class LikeMailerServiceImpl extends ApplicationMailerServiceImpl implements LikeMailerService
{
	private final TemplateEngine templateEngine;
	
	private final UserParamService userParamService;
	
	@Autowired
	public LikeMailerServiceImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	@Override
	public void sendEmailToPostPublisher(final UserDTO user) {
		if (this.userParamService.canReceiveEmail()) {
		
		}
	}
}
