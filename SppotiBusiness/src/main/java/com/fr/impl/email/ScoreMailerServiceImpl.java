package com.fr.impl.email;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.service.UserParamService;
import com.fr.service.email.ScoreMailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

/**
 * Created by djenanewail on 11/10/17.
 */
@Component
public class ScoreMailerServiceImpl extends ApplicationMailerServiceImpl implements ScoreMailerService
{
	private final TemplateEngine templateEngine;
	
	private final UserParamService userParamService;
	
	@Autowired
	public ScoreMailerServiceImpl(final TemplateEngine templateEngine, final UserParamService userParamService) {
		this.templateEngine = templateEngine;
		this.userParamService = userParamService;
	}
	
	
	@Override
	public void sendScoreNotificationToTeamMember(final TeamDTO team) {
		if (this.userParamService.canReceiveEmail()) {
		
		}
	}
}
