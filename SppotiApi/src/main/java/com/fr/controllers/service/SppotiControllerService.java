/**
 *
 */
package com.fr.controllers.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.fr.entities.Address;
import com.fr.entities.Sppoti;
import com.fr.entities.Sport;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService {

    void verifyAllDataBeforeSaving(String titre, Long sportId, String description, String date, Long[] teamPeopleId,
                                   Address spotAddress, int membersCount, int type, String tags) throws Exception;

    Sport getSportGame();

    void setSportGame(Sport sportGame);

    Address getAddressGame();

    void setAddressGame(Address addressGame);

    Set<Users> getTeamGame();

    void setTeamGame(Set<Users> teamGame);

    String getTitre();

    void setTitre(String titre);

    String getDescription();

    void setDescription(String description);

    boolean saveSpoot(Sppoti spotToSave);
}
