package com.fr.repositoriesImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.fr.RepositoriesService.ResourceDaoService;
import com.fr.entities.Resources;

/**
 * Created by: Wail DJENANE on Oct 5, 2016
 */
@Component
public class ResourceDaoImpl extends GenericDaoImpl<Resources, Integer> implements ResourceDaoService {

	public ResourceDaoImpl() {

		this.entityClass = Resources.class;
	}

	@Override
	public List<Resources> getLastUserAvatar(Long userId) {

		return getLastUserCoverOrAvatar(userId, 0);
	}

	@Override
	public List<Resources> getLastUserCover(Long userId) {

		return getLastUserCoverOrAvatar(userId, 1);
	}

	/*
	 * 0: Avatar
	 * 1: Cover
	 * 2: Document
	 */
	@SuppressWarnings("unchecked")
	private List<Resources> getLastUserCoverOrAvatar(Long userId, int op) {

		Criteria cr = getSession().createCriteria(entityClass, "rs");

		cr.add(Restrictions.eq("rs.user.id", userId));
		switch (op) {
		case 0:
			cr.add(Restrictions.eq("rs.type", 0));
			break;
		case 1:
			cr.add(Restrictions.eq("rs.type", 1));
			break;
		default:
			break;
		}
		
		cr.add(Restrictions.eq("rs.isSelected", true));
		cr.addOrder(Order.desc("id"));
		cr.setMaxResults(1);

		return cr.list();

	}
}
