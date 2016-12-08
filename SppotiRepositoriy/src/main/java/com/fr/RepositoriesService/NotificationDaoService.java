package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.pojos.Notifications;

/**
 * Created by: Wail DJENANE on Nov 10, 2016
 */

@Service
public interface NotificationDaoService extends GenericDaoService<Notifications, Integer> {

	/**
	 * @param userId
	 * @param page
	 * @return
	 */
	public List<Notifications> getUnseenNotifications(Long userId, int page);

}
