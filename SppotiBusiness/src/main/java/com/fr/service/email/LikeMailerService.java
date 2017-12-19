package com.fr.service.email;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface LikeMailerService
{
	void sendEmailToPostOwner(UserDTO user, PostDTO post);
	
	void sendEmailToCommentOwner(UserDTO user, CommentDTO comment);
}
