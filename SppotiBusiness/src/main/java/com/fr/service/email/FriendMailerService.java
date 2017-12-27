package com.fr.service.email;

import com.fr.entities.UserEntity;

/**
 * Created by djenanewail on 12/27/17.
 */
public interface FriendMailerService
{
	void onSendFriendRequest(UserEntity from, UserEntity to);
	
	void onAcceptFriendRequest(UserEntity from, UserEntity to);
	
	void onRefuseFriendRequest(UserEntity from, UserEntity to);
}
