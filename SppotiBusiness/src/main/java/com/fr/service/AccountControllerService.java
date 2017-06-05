package com.fr.service;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.TypeAccountValidation;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface AccountControllerService extends AbstractControllerService
{
	
	/**
	 * save new user.
	 *
	 * @param user
	 * 		{@link UserDTO} to save.
	 */
	void saveNewUser(SignUpDTO user);
	
	/**
	 * Activate sppoti account.
	 *
	 * @param code
	 * 		activation code.
	 *
	 * @return activation state.
	 */
	void tryActivateAccount(String code, TypeAccountValidation type);
	
	/**
	 * UPDATE ACCOUNT DATA.
	 *
	 * @param userDTO
	 * 		user data to update.
	 */
	void updateUser(UserDTO userDTO);
	
	/**
	 * Unselect avatar or cover.
	 *
	 * @param userId
	 * 		user id.
	 * @param i
	 * 		resoirce index.
	 */
	void unSelectOldResource(Long userId, int i);
	
	/**
	 * @param targetUser
	 * 		target user account.
	 *
	 * @return UserDTO
	 */
	UserDTO fillAccountResponse(UserEntity targetUser);
	
	/**
	 * Update user cover  and avatar.
	 *
	 * @param user
	 * 		cover/avatar to update.
	 */
	void updateAvatarAndCover(UserDTO user);
	
	/**
	 * send recover account email.
	 *
	 * @param user
	 * 		user data.
	 */
	void sendRecoverAccountEmail(SignUpDTO user);
	
	/**
	 * Update password if confirmation code found.
	 *
	 * @param userDTO
	 * 		user data.
	 * @param code
	 * 		account recover code.
	 */
	void recoverAccount(UserDTO userDTO, String code);
	
	/**
	 * Generate new account confirmation email.
	 *
	 * @param userDTO
	 * 		user data.
	 */
	void generateNewConfirmationEmail(UserDTO userDTO);
	
	/**
	 * @param username
	 * 		target user username.
	 * @param connectedUserId
	 * 		connected user id.
	 *
	 * @return user DTO.
	 */
	UserDTO handleFriendShip(String username, Long connectedUserId);
	
	/**
	 * Deactivate account.
	 *
	 * @param userId
	 * 		user id.
	 */
	void deactivateAccount(int userId);
}