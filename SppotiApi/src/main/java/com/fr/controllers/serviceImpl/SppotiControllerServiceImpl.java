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
import com.fr.entities.Address;
import com.fr.entities.Sport;
import com.fr.entities.Sppoti;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
public class SppotiControllerServiceImpl extends AbstractControllerServiceImpl implements SppotiControllerService {

	private Sport sportGame;
	private Address addressGame;
	private Set<Users> teamGame;
	private String titre;
	private String description;
	private int teamCount;
	private int sppotiType;
	private String tags;

	public SppotiControllerServiceImpl() {
		sportGame = new Sport();
		addressGame = new Address();
		teamGame = new HashSet<>();
	}

	@Override
	public void verifyAllDataBeforeSaving(String titre, Long sportId, String description, String date,
			Long[] teamPeopleId, Address spotAddress, int membersCount, int type, String tags) throws Exception {

		if (titre == null || titre.isEmpty() || sportId == null || description == null || description.isEmpty()
				|| date == null || date.isEmpty() || teamPeopleId == null || teamPeopleId.length == 0
				|| spotAddress == null || spotAddress.getNumber() == null || spotAddress.getStreetName() == null
				|| spotAddress.getStreetName().isEmpty() || spotAddress.getTownName() == null
				|| spotAddress.getTownName().isEmpty() || spotAddress.getType() == null
				|| spotAddress.getType().isEmpty() || spotAddress.getZipCode() == null || membersCount <= 0
				|| type <= 0) {

			throw new EmptyArgumentException("Missing Argument in the JSON request");

		}

		this.titre = titre;
		this.description = description;
		this.addressGame = spotAddress;
		this.teamCount = membersCount;
		this.sppotiType = type;
		this.tags = tags;

		Sport sp = sportDaoService.getEntityByID(sportId);
		if (sp != null) {
			this.sportGame = sp;
		} else {
			throw new EntityNotFoundException("Sport ID is not valid");
		}

		for (Long userId : teamPeopleId) {
			Users u = userDaoService.getEntityByID(userId);
			if (u != null) {
				this.teamGame.add(u);
			} else {
				throw new EntityNotFoundException("One of the team ID is not valid");
			}
			Thread.sleep(100);
		}
	}

	@Override
	public Sport getSportGame() {
		return sportGame;
	}

	@Override
	public void setSportGame(Sport sportGame) {
		this.sportGame = sportGame;
	}

	@Override
	public Address getAddressGame() {
		return addressGame;
	}

	@Override
	public void setAddressGame(Address addressGame) {
		this.addressGame = addressGame;
	}

	@Override
	public Set<Users> getTeamGame() {
		return teamGame;
	}

	@Override
	public void setTeamGame(Set<Users> teamGame) {
		this.teamGame = teamGame;
	}

	@Override
	public String getTitre() {
		return titre;
	}

	@Override
	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public int getTeamCount() {
		return teamCount;
	}

	public void setTeamCount(int teamCount) {
		this.teamCount = teamCount;
	}

	public int getSppotiType() {
		return sppotiType;
	}

	public void setSppotiType(int sppotiType) {
		this.sppotiType = sppotiType;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Override
	public boolean saveSpoot(Sppoti spotToSave) {
		boolean userIsUpToDate = true;

		// add the game
		Long gameId = (Long) sppotiDaoService.save(spotToSave);
		if (gameId > 0) {
			// if game has been added -> Link users to create the team

			Sppoti g = sppotiDaoService.getEntityByID(gameId);

			for (Users user : teamGame) {
				user.setGameTeam(g);
				if (!userDaoService.update(user)) {
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

}
