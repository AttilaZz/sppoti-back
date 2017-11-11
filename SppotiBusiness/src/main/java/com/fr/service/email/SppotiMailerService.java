package com.fr.service.email;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface SppotiMailerService extends ApplicationMailerService
{
	void sendAddSppotiEmail(SppotiDTO Sppoti);
	
	void sendJoinSppotiEmail(SppotiDTO sppoti, UserDTO to, UserDTO from);
	
	void sendConfirmJoinSppotiEmail(SppotiDTO sppoti, UserDTO sppoter);
}
