package com.fr.service.email;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;

/**
 * Created by djenanewail on 11/13/17.
 */
public interface PostMailerService extends ApplicationMailerService
{
	void sendEmailToTargetProfileOwner(UserDTO target, PostDTO post);
}
