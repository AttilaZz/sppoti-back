package com.fr.repositoriesImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.fr.RepositoriesService.AddressDaoService;
import com.fr.entities.Address;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */

@Component
public class AddressDaoImpl extends GenericDaoImpl<Address, Integer> implements AddressDaoService {

	public AddressDaoImpl() {
		this.entityClass = Address.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Address> getLastPostAddress(Long id) {

		Criteria cr = getSession().createCriteria(entityClass, "ad");

		cr.add(Restrictions.eq("ad.post.id", id));
		cr.addOrder(Order.desc("id"));
		cr.setMaxResults(1);

		return cr.list();

	}

}
