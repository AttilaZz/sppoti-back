package com.fr.repositoriesImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.fr.RepositoriesService.EditContentDaoService;
import com.fr.pojos.EditHistory;

/**
 * Created by: Wail DJENANE on Aug 20, 2016
 */

@Component
public class EditContentDaoImpl extends GenericDaoImpl<EditHistory, Integer> implements EditContentDaoService {

	private static final int PAGE_SIZE = 10;

	public EditContentDaoImpl() {

		this.entityClass = EditHistory.class;
	}

	@Override
	public List<EditHistory> getLastEditedPost(Long postId) {

		return getLastEditedcontent(postId, 1);

	}

	@Override
	public List<EditHistory> getLastEditedComent(Long commentId) {
		return getLastEditedcontent(commentId, 2);
	}
	
	@Override
	public List<EditHistory> getAllPostHistory(Long postid, int page) {
		return getHistory(postid, page, 1);
	}

	@Override
	public List<EditHistory> getAllComenttHistory(Long commentId, int page) {
		return getHistory(commentId, page, 2);
	}

	/*
	 * 1: post 2: comment 3: address 4: ...
	 */
	@SuppressWarnings("unchecked")
	private List<EditHistory> getHistory(Long id, int page, int op) {

		int debut = page * PAGE_SIZE;
		Criteria cr = getSession().createCriteria(entityClass, "ec");
		switch (op) {
		case 1:
			cr.add(Restrictions.eq("ec.post.id", id));
			break;
		case 2:
			cr.add(Restrictions.eq("ec.comment.id", id));
			break;
		default:
			break;
		}

		cr.setFirstResult(debut);
		cr.setMaxResults(PAGE_SIZE);
		cr.addOrder(Order.desc("ec.datetimeEdited"));

		return cr.list();
	}

	@SuppressWarnings("unchecked")
	private List<EditHistory> getLastEditedcontent(Long id, int op) {
		Criteria cr = getSession().createCriteria(entityClass, "ec");
		
		switch (op) {
		case 1:
			cr.add(Restrictions.eq("ec.post.id", id));
			break;
		case 2:
			cr.add(Restrictions.eq("ec.comment.id", id));
			break;
		default:
			break;
		}

		cr.addOrder(Order.desc("ec.id"));
		cr.setMaxResults(1);

		return cr.list();

	}

}
