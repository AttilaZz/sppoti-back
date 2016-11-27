/**
 * 
 */
package com.fr.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.pojos.FriendShip;

import java.util.List;

/**
 * Created by: Wail DJENANE on Oct 17, 2016
 */
@JsonInclude(value = Include.NON_DEFAULT)
public class HeaderData {

	private String firstName;
	private String lastName;

	private Long avatarId;
	private String avatar;

	private Long coverId;
	private String cover;
	private int coverType;

	private String username;
	private int nbNotif;

	private int pendingFriendRequestCount;
	private List<FriendShip> pendingFriendRequest;

	private int confirmedFriendRequestCount;
	private List<FriendShip> confirmedFriendRequest;

	private List<Notification> notifList;//post-comment like + content share + tag
	private int notifListCount;

	public HeaderData() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Long getCoverId() {
		return coverId;
	}

	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getNbNotif() {
		return nbNotif;
	}

	public void setNbNotif(int nbNotif) {
		this.nbNotif = nbNotif;
	}

	public int getCoverType() {
		return coverType;
	}

	public void setCoverType(int coverType) {
		this.coverType = coverType;
	}

	public int getPendingFriendRequestCount() {
		return pendingFriendRequestCount;
	}

	public void setPendingFriendRequestCount(int pendingFriendRequestCount) {
		this.pendingFriendRequestCount = pendingFriendRequestCount;
	}

	public List<FriendShip> getPendingFriendRequest() {
		return pendingFriendRequest;
	}

	public void setPendingFriendRequest(List<FriendShip> pendingFriendRequest) {
		this.pendingFriendRequest = pendingFriendRequest;
	}

	public int getConfirmedFriendRequestCount() {
		return confirmedFriendRequestCount;
	}

	public void setConfirmedFriendRequestCount(int confirmedFriendRequestCount) {
		this.confirmedFriendRequestCount = confirmedFriendRequestCount;
	}

	public List<FriendShip> getConfirmedFriendRequest() {
		return confirmedFriendRequest;
	}

	public void setConfirmedFriendRequest(List<FriendShip> confirmedFriendRequest) {
		this.confirmedFriendRequest = confirmedFriendRequest;
	}

	public List<Notification> getNotifList() {
		return notifList;
	}

	public void setNotifList(List<Notification> notifList) {
		this.notifList = notifList;
	}

	public int getNotifListcount() {
		return notifListCount;
	}

	public void setNotifListcount(int notifListcount) {
		this.notifListCount = notifListcount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + ((avatarId == null) ? 0 : avatarId.hashCode());
		result = prime * result + ((confirmedFriendRequest == null) ? 0 : confirmedFriendRequest.hashCode());
		result = prime * result + confirmedFriendRequestCount;
		result = prime * result + ((cover == null) ? 0 : cover.hashCode());
		result = prime * result + ((coverId == null) ? 0 : coverId.hashCode());
		result = prime * result + coverType;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + nbNotif;
		result = prime * result + ((notifList == null) ? 0 : notifList.hashCode());
		result = prime * result + notifListCount;
		result = prime * result + ((pendingFriendRequest == null) ? 0 : pendingFriendRequest.hashCode());
		result = prime * result + pendingFriendRequestCount;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeaderData other = (HeaderData) obj;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (avatarId == null) {
			if (other.avatarId != null)
				return false;
		} else if (!avatarId.equals(other.avatarId))
			return false;
		if (confirmedFriendRequest == null) {
			if (other.confirmedFriendRequest != null)
				return false;
		} else if (!confirmedFriendRequest.equals(other.confirmedFriendRequest))
			return false;
		if (confirmedFriendRequestCount != other.confirmedFriendRequestCount)
			return false;
		if (cover == null) {
			if (other.cover != null)
				return false;
		} else if (!cover.equals(other.cover))
			return false;
		if (coverId == null) {
			if (other.coverId != null)
				return false;
		} else if (!coverId.equals(other.coverId))
			return false;
		if (coverType != other.coverType)
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (nbNotif != other.nbNotif)
			return false;
		if (notifList == null) {
			if (other.notifList != null)
				return false;
		} else if (!notifList.equals(other.notifList))
			return false;
		if (notifListCount != other.notifListCount)
			return false;
		if (pendingFriendRequest == null) {
			if (other.pendingFriendRequest != null)
				return false;
		} else if (!pendingFriendRequest.equals(other.pendingFriendRequest))
			return false;
		if (pendingFriendRequestCount != other.pendingFriendRequestCount)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
