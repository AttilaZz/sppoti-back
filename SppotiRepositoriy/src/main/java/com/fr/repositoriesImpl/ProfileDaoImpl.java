package com.fr.repositoriesImpl;

import javax.transaction.Transactional;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.fr.RepositoriesService.ProfileDaoService;
import com.fr.entities.Roles;

/**
 * Created by: Wail DJENANE on May 29, 2016
 */
@Component
public class ProfileDaoImpl extends GenericDaoImpl<Roles, Integer> implements ProfileDaoService {

    public ProfileDaoImpl() {
        this.entityClass = Roles.class;
    }

    public Roles getProfileEntityByType(String type) {
        Roles userProfile = (Roles) getSession().createCriteria(entityClass, "profile")
                .add(Restrictions.eq("name", type)).uniqueResult();

        return userProfile;
    }

}
