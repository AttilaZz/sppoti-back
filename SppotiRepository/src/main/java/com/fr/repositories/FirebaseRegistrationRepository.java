package com.fr.repositories;

import com.fr.entities.FirebaseRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 11/19/17.
 */
public interface FirebaseRegistrationRepository extends JpaRepository<FirebaseRegistrationEntity, Long>
{
	FirebaseRegistrationEntity findByRegistrationKey(String key);
}
