package com.fr.RepositoriesService;

import org.springframework.stereotype.Service;

import com.fr.entities.Roles;

/**
 * Created by: Wail DJENANE on May 29, 2016
 */
@Service("profileService")
public interface ProfileDaoService extends GenericDaoService<Roles, Integer> {

    Roles getProfileEntityByType(String type);

}
