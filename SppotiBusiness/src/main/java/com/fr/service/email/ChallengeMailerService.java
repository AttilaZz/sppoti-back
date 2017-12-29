package com.fr.service.email;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;

/**
 * Created by djenanewail on 12/29/17.
 */
public interface ChallengeMailerService
{
	void onSendChallenge(TeamDTO from, TeamDTO to, SppotiDTO sppoti);
	
	void onAcceptChallenge(TeamDTO from, SppotiDTO sppoti);
	
	void onRefuseChallenge(TeamDTO from, TeamDTO to, SppotiDTO sppoti);
	
	void onCancelChallenge(TeamDTO from, TeamDTO to, SppotiDTO sppotiDTO);
}
