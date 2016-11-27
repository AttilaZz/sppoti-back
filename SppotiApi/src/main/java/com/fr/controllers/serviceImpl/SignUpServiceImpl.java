package com.fr.controllers.serviceImpl;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fr.controllers.service.SignUpService;
import com.fr.models.SignUpRequest;
import com.fr.pojos.Sport;
import com.fr.pojos.UserRoles;
import com.fr.pojos.Users;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */

@Component
public class SignUpServiceImpl extends AbstractControllerServiceImpl implements SignUpService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${key.originBack}")
	private String rootAddress;

	@Override
	public boolean isEmailContentValid(String email) {
		return !userDaoService.isEmailExist(email);
	}

	@Override
	public boolean isEmailFormValid(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUsernameValid(String username) {
		return !userDaoService.isUsernameExist(username);
	}

	@Override
	public boolean isEmailConfirmed(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveNewUser(Users user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		try {
			userDaoService.save(user);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}

	@Override
	public UserRoles getProfileEntity(String profileType) {
		return profileDaoService.getProfileEntityByType(profileType);
	}

	@Override
	public Sport getSportById(Long id) {
		return sportDaoService.getSportById(id);
	}

	@Override
	public boolean isReceivedDataNotEmpty(SignUpRequest user) {
		if (user.getFirstName() != null && !user.getFirstName().isEmpty() && user.getLastName() != null
				&& !user.getLastName().isEmpty() && user.getEmail() != null && !user.getEmail().isEmpty()
				&& user.getUsername() != null && !user.getUsername().isEmpty() && user.getPassword() != null
				&& !user.getPassword().isEmpty() && user.getSexe() != null && !user.getSexe().isEmpty()
				&& user.getSportId() != null && user.getSportId().length > 0 && user.getDateBorn() != null
				&& !user.getDateBorn().isEmpty())
			return true;

		return false;
	}

	@Override
	public List<Sport> getAllSports() {
		return sportDaoService.getAll();
	}

	@Override
	public boolean tryActivateAccount(String code) {
		return userDaoService.performActivateAccount(code);
	}

	@Override
	public boolean sendConfirmationEmail(String email, String code) {

		// String rootAddress =
		// globalAddressConfigProperties().getProperty("originBack");
		String link = rootAddress + "SppotiWebAppGui/inscription/create/user/" + code;
		String body = "<H2>Account confirmation</H2><br/><br/>"
				+ "<p>Please Click the link or copy/paste in the broser to corfirm your account</p><br/>" + "<p>" + link
				+ "</p>";
		// Send a composed mail
		try {
			mailer.sendMail2(email, "Account Confirmation", body);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
