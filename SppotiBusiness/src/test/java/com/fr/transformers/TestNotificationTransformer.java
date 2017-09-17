package com.fr.transformers;

import com.fr.BusinessTestConfiguration;
import com.fr.impl.NotificationBusinessServiceImpl;
import com.fr.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by djenanewail on 9/16/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BusinessTestConfiguration.class)
public class TestNotificationTransformer
{
	@Mock
	NotificationTransformer notificationTransformer;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	SimpMessagingTemplate simpMessagingTemplate;
	
	@InjectMocks
	private NotificationBusinessServiceImpl notificationBusinessService;
	
	@Test
	public void testBuildNotificationEntityWithAnEmptyObjectToSend() {
	}
}
