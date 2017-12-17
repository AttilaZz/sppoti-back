package com.fr;

import com.fr.entities.AccountParamEntity;
import com.fr.entities.UserEntity;
import com.fr.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by djenanewail on 12/16/17.
 */
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent>
{
	private final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
	
	}
	
	private void fillAccountParamTable() {
		this.LOGGER.info("Launching USER table refresh ...");
		final List<UserEntity> list = this.userRepository.findAll();
		
		this.LOGGER.info("USER table contains {} entries", list.size());
		
		final List<UserEntity> usersToUpdate = new ArrayList<>();
		
		list.forEach(u -> {
			if (Objects.isNull(u.getParamEntity())) {
				this.LOGGER.info("User <{}> has been added to update list", u.getEmail());
				final AccountParamEntity paramEntity = new AccountParamEntity();
				paramEntity.setCanReceiveEmail(true);
				paramEntity.setCanReceiveNotification(true);
				paramEntity.setUser(u);
				u.setParamEntity(paramEntity);
				usersToUpdate.add(u);
			}
		});
		
		this.LOGGER.info("Writing to database ...");
		this.userRepository.save(usersToUpdate);
		this.userRepository.flush();
	}
}
