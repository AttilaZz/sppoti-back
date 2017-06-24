package com.fr.business;

import com.fr.impl.GlobalSearchServiceImpl;
import com.fr.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by djenanewail on 6/24/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GlobalSearchServiceTest
{
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private GlobalSearchServiceImpl globalSearchService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void findAllUsersFromCriteriaTest() {
	}
}
