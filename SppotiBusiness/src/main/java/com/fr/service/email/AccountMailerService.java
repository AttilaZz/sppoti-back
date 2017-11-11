package com.fr.service.email;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.TypeAccountValidation;

import java.util.Date;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface AccountMailerService extends ApplicationMailerService
{
	void sendCreateAccountConfirmationEmail(UserDTO to, String confirmationCode, TypeAccountValidation type);
	
	void sendRecoverPasswordEmail(UserDTO to, String confirmationCode, Date currentDate);
	
	void sendEmailUpdateConfirmation(String to, String confirmationCode);
}
