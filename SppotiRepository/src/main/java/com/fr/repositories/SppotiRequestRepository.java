package com.fr.repositories;

import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.SppotiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Optional;

/**
 * Created by djenanewail on 7/4/17.
 */
public interface SppotiRequestRepository extends JpaRepository<SppotiRequest, Long>,
		QueryDslPredicateExecutor<SppotiRequest>
{
	
	Optional<SppotiRequest> findBySppotiUuidAndUserUuidAndStatus(String sppotiId, String id,
																 GlobalAppStatusEnum pending);
}
