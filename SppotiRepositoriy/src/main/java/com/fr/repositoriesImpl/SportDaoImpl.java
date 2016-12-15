package com.fr.repositoriesImpl;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.fr.RepositoriesService.SportDaoService;
import com.fr.entities.Sport;

/**
 * Created by: Wail DJENANE on May 29, 2016
 */
@Component
public class SportDaoImpl extends GenericDaoImpl<Sport, Integer> implements SportDaoService {

    Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

    public SportDaoImpl() {
        this.entityClass = Sport.class;
    }

    @Override
    public Sport getSportById(Long id) {

        Sport sport = (Sport) getSession().createCriteria(entityClass, "sport").add(Restrictions.idEq(id))
                .uniqueResult();

        return sport;
    }

}
