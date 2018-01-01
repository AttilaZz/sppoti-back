package com.fr.commons.dto.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO contains result of the global search.
 *
 * Created by wdjenane on 22/06/2017.
 */
public class GlobalSearchResultDTO
{
	
	private List<TeamDTO> teams = new ArrayList<>();
	private List<SppotiDTO> sppoties = new ArrayList<>();
	private List<UserDTO> users = new ArrayList<>();
	
	public List<TeamDTO> getTeams() {
		return this.teams;
	}
	
	public void setTeams(final List<TeamDTO> teams) {
		this.teams = teams;
	}
	
	public List<SppotiDTO> getSppoties() {
		return this.sppoties;
	}
	
	public void setSppoties(final List<SppotiDTO> sppoties) {
		this.sppoties = sppoties;
	}
	
	public List<UserDTO> getUsers() {
		return this.users;
	}
	
	public void setUsers(final List<UserDTO> users) {
		this.users = users;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public String toString() {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (final JsonProcessingException e) {
			return null;
		}
	}
}
