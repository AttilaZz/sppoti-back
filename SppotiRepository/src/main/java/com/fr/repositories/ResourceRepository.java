package com.fr.repositories;

import com.fr.entities.ResourcesEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by djenanewail on 12/19/16.
 */

public interface ResourceRepository extends CrudRepository<ResourcesEntity, Long> {

    ResourcesEntity getByUserIdAndTypeAndIsSelectedTrue(Long id, int type);

}
