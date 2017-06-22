package com.fr.commons.dto.search;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;

import java.util.List;

/**
 * DTO contains result of the global search.
 *
 * Created by wdjenane on 22/06/2017.
 */
public class GlobalSearchResultDTO {

    private List<TeamDTO> teams;
    private List<SppotiDTO> sppoties;
    private List<UserDTO> users;

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public List<SppotiDTO> getSppoties() {
        return sppoties;
    }

    public void setSppoties(List<SppotiDTO> sppoties) {
        this.sppoties = sppoties;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
