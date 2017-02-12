package com.fr.rest.controllers.account;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.Address;
import com.fr.entities.Resources;
import com.fr.entities.UserEntity;
import com.fr.enums.CoverType;
import com.fr.rest.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
public class AccountUpdateController {

    private Logger LOGGER = Logger.getLogger(AccountUpdateController.class);
    private static final String ATT_USER_ID = "USER_ID";

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private AccountControllerService accountControllerService;

    @Autowired
    public void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<UserDTO> editUserInfo(@RequestBody UserDTO user, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity connected_user = accountControllerService.getUserById(userId);

        //detect which element uwer want to update
        Resources resource = new Resources();

        boolean update = false;

        if ((user.getAvatar() != null && !user.getAvatar().isEmpty()) || (user.getCover() != null && !user.getCover().isEmpty() && user.getCoverType() != 0)) {

            resource.setSelected(true);

            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                resource.setUrl(user.getAvatar());
                resource.setType(1);
                resource.setTypeExtension(1);
                accountControllerService.unSelectOldResource(userId, 1);
                update = true;
            } else if (user.getCover() != null && !user.getCover().isEmpty() && (CoverType.IMAGE.type() == user.getCoverType() || CoverType.VIDEO.type() == user.getCoverType())) {
                resource.setUrl(user.getCover());
                resource.setType(2);
                resource.setTypeExtension(user.getCoverType());
                accountControllerService.unSelectOldResource(userId, 2);
                update = true;
            }

            resource.setUser(connected_user);
            connected_user.getRessources().add(resource);

        } else {
            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                connected_user.setFirstName(user.getFirstName());
                update = true;
            }
            if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                connected_user.setLastName(user.getLastName());
                update = true;
            }
            if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                connected_user.getAddresses().add(new Address(user.getAddress()));
                update = true;
            }
            if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                connected_user.setUsername(user.getUsername());
                update = true;
            }
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                connected_user.setTelephone(user.getPhone());
                update = true;
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                connected_user.setPassword(user.getPassword());
                update = true;
            }
            //TODO: Update sports
        }

        if (update) {

            try {
                accountControllerService.updateUser(connected_user);
                LOGGER.info("USER-UPDATE: UserDTO has been updated!");
                return new ResponseEntity<>(user, HttpStatus.OK);

            } catch (RuntimeException e) {
                LOGGER.error("USER-UPDATE: ERROR updating user: " + e);
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);

            }

        } else {
            LOGGER.error("USER-UPDATE: Nothing to update OR missigin parameter");
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }

    }

}
