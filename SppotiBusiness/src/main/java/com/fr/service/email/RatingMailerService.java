package com.fr.service.email;

import com.fr.entities.SppotiEntity;
import com.fr.entities.UserEntity;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface RatingMailerService
{
	void onRatingUser(UserEntity from, UserEntity to, SppotiEntity sppoti);
}
