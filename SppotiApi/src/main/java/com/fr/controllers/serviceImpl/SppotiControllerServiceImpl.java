/**
 *
 */
package com.fr.controllers.serviceImpl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import com.fr.models.User;
import org.springframework.stereotype.Component;

import com.fr.controllers.service.SppotiControllerService;
import com.fr.exceptions.EmptyArgumentException;
import com.fr.entities.Sport;
import com.fr.entities.Sppoti;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
public class SppotiControllerServiceImpl extends AbstractControllerServiceImpl implements SppotiControllerService {

    private Sport sportGame;
    private String addressGame;
    private Set<Users> teamGame;
    private String titre;
    private String description;
    private int teamCount;
    private int sppotiType;
    private String tags;

    public SppotiControllerServiceImpl() {
        sportGame = new Sport();
        teamGame = new HashSet<>();
    }

    @Override
    public void verifyAllDataBeforeSaving(String titre, Long sportId, String description, String date,
                                          Long[] teamPeopleId, String spotAddress, int membersCount, int type, String tags) throws Exception {

        if (titre == null || titre.isEmpty()) {

            throw new EmptyArgumentException("Missing Argument (TITLE) in the JSON request");

        }
        if (sportId == null) {
            throw new EmptyArgumentException("Missing Argument (SPORT-ID) n the JSON request");

        }
        if (description == null || description.isEmpty()) {
            throw new EmptyArgumentException("Missing Argument (DESCRIPTION) in the JSON request");

        }
        if (date == null || date.isEmpty()) {
            throw new EmptyArgumentException("Missing Argument (DATE) in the JSON request");

        }

        if (teamPeopleId == null || teamPeopleId.length == 0) {
            throw new EmptyArgumentException("Missing or Empty Argument (TEAM-PEOPLE) in the JSON request");

        }

        if (spotAddress == null) {
            throw new EmptyArgumentException("Missing Argument (ADDRESS) in the JSON request");

        }

//        if (membersCount <= 0) {
//            throw new EmptyArgumentException("Missing Argument (MEMBERS-COUNT) in the JSON request");
//
//        }
//        if (type <= 0) {
//            throw new EmptyArgumentException("Missing Argument (TYPE) in the JSON request");
//
//        }

        this.titre = titre;
        this.description = description;
        this.addressGame = spotAddress;
        this.teamCount = membersCount;
        this.sppotiType = type;
        this.tags = tags;

        Sport sp = sportRepository.getOne(sportId);
        if (sp != null) {
            this.sportGame = sp;
        } else {
            throw new EntityNotFoundException("Sport ID is not valid");
        }

        for (Long userId : teamPeopleId) {
            try {
                Users u = userRepository.getOne(userId);
                if (u != null) {
                    this.teamGame.add(u);
                } else {
                    throw new EntityNotFoundException("One of the team ID is not valid");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean saveSpoot(Sppoti spotToSave) {

        Set<Users> users = new HashSet<>();

        for (Users user : teamGame) {
            users.add(user);
        }

        spotToSave.setTeamMemnbers(users);

        Sppoti sppoti = sppotiRepository.save(spotToSave);

        return sppoti != null;
    }

    @Override
    public Sport getSportGame() {
        return sportGame;
    }

    public String getAddressGame() {
        return addressGame;
    }

    @Override
    public Set<Users> getTeamGame() {
        return teamGame;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public int getSppotiType() {
        return sppotiType;
    }

    public String getTags() {
        return tags;
    }
}
