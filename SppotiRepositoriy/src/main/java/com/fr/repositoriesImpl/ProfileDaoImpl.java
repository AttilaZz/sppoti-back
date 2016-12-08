package com.fr.repositoriesImpl;

import javax.transaction.Transactional;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fr.RepositoriesService.ProfileDaoService;
import com.fr.pojos.UserRoles;

/**
 * Created by: Wail DJENANE on May 29, 2016
 */

@Repository("profileDao")
@Transactional
public class ProfileDaoImpl extends GenericDaoImpl<UserRoles, Integer> implements ProfileDaoService {

	public ProfileDaoImpl() {
		this.entityClass = UserRoles.class;
	}

	public UserRoles getProfileEntityByType(String type) {
		UserRoles userProfile = (UserRoles) getSession().createCriteria(entityClass, "profile")
				.add(Restrictions.eq("name", type)).uniqueResult();

		return userProfile;
	}

}
