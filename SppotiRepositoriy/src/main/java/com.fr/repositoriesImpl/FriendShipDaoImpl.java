package com.fr.repositoriesImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fr.RepositoriesService.FriendShipDaoService;
import com.fr.RepositoriesService.UserDaoService;
import com.fr.pojos.FriendShip;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
@Component
public class FriendShipDaoImpl extends GenericDaoImpl<FriendShip, Integer> implements FriendShipDaoService {

	@Autowired
	UserDaoService userDao;

	@Value("${key.friendShipPerPage}")
	private String friendshipPageSize;

	public FriendShipDaoImpl() {
		this.entityClass = FriendShip.class;
	}

	private int validatePerPageInjection() {
		int perPage = 0;
		try {
			perPage = Integer.parseInt(friendshipPageSize);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			perPage = 0;
		}

		return perPage;
	}

	private FriendShip friendExample(Boolean isPending, Boolean isConfirmed, Long userId) {
		FriendShip f = new FriendShip();
		f.setUser(userDao.getEntityByID(userId));
		f.setPending(true);
		f.setConfirmed(isConfirmed);

		return f;

	}

	@Override
	public int getPendingFriendRequestCount(Long userId) {
		FriendShip f = friendExample(true, false, userId);

		return getSession().createCriteria(entityClass).add(Example.create(f)).list().size();

	}

	@Override
	public int getConfirmedFriendRequestCount(Long userId) {
		FriendShip f = friendExample(false, true, userId);

		return getSession().createCriteria(entityClass).add(Example.create(f)).list().size();
	}

	@Override
	public List<FriendShip> getPendingFriendList(Long userId, int buttomMarker) {

		int debut = buttomMarker * validatePerPageInjection();

		// FriendShip f = friendExample(true, false, userId);
		return initFriendListToSend(debut, userId, true);
	}

	@Override
	public List<FriendShip> getConfirmedFriendList(Long userId, int buttomMarker) {

		int debut = buttomMarker * validatePerPageInjection();

		return initFriendListToSend(debut, userId, false);
	}

	@SuppressWarnings("unchecked")
	private List<FriendShip> initFriendListToSend(int debut, Long userId, boolean isPendingSearch) {
		SimpleExpression pendingOrConfirmed;
		if (isPendingSearch) {
			pendingOrConfirmed = Restrictions.eq("f.isPending", true);

		} else {
			pendingOrConfirmed = Restrictions.eq("f.isConfirmed", true);
		}

		List<FriendShip> pendingList = getSession().createCriteria(entityClass, "f")
				// .add(Example.create(f))
				.setFirstResult(debut).setMaxResults(validatePerPageInjection()).add(pendingOrConfirmed)
				.add(Restrictions.eq("f.user.id", userId)).createAlias("f.friend", "userFriend")
				.setProjection(Projections.projectionList().add(Projections.property("userFriend.id"))
						.add(Projections.property("userFriend.firstName"))
						.add(Projections.property("userFriend.lastName"))
						// TODO: Add avatar
						// .add(Projections.property("userFriend.avatar"))
						.add(Projections.property("f.datetime")))
				.addOrder(Order.desc("f.datetime")).list();

		return pendingList;
	}

	@Override
	public FriendShip getConfirmedFriendShip(Long userId, Long friendId) {

		return getFriendShipFromStatus(userId, friendId, 1);
	}

	@Override
	public FriendShip getPendingFriendShip(Long userId, Long friendId) {

		return getFriendShipFromStatus(userId, friendId, 2);
	}

	@Override
	public FriendShip getFriendShip(Long userId, Long friendId) {

		return getFriendShipFromStatus(userId, friendId, 0);
	}

	private FriendShip getFriendShipFromStatus(Long userId, Long friendId, int status) {

		Criteria cr = getSession().createCriteria(entityClass, "f");
		cr.add(Restrictions.eq("f.friend.id", friendId));
		cr.add(Restrictions.eq("f.user.id", userId));

		switch (status) {
		case 1:// pending
			cr.add(Restrictions.eq("f.isConfirmed", true));
			break;
		case 2:// confirmed
			cr.add(Restrictions.eq("f.isPending", true));
			break;
		default:
			break;
		}

		FriendShip friendShip = (FriendShip) cr.uniqueResult();

		return friendShip;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FriendShip> getFriendsFromPrefix(Long userId, String friendPrefix, int page) {

		int perPage = 0;
		int debut = 0;

		try {
			perPage = Integer.parseInt(friendshipPageSize);
			debut = page * perPage;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		Criteria cr = getSession().createCriteria(entityClass, "f");
		cr.add(Restrictions.eq("f.isConfirmed", true));
		cr.createCriteria("f.user").add(Restrictions.eq("id", userId));

		cr.createCriteria("f.friend").add(Property.forName("username").like(friendPrefix + "%"))
				.addOrder(Property.forName("username").asc());

		cr.setFirstResult(debut).setMaxResults(perPage);

		return cr.list();

	}
}
