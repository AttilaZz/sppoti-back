package com.fr.service.email;

import com.fr.commons.dto.UserDTO;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface LikeMailerService
{
	void sendEmailToPostPublisher(UserDTO user);
}
