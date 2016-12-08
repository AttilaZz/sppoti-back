package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.entities.Resources;

/**
 * Created by: Wail DJENANE on Oct 5, 2016
 */

@Service
public interface ResourceDaoService extends GenericDaoService<Resources, Integer> {

	/**
	 * @param userId
	 * @return
	 */
	List<Resources> getLastUserAvatar(Long userId);

	/**
	 * @param userId
	 * @return
	 */
	List<Resources> getLastUserCover(Long userId);

}
