package com.fr.service.email;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.enums.SppotiResponse;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface SppotiMailerService extends ApplicationMailerService
{
	void onCreateSppoti(SppotiDTO sppoti, UserDTO to, UserDTO from);
	
	void onSendingJoinRequestToSppoti(SppotiDTO sppoti, UserDTO to, UserDTO from);
	
	void onRespondingToSppotiJoinRequestFromSppoter(SppotiDTO sppoti, UserDTO to, UserDTO from,
													SppotiResponse sppotiResponse);
	
	void onRespondingToSppotiJoinRequestFromSppotiAdmin(SppotiDTO sppoti, UserDTO to, UserDTO from,
														SppotiResponse sppotiResponse);
	
	void onSppotiEdit(SppotiDTO sppoti, UserDTO from);
	
	void onSppotiDeleted(SppotiDTO sppoti, UserDTO from);
}
