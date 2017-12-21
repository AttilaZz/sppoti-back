package com.fr.service.email;

import com.fr.commons.dto.ScoreDTO;
import com.fr.entities.UserEntity;

import java.util.List;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface ScoreMailerService
{
	void sendAddScoreEmail(ScoreDTO score, UserEntity to, UserEntity from);
	
	void sendAcceptScoreEmail(ScoreDTO score, UserEntity sender, List<UserEntity> receivers);
	
	void sendRefuseScoreEmail(ScoreDTO score, UserEntity sender, List<UserEntity> receivers);
}
