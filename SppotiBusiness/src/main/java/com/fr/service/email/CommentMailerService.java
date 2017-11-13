package com.fr.service.email;

import com.fr.commons.dto.UserDTO;

import java.util.List;

/**
 * Created by djenanewail on 11/13/17.
 */
public interface CommentMailerService extends ApplicationMailerService
{
	void sendEmailToPostContributors(List<UserDTO> contributors);
}
