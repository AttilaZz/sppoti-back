/**
 *
 */
package com.fr.controllers.service.implem;

import com.fr.controllers.service.SppotiControllerService;
import com.fr.entities.Sport;
import com.fr.entities.Sppoti;
import com.fr.entities.Users;
import com.fr.exceptions.EmptyArgumentException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
public class SppotiControllerServiceImpl extends AbstractControllerServiceImpl implements SppotiControllerService {

    private Sport sportGame;
    private String addressGame;
    private Set<Users> myTeam;
    private Set<Users> adverseTeam;
    private String titre;
    private String description;
    private int teamCount;
    private String tags;

    public SppotiControllerServiceImpl() {
        sportGame = new Sport();
        myTeam = new HashSet<>();
        adverseTeam = new HashSet<>();
    }

    @Override
    public void verifyAllDataBeforeSaving(String titre, Long sportId, String description, Date date,
                                          Long[] teamPeopleId, Long[] vsTeam, String spotAddress, int membersCount, String tags) throws Exception {

        if (titre == null || titre.isEmpty()) {

            throw new EmptyArgumentException("Missing Argument (TITLE) in the JSON request");

        }
        if (sportId == null) {
            throw new EmptyArgumentException("Missing Argument (SPORT-ID) n the JSON request");

        }
        if (description == null || description.isEmpty()) {
            throw new EmptyArgumentException("Missing Argument (DESCRIPTION) in the JSON request");

        }
        if (date == null) {
            throw new EmptyArgumentException("Missing Argument (DATE) in the JSON request");

        }

        if (teamPeopleId == null || teamPeopleId.length == 0) {
            throw new EmptyArgumentException("Missing or Empty Argument (MY-TEAM) in the JSON request");

        }

        if (vsTeam == null || vsTeam.length == 0) {
            throw new EmptyArgumentException("Missing or Empty Argument (ADVERSE-TEAM) in the JSON request");

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
        this.tags = tags;

        Sport sp = sportRepository.findById(sportId);

        if (sp != null) {
            this.sportGame = sp;
        } else {
            throw new EntityNotFoundException("Sport ID is not valid");
        }

        //my team
        for (Long userId : teamPeopleId) {
            try {
                Users u = userRepository.getOne(userId);
                if (u != null) {
                    this.myTeam.add(u);
                } else {
                    throw new EntityNotFoundException("One of the team ID is not valid");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //adverse team
        for (Long userId : vsTeam) {
            try {
                Users u = userRepository.getOne(userId);
                if (u != null) {
                    this.adverseTeam.add(u);
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

        Set sppoti = new HashSet();
        sppoti.add(spotToSave);

        //my team
        for (Users user : myTeam) {
            user.setTeamSppoties(sppoti);
            users.add(user);
        }
        spotToSave.setMyteam(users);

        //adverse team
        users.clear();
        for (Users user : adverseTeam) {
            user.setAdverseSppoties(sppoti);
            users.add(user);
        }
        spotToSave.setAdverseTeam(users);


        return sppotiRepository.save(spotToSave) != null;

    }

    @Override
    public Sport getSportGame() {
        return sportGame;
    }

    public String getAddressGame() {
        return addressGame;
    }

    public Set<Users> getMyTeam() {
        return myTeam;
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

    public String getTags() {
        return tags;
    }
}
