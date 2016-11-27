/**
 * 
 */
package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.pojos.Post;

/**
 * Created by: Wail DJENANE on Jun 21, 2016
 */
@Service
public interface ActuControllerService extends AbstractControllerService {

	public List<Post> getAllUserFriendPosts(Long userId, int pageId);
}
