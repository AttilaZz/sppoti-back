/**
 *
 */
package com.fr.RepositoriesService;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fr.entities.Resources;
import com.fr.entities.Sport;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Service("userService")
public interface UserDaoService extends GenericDaoService<Users, Integer> {
    boolean findUser(String email, String password);

    boolean isEmailExist(String email);

    boolean isUsernameExist(String username);

    Users getUserWithAllDataById(Long id);

    Set<Sport> getAllUserSubscribedSports(Long userId);

    boolean performActivateAccount(String code);

    Users getUserFromloginUsername(String login, int loginType);

    /**
     * @param userId
     * @return
     */
    List<?> getHeaderData(Long userId);

    /**
     * @param userId
     * @return
     */
    List<Resources> getLastAvatar(Long userId);

    /**
     * @param prefix
     * @param page
     * @param i
     * @return
     */
    List<Users> getUsersFromPrefix(String prefix, int page);

    /**
     * @param userId
     * @return
     */
    List<Resources> getLastCover(Long userId);


}
