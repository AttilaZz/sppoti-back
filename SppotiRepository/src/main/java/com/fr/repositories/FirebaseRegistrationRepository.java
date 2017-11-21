package com.fr.repositories;

import com.fr.entities.FirebaseRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by djenanewail on 11/19/17.
 */
public interface FirebaseRegistrationRepository extends JpaRepository<FirebaseRegistrationEntity, String>
{
	Optional<FirebaseRegistrationEntity> findByNotificationKeyName(String key);
}
