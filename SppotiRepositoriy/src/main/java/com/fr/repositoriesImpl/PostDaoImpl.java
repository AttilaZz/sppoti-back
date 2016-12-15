package com.fr.repositoriesImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fr.RepositoriesService.PostDaoService;
import com.fr.entities.Post;

/**
 * Created by: Wail DJENANE on Jun 4, 2016
 */

@Repository("postDao")
public class PostDaoImpl extends GenericDaoImpl<Post, Integer> implements PostDaoService {

    @Value("${key.postsPerPage}")
    private String postsPageSize;

    public PostDaoImpl() {
        this.entityClass = Post.class;
    }

    private int validatePerPageInjection() {
        int perPage = 0;
        try {
            perPage = Integer.parseInt(postsPageSize);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            perPage = 0;
        }

        return perPage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getPostsFromLastPage(Long userId, int page) {

        int debut = page * validatePerPageInjection();

        Criteria cr = getSession().createCriteria(entityClass, "post");

        cr.add(Restrictions.eq("post.user.id", userId));

        cr.setFirstResult(debut).setMaxResults(validatePerPageInjection()).addOrder(Order.desc("post.datetimeCreated"));

        return cr.list();

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getPostsFromSubscribedUserSports(Long[] sportsId, int buttomMarker) {

        int debut = buttomMarker * validatePerPageInjection();
        return getSession().createCriteria(entityClass, "post").add(Restrictions.in("post.sport.id", sportsId))
                // .add(Restrictions.eq("post.userPost.id", userId))
                .setFirstResult(debut).setMaxResults(validatePerPageInjection())
                .addOrder(Order.desc("post.datetimeCreated"))
                // .add( Restrictions.disjunction()
                // .add( Restrictions.isNull("age") )
                // .add( Restrictions.eq("age", new Integer(0) ) )
                // .add( Restrictions.eq("age", new Integer(1) ) )
                // .add( Restrictions.eq("age", new Integer(2) ) )
                // )
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getPhotoGalleryPostsFromLastMajId(Long userId, int buttomMarker) {

        int debut = buttomMarker * validatePerPageInjection();

        Criteria cr = getSession().createCriteria(entityClass, "post").add(Restrictions.eq("post.user.id", userId))
                .add(Restrictions.isNotNull("post.album")).setFirstResult(debut)
                .setMaxResults(validatePerPageInjection()).addOrder(Order.desc("post.datetimeCreated"));

        return cr.list();

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getVideoGalleryPostsFromLastMajId(Long userId, int buttomMarker) {

        int debut = buttomMarker * validatePerPageInjection();

        Criteria cr = getSession().createCriteria(entityClass, "post").add(Restrictions.eq("post.userPost.id", userId))
                .add(Restrictions.isNotNull("post.videoLink")).setFirstResult(debut)
                .setMaxResults(validatePerPageInjection()).addOrder(Order.desc("post.datetimeCreated"));

        return cr.list();

    }

}