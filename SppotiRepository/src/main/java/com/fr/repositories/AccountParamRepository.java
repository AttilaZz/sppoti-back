package com.fr.repositories;

import com.fr.entities.AccountParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by djenanewail on 12/17/17.
 */
public interface AccountParamRepository extends JpaRepository<AccountParamEntity, Long>
{
	Optional<AccountParamEntity> findByUserUuidAndCanReceiveEmailTrue(String userId);
	
	Optional<AccountParamEntity> findByUserUuidAndCanReceiveNotificationTrue(String userId);
}
