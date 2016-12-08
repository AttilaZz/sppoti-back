package com.fr.repositoriesImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.fr.RepositoriesService.MessageDaoService;
import com.fr.entities.Messages;

/**
 * Created by: Wail DJENANE on Jun 16, 2016
 */
@Component
public class MessageDaoImpl extends GenericDaoImpl<Messages, Integer> implements MessageDaoService {

	private static final int PAGE_SIZE = 10;

	public MessageDaoImpl() {
		this.entityClass = Messages.class;
	}

	@Override
	public Messages getMessageById(Long id) {
		return getEntityByID(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Messages> getAllSentMessagesByUserId(Long userId, int buttomMarker) {
		int debut = buttomMarker * PAGE_SIZE;

		Criteria cr = getSession().createCriteria(entityClass, "msg").add(Restrictions.eq("userMessage.id", userId))
				.setFirstResult(debut).setMaxResults(PAGE_SIZE).addOrder(Order.desc("datetime"));

		return cr.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Messages> getAllReceivedtMessagesByUserId(Long userId, int buttomMarker) {
		int debut = buttomMarker * PAGE_SIZE;

		Criteria cr = getSession().createCriteria(entityClass, "msg").add(Restrictions.eq("receiver_id", userId))
				.setFirstResult(debut).setMaxResults(PAGE_SIZE).addOrder(Order.desc("datetime"));

		return cr.list();
	}

}
