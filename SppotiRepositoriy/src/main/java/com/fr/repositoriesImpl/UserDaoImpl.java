package com.fr.repositoriesImpl;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fr.RepositoriesService.ResourceDaoService;
import com.fr.RepositoriesService.UserDaoService;
import com.fr.entities.Resources;
import com.fr.entities.Sport;
import com.fr.entities.Users;

/**
 * @author Wail DJENANE
 * @since 25/01/2016
 */

@Repository("userDao")
@Transactional
public class UserDaoImpl extends GenericDaoImpl<Users, Integer> implements UserDaoService {

	@Autowired
	private ResourceDaoService resourceDao;

	@Value("${key.usersSearchPerPage}")
	private String usersPageSize;

	Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

	public UserDaoImpl() {
		this.entityClass = Users.class;
	}

	@Override
	public boolean findUser(String email, String password) {

		Users user = new Users();
		user.setEmail(email);
		user.setPassword(password);

		return !getSession().createCriteria(entityClass).add(Example.create(user)).list().isEmpty();
	}

	@Override
	public boolean isEmailExist(String email) {

		List<Users> lUsers = nativeSqlRequest("Select * from users where email='" + email + "'");
		LOGGER.info("Matching found = " + lUsers.size());
		return !lUsers.isEmpty();
	}

	@Override
	public boolean isUsernameExist(String username) {

		List<Users> lUsers = nativeSqlRequest("Select * from users where username='" + username + "'");
		LOGGER.info("Matching found = " + lUsers.size());
		return !lUsers.isEmpty();
	}

	@Override
	public Users getUserWithAllDataById(Long userId) {

		Users user = (Users) getSession().createCriteria(entityClass, "user").add(Restrictions.idEq(userId))
				.uniqueResult();

		// Hibernate.initialize(Users.class);
		// Hibernate.initialize(Profile.class);
		// Hibernate.initialize(Post.class);
		// Hibernate.initialize(Sport.class);
		// Hibernate.initialize(Messages.class);

		return user;
	}

	@Override
	public Set<Sport> getAllUserSubscribedSports(Long userId) {

		return getEntityByID(userId).getRelatedSports();
	}

	@Override
	public boolean performActivateAccount(String code) {
		Users u = new Users();
		u.setConfirmationCode(code);

		List<Users> foundUser = getSimilarEntity(u);

		if (!foundUser.isEmpty()) {
			foundUser.get(0).setConfirmed(true);
			this.update(foundUser.get(0));
			return true;
		}

		return false;
	}

	@Override
	public Users getUserFromloginUsername(String login, int loginType) {
		/*
		 * 1: username 2: email 3: phone
		 */

		SimpleExpression mRestriction = null;

		switch (loginType) {
		case 1:
			mRestriction = Restrictions.eq("username", login);
			break;
		case 2:
			mRestriction = Restrictions.eq("email", login);
			break;
		case 3:
			mRestriction = Restrictions.eq("telephone", login);
			break;
		default:
			break;
		}

		Users user = (Users) getSession().createCriteria(entityClass, "user").add(mRestriction).uniqueResult();

		return user;

	}

	@Override
	public List<?> getHeaderData(Long userId) {

		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("firstName"));
		projList.add(Projections.property("lastName"));
		projList.add(Projections.property("username"));

		List<?> luser = getSession().createCriteria(entityClass, "user").add(Restrictions.idEq(userId))
				.setProjection(projList).list();

		return luser;
	}

	@Override
	public List<Resources> getLastCover(Long userId) {

		return resourceDao.getLastUserCover(userId);

	}

	@Override
	public List<Resources> getLastAvatar(Long userId) {

		return resourceDao.getLastUserAvatar(userId);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Users> getUsersFromPrefix(String prefix, int page) {
		int perPage = 0;
		int debut = 0;

		try {
			perPage = Integer.parseInt(usersPageSize);
			debut = page * perPage;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		List users = getSession().createCriteria(entityClass, "user")
				.add(Property.forName("username").like(prefix + "%"))
				.addOrder(Property.forName("username").asc())
				.setFirstResult(debut).setMaxResults(perPage).list();

		return users;

	}
}
