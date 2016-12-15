/**
 *
 */
package com.fr.controllers.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fr.controllers.service.ActuControllerService;
import com.fr.entities.Post;
import com.fr.entities.Sport;

/**
 * Created by: Wail DJENANE on Jun 21, 2016
 */
@Component
public class ActuControllerServiceImpl extends AbstractControllerServiceImpl implements ActuControllerService {

    private List<Sport> getAllUserSubscribedSports(Long userId) {
        Set<Sport> userSp = userDaoService.getAllUserSubscribedSports(userId);
        List<Sport> userSpList = new ArrayList<>();
        for (Sport s : userSp) {
            userSpList.add(s);
        }
        return userSpList;

    }

    @Override
    public List<Post> getAllUserFriendPosts(Long userId, int pageId) {
        List<Long> sportId = new ArrayList<>();

        List<Sport> sports = getAllUserSubscribedSports(userId);

        for (Sport sport : sports) {
            sportId.add(sport.getId());
        }

        Long[] sp = new Long[sports.size()];
        sp = sportId.toArray(sp);

        return postDaoService.getPostsFromSubscribedUserSports(sp, pageId);
    }

}
