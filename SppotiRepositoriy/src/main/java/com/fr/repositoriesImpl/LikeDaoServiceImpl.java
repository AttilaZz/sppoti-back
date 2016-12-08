package com.fr.repositoriesImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fr.RepositoriesService.LikeDaoService;
import com.fr.RepositoriesService.UserDaoService;
import com.fr.pojos.LikeContent;

/**
 * Created by: Wail DJENANE on Aug 15, 2016
 */
@Component
public class LikeDaoServiceImpl extends GenericDaoImpl<LikeContent, Integer> implements LikeDaoService {

	@Autowired
	UserDaoService userDao;

	public LikeDaoServiceImpl() {
		this.entityClass = LikeContent.class;
	}

	@Value("${key.likesPerPage}")
	private String likesPageSize;

	private int validatePerPageInjection() {
		int perPage = 0;
		try {
			perPage = Integer.parseInt(likesPageSize);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			perPage = 0;
		}

		return perPage;
	}

	@Override
	public boolean unLikeComment(Long commentId, Long userId) {

		LikeContent likeToDelete = getLikeCommentRow(commentId, userId);

		return delete(likeToDelete);

	}

	@Override
	public boolean unLikePost(Long posttId, Long userId) {

		LikeContent likeToDelete = getLikePostRow(posttId, userId);

		return delete(likeToDelete);

	}

	@Override
	public boolean isPostAlreadyLiked(Long postId, Long userId) {
		return this.getLikePostRow(postId, userId) != null;

	}

	private LikeContent getLikePostRow(Long postId, Long userId) {
		LikeContent likePostRow = (LikeContent) getSession().createCriteria(entityClass, "like")
				.add(Restrictions.eq("like.post.id", postId)).add(Restrictions.eq("like.user.id", userId))
				.uniqueResult();

		return likePostRow;
	}

	private LikeContent getLikeCommentRow(Long commentId, Long userId) {
		LikeContent likePostRow = (LikeContent) getSession().createCriteria(entityClass, "like")
				.add(Restrictions.eq("like.comment.id", commentId)).add(Restrictions.eq("like.user.id", userId))
				.uniqueResult();

		return likePostRow;
	}

	@Override
	public boolean isCommentAlreadyLiked(Long commentId, Long userId) {
		return this.getLikeCommentRow(commentId, userId) != null;
	}

	/*
	 * Get list of likers, ordered by the like time
	 */
	@Override
	public List<LikeContent> getPostLikers(Long postId, int page) {
		return getLikers(postId, page, 1);
	}

	@Override
	public List<LikeContent> getCommentLikers(Long commentId, int page) {
		return getLikers(commentId, page, 2);
	}

	/*
	 * -- OPERATION TYPE -- 1: post 2: comment
	 */
	@SuppressWarnings("unchecked")
	private List<LikeContent> getLikers(Long id, int page, int op) {

		int debut = page * validatePerPageInjection();

		Criteria cr = getSession().createCriteria(entityClass, "like");
		cr.setFirstResult(debut).setMaxResults(validatePerPageInjection());
		cr.addOrder(Order.desc("like.datetimeCreated"));

		switch (op) {
		case 1:
			cr.add(Restrictions.eq("like.post.id", id));
			break;
		case 2:
			cr.add(Restrictions.eq("like.comment.id", id));
			break;
		default:
			break;
		}

		List<LikeContent> dbList = cr.list();

		return dbList;
	}
}
