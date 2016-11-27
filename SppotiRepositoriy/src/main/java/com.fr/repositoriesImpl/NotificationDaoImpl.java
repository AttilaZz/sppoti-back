package com.fr.repositoriesImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fr.RepositoriesService.NotificationDaoService;
import com.fr.pojos.Notifications;

/**
 * Created by: Wail DJENANE on Nov 10, 2016
 */
@Component
public class NotificationDaoImpl extends GenericDaoImpl<Notifications, Integer> implements NotificationDaoService {

	@Value("${key.notificationsPerPage}")
	private String notifsPageSize;

	public NotificationDaoImpl() {
		this.entityClass = Notifications.class;
	}

	private int validatePerPageInjection() {
		int perPage = 0;
		try {
			perPage = Integer.parseInt(notifsPageSize);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			perPage = 0;
		}

		return perPage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notifications> getUnseenNotifications(Long userId, int page) {
		int debut = page * validatePerPageInjection();

		Criteria cr = getSession().createCriteria(entityClass, "n");

		cr.add(Restrictions.eq("n.notifiedUserId", userId));
		cr.add(Restrictions.eq("n.isViewed", false));

		cr.setFirstResult(debut).setMaxResults(validatePerPageInjection()).addOrder(Order.desc("n.datetimeCreated"));

		return cr.list();
	}

}
