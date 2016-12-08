package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.models.SignUpRequest;
import com.fr.entities.UserRoles;
import com.fr.entities.Sport;
import com.fr.entities.Users;

@Service
public interface SignUpService {

	public boolean isEmailContentValid(String email);

	public boolean isEmailFormValid(String email);

	public boolean isUsernameValid(String username);

	public boolean isEmailConfirmed(String email);

	public boolean saveNewUser(Users user);

	public UserRoles getProfileEntity(String profileType);

	public Sport getSportById(Long id);

	public boolean isReceivedDataNotEmpty(SignUpRequest user);

	public List<Sport> getAllSports();
	
	public boolean tryActivateAccount(String code);

	boolean sendConfirmationEmail(String email, String code);
}
