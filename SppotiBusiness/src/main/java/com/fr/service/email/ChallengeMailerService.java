package com.fr.service.email;

import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamEntity;

/**
 * Created by djenanewail on 12/29/17.
 */
public interface ChallengeMailerService
{
	void onSendChallenge(TeamEntity from, TeamEntity to, SppotiEntity sppoti);
	
	void onAcceptChallenge(TeamEntity from, TeamEntity to, SppotiEntity sppoti);
	
	void onRefuseChallenge(TeamEntity from, TeamEntity to, SppotiEntity sppoti);
}
