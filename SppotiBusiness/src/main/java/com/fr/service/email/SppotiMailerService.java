package com.fr.service.email;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.enums.SppotiResponse;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface SppotiMailerService extends ApplicationMailerService
{
	void sendAddSppotiEmail(SppotiDTO Sppoti);
	
	void sendJoinSppotiEmailToSppoter(SppotiDTO sppoti, UserDTO to, UserDTO from);
	
	void sendJoinSppotiEmailToSppotiAdmin(SppotiDTO sppoti, UserDTO to, UserDTO from);
	
	void sendSppotiJoinResponseEmail(SppotiDTO sppoti, UserDTO to, UserDTO from, SppotiResponse sppotiResponse);
	
	void onSppotiEdit(SppotiDTO sppoti, UserDTO from);
	
	void onSppotiDeleted(SppotiDTO sppoti, UserDTO from);
}
