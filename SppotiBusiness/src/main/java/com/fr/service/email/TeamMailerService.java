package com.fr.service.email;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.entities.TeamMemberEntity;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface TeamMailerService extends ApplicationMailerService
{
	void sendJoinTeamEmail(TeamDTO team, UserDTO to, UserDTO from);
	
	void sendConfirmJoinTeamEmail(TeamDTO team, TeamMemberEntity member);
}
