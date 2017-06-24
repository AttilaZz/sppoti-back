package com.fr.repositories;

import com.fr.entities.SppotiEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface SppotiRepository extends JpaRepository<SppotiEntity, Long>, QueryDslPredicateExecutor<SppotiEntity>
{
	
	/**
	 * Find sppoti by uuid.
	 *
	 * @param uuid
	 * 		sppoti uuid.
	 *
	 * @return sppoti entity.
	 */
	SppotiEntity findByUuid(String uuid);
	
	/**
	 * @param id
	 * 		sppoti creator id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return return all found sppoties.
	 */
	List<SppotiEntity> findByUserSppotiUuidAndDeletedFalse(String id, Pageable pageable);
}
