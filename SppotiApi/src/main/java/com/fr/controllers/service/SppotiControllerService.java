package com.fr.controllers.service;

import com.fr.entities.Sport;
import com.fr.entities.Sppoti;
import com.fr.entities.Users;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService {
    void verifyAllDataBeforeSaving(String titre, Long sportId, String description, String date,
                                   Long[] teamPeopleId, String spotAddress, int membersCount, int type, String tags) throws Exception;

    boolean saveSpoot(Sppoti spotToSave);

    Sport getSportGame();

    Set<Users> getTeamGame();
}
