package com.fr.impl.email;

import com.fr.commons.dto.UserDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.PostMailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

/**
 * Created by djenanewail on 11/13/17.
 */
@Component
public class PostMailerServiceImpl extends ApplicationMailerServiceImpl implements PostMailerService
{
	private final TemplateEngine templateEngine;
	private final UserParamService userParamService;
	
	@Autowired
	public PostMailerServiceImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	@Override
	public void sendEmailToTargetProfileOwner(final UserDTO target) {
		if (this.userParamService.canReceiveEmail()) {
		
		}
	}
}
