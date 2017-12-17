package com.fr.service.email;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.EmailUserDTO;
import com.fr.commons.dto.post.PostDTO;

import java.util.List;

/**
 * Created by djenanewail on 11/13/17.
 */
public interface CommentMailerService extends ApplicationMailerService
{
	void sendEmailToPostContributors(final PostDTO postDTO, CommentDTO comment, List<EmailUserDTO> contributors);
}
