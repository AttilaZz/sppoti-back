package com.fr.RepositoriesService;

import java.io.Serializable;
import java.util.List;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
public interface GenericDaoService<E, ID> {

    // HIBERNATE
    Long saveAndGetId(final E E);

    Serializable save(final E E);

    boolean persist(final E E);

    boolean merge(final E E);

    boolean delete(final E E);

    boolean delete(String Entity, final E E);

    boolean update(final E E);

    boolean saveOrUpdate(final E E);

    E getEntityByID(final Long ID);

    boolean isEntityExist(Object pojo);

    /*
     * Check if an entity exist -> useful for loging and testing email existance
     * ...
     */
    List<E> getSimilarEntity(Object pojo);

    // get all entities
    List<E> getAll();

    // Case sensitive form of the above restriction.
    List<E> likeCriteria(String field, String likeWhat);

    // Getting result in ascending order
    List<E> OrderAscCriteria(String field);

    // Getting result in desending order
    List<E> OrderDesCriteria(String field);

    // To get records having salary in between 1000 and 2000
    List<E> betweenCriteria(String field, Long param1, Long param2);

    // To get records having PARAM1 more than 2000
    List<E> moreThanCriteria(String field, Long param1);

    // To get records having salary less than 2000
    List<E> lessThanCriteria(String field, Long param1);

    // execute a native sql request for more options
    List<E> nativeSqlRequest(String sqlRequest);

    /**
     * @return
     */
    int deleteAll();

}
