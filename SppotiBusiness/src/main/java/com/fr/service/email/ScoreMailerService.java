package com.fr.service.email;

import com.fr.commons.dto.ScoreDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.UserEntity;

import java.util.List;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface ScoreMailerService
{
	void sendAddScoreEmail(ScoreDTO score, UserEntity to, UserEntity from, final SppotiEntity sppoti);
	
	void sendAcceptScoreEmail(ScoreDTO score, UserEntity sender, List<UserEntity> receivers, final SppotiEntity sppoti);
	
	void sendRefuseScoreEmail(ScoreDTO score, UserEntity sender, List<UserEntity> receivers, final SppotiEntity sppoti);
}
