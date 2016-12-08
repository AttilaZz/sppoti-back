package com.fr.repositoriesImpl;

import com.fr.RepositoriesService.CommentDaoService;
import com.fr.pojos.Comment;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Component
public class CommentDaoImpl extends GenericDaoImpl<Comment, Integer> implements CommentDaoService {

    @Value("${key.commentsPerPage}")
    private String commentPageSize;

    public CommentDaoImpl() {
        this.entityClass = Comment.class;
    }

    private int validatePerPageInjection() {
        int perPage = 0;
        try {
            perPage = Integer.parseInt(commentPageSize);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            perPage = 0;
        }

        return perPage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> getCommentsFromLastMajId(Long postId, int buttomMarker) {

        int debut = buttomMarker * validatePerPageInjection();

        Criteria cr = getSession().createCriteria(entityClass, "comment")
                .add(Restrictions.eq("comment.postComment.id", postId)).setFirstResult(debut)
                .setMaxResults(validatePerPageInjection()).addOrder(Order.desc("comment.datetimeCreated"));

        return cr.list();

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> getLastPostComment(Long postId) {

        Criteria cr = getSession().createCriteria(entityClass, "comment")
                .add(Restrictions.eq("comment.postComment.id", postId)).addOrder(Order.desc("id")).setMaxResults(1);

        return cr.list();
    }

    @Override
    public Long getCommentCount(Long postId) {
        return (Long) getSession().createCriteria(entityClass, "comment")
                .add(Restrictions.eq("comment.postComment.id", postId)).setProjection(Projections.rowCount())
                .uniqueResult();
    }

}
