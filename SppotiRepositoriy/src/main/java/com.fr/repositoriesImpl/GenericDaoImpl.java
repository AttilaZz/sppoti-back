package com.fr.repositoriesImpl;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fr.RepositoriesService.GenericDaoService;

@Repository("DAO")
@Transactional
public abstract class GenericDaoImpl<E, id extends Serializable> implements GenericDaoService<E, id> {

    protected Class<E> entityClass;

    // public abstract void initializeEntityClass();

    protected SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public GenericDaoImpl() {
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();

    }

    @Override
    public Long saveAndGetId(E o) {
        Serializable result = getSession().save(o);

        Long r = (Long) result;
        return r;
    }

    @Override
    public Serializable save(E o) {
        try {
            return getSession().save(o);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean persist(E o) {
        try {
            getSession().persist(o);
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean merge(E o) {
        try {
            getSession().merge(o);
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(E o) {

        try {
            getSession().delete(o);
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(String Entity, E o) {
        try {
            getSession().delete(Entity, o);
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int deleteAll() {
        String hql = String.format("delete from %s", this.entityClass.getName());
        Query query = getSession().createQuery(hql);
        return query.executeUpdate();
    }

    @Override
    public boolean update(E o) {

        try {
            getSession().update(o);
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdate(E o) {

        try {
            getSession().saveOrUpdate(o);
            getSession().flush();
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /***/
    @Override
    @SuppressWarnings("unchecked")
    public E getEntityByID(final Long ID) {
        try {
            return (E) getSession().get(entityClass, ID);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> getSimilarEntity(Object pojo) {
        try {
            return getSession().createCriteria(entityClass).add(Example.create(pojo)).list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean isEntityExist(Object pojo) {
        try {
            return !getSession().createCriteria(entityClass).add(Example.create(pojo)).list().isEmpty();
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> getAll() {
        List<E> list = null;

        try {
            final Criteria crit = getSession().createCriteria(entityClass);
            list = crit.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> likeCriteria(String field, String likeWhat) {
        try {
            Criteria cr = getSession().createCriteria(entityClass);
            return cr.add(Restrictions.like(field, likeWhat)).list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<E> OrderAscCriteria(String field) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<E> OrderDesCriteria(String field) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> betweenCriteria(String field, Long param1, Long param2) {
        try {

            Criteria cr = getSession().createCriteria(entityClass);
            cr.add(Restrictions.between(field, param1, param2));
            return cr.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> moreThanCriteria(String field, Long param1) {
        try {

            Criteria cr = getSession().createCriteria(entityClass);
            cr.add(Restrictions.gt(field, param1));
            return cr.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> lessThanCriteria(String field, Long param1) {
        try {

            Criteria cr = getSession().createCriteria(entityClass);
            cr.add(Restrictions.lt(field, param1));
            return cr.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> nativeSqlRequest(String sqlRequest) {
        try {
            return getSession().createSQLQuery(sqlRequest).list();

        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

}
