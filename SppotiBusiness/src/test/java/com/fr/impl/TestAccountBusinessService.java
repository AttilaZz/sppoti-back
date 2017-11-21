package com.fr.impl;

import com.fr.commons.dto.FirebaseDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.entities.FirebaseRegistrationEntity;
import com.fr.entities.UserEntity;
import com.fr.repositories.FirebaseRegistrationRepository;
import com.fr.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Created by djenanewail on 11/19/17.
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BusinessTestConfiguration.class)
public class TestAccountBusinessService
{
	private static final String REGISTRATION_KEY = "L0987654DFGHJK";
	private static final String USER_KEY = "0987654321";
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private FirebaseRegistrationRepository firebaseRegistrationRepository;
	
	@InjectMocks
	private AccountBusinessServiceImpl accountBusinessService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = BusinessGlobalException.class)
	public void should_not_save_firebase_registration_key_if_user_not_exist() {
		
		when(this.userRepository.getByUuidAndDeletedFalseAndConfirmedTrue(USER_KEY)).thenReturn(Optional.empty());
		
		this.accountBusinessService.saveFirebaseRegistrationKey(buildFirebaseDTO());
	}
	
	@Test
	public void should_save_firebase_registration_id_if_user_has_been_found() throws Exception {
		when(this.userRepository.getByUuidAndDeletedFalseAndConfirmedTrue(USER_KEY))
				.thenReturn(Optional.of(new UserEntity()));
		
		this.accountBusinessService.saveFirebaseRegistrationKey(buildFirebaseDTO());
		
		Mockito.verify(this.firebaseRegistrationRepository, times(1))
				.save(Mockito.any(FirebaseRegistrationEntity.class));
	}
	
	private FirebaseDTO buildFirebaseDTO() {
		final FirebaseDTO dto = new FirebaseDTO();
		dto.setRegistrationId(REGISTRATION_KEY);
		dto.setUserId(USER_KEY);
		return dto;
	}
	
}
