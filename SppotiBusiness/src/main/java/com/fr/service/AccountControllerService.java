package com.fr.service;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface AccountControllerService extends AbstractControllerService {

    /**
     * save new user.
     *
     * @param user {@link UserDTO} to save.
     */
    void saveNewUser(SignUpDTO user);

    /**
     * Activate sppoti account.
     *
     * @param code activation code.
     * @return activation state.
     */
    void tryActivateAccount(String code);

    /**
     * UPDATE ACCOUNT DATA.
     *
     * @param userDTO user data to update.
     */
    void updateUser(UserDTO userDTO);

    /**
     * Unselect avatar or cover.
     *
     * @param userId user id.
     * @param i      resoirce index.
     */
    void unSelectOldResource(Long userId, int i);

    /**
     * find user by username.
     *
     * @param username username.
     * @return found user.
     */
    UserEntity getUserByUsername(String username);

    /**
     * @param targetUser     target user account.
     * @param connected_user connected user id.
     * @return UserDTO
     */
    UserDTO fillUserResponse(UserEntity targetUser, UserEntity connected_user);

    /**
     * Update user cover  and avatar.
     *
     * @param user cover/avatar to update.
     */
    void updateAvatarAndCover(UserDTO user);

    /**
     * send recover account email.
     *
     * @param user user data.
     */
    void sendRecoverAccountEmail(SignUpDTO user);

    /**
     * Update password if confirmation code found.
     *  @param userDTO user data.
     * @param code    account recover code.
     */
    void recoverAccount(SignUpDTO userDTO, String code);

    /**
     *Generate new account confirmation email.
     *
     * @param userDTO user data.
     */
    void generateNewConfirmationEmail(UserDTO userDTO);
}