package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.pojos.Users;

@Service
public interface AbstractControllerService {

	public List<String> getUserRole();

	public String getAuthenticationUsername();

	public Users getUserFromUsernameType(String loginUser);

	public Users getUserById(Long id);

	int getUserLoginType(String username);
}
