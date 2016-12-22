package com.fr.repositories;

import com.fr.entities.PersistentLogin;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by djenanewail on 12/22/16.
 */
public interface HibernateTokenRepository extends CrudRepository<PersistentLogin, Long> {

    PersistentLogin getBySeries(String series);

    PersistentLogin getByUsername(String username);
}
