package com.fr.service.email;

import com.fr.commons.dto.UserDTO;

/**
 * Created by djenanewail on 11/13/17.
 */
public interface PostMailerService extends ApplicationMailerService
{
	void sendEmailToTargetProfileOwner(UserDTO target);
}
