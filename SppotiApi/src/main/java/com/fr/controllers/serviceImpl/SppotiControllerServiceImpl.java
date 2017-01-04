/**
 *
 */
package com.fr.controllers.serviceImpl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

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

        if (titre == null || titre.isEmpty() || sportId == null || description == null || description.isEmpty()
                || date == null || date.isEmpty() || teamPeopleId == null || teamPeopleId.length == 0
                || spotAddress == null || membersCount <= 0
                || type <= 0) {

            throw new EmptyArgumentException("Missing Argument in the JSON request");

        }

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
        boolean userIsUpToDate = true;

        // add the game
        Sppoti sppoti = sppotiRepository.save(spotToSave);

        Long gameId = sppoti.getId();
        if (gameId > 0) {
            // if game has been added -> Link users to create the team

            Sppoti g = sppotiRepository.getOne(gameId);

            for (Users user : teamGame) {
                user.setGameTeam(g);

                try {
                    userRepository.save(user);
                } catch (Exception e) {
                    userIsUpToDate = false;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (userIsUpToDate)
                return true;

            // TODO: If one of the users failed to update -- retry

        }

        return false;
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
