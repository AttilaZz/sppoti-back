package com.fr.rest.controllers.account;

import com.fr.commons.dto.UserDTO;
import com.fr.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
public class AccountUpdateController {

    private Logger LOGGER = Logger.getLogger(AccountUpdateController.class);

    private AccountControllerService accountControllerService;

    @Autowired
    public void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    @PutMapping
    public ResponseEntity<UserDTO> editUserInfo(@RequestBody UserDTO user) {

        boolean update = false;

        if ((user.getAvatar() != null && !user.getAvatar().isEmpty()) || (user.getCover() != null && !user.getCover().isEmpty() && user.getCoverType() != 0)) {

            //resources
            accountControllerService.updateAvatarAndCover(user);

        } else {
            //first name
            if (!StringUtils.isEmpty(user.getFirstName())) {
                update = true;
            }
            //last name
            if (!StringUtils.isEmpty(user.getLastName())) {
                update = true;
            }
            //address
            if (!StringUtils.isEmpty(user.getAddress())) {
                update = true;
            }
            //username
            if (!StringUtils.isEmpty(user.getUsername())) {
                update = true;
            }
            //phone
            if (!StringUtils.isEmpty(user.getPhone())) {
                update = true;
            }
            //password
            if (!StringUtils.isEmpty(user.getPassword())) {
                update = true;
            }
            //email
            if (!StringUtils.isEmpty(user.getEmail())) {
                update = true;
            }
            //TODO: Update sports
        }

        if (update) {
            accountControllerService.updateUser(user);
            LOGGER.info("USER-UPDATE: UserDTO has been updated!");
            return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
        } else {
            LOGGER.error("USER-UPDATE: Nothing to update OR missing parameter");
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }

    }

}