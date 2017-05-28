package com.fr.repositories;

import com.fr.entities.SppotiEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface SppotiRepository extends JpaRepository<SppotiEntity, Long>
{
	
	/**
	 * Find sppoti by uuid.
	 *
	 * @param uuid
	 * 		sppoti uuid.
	 *
	 * @return sppoti entity.
	 */
	SppotiEntity findByUuid(int uuid);
	
	/**
	 * @param id
	 * 		sppoti creator id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return return all found sppoties.
	 */
	List<SppotiEntity> findByUserSppotiUuidAndDeletedFalse(Integer id, Pageable pageable);
}
