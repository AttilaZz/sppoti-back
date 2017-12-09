package com.fr.service.email;

import com.fr.commons.dto.EmailUserDTO;

import java.util.List;

/**
 * Created by djenanewail on 11/13/17.
 */
public interface CommentMailerService extends ApplicationMailerService
{
	void sendEmailToPostContributors(List<EmailUserDTO> contributors);
}
