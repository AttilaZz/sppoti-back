package com.fr.service.email;

import com.fr.commons.dto.team.TeamDTO;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface ScoreMailerService
{
	void sendScoreNotificationToTeamMember(TeamDTO team);
	
}
