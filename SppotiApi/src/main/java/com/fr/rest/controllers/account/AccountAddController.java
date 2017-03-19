package com.fr.rest.controllers.account;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.entities.RoleEntity;
import com.fr.entities.SportEntity;
import com.fr.entities.UserEntity;
import com.fr.exceptions.ConflictEmailException;
import com.fr.exceptions.ConflictPhoneException;
import com.fr.exceptions.ConflictUsernameException;
import com.fr.models.UserRoleType;
import com.fr.rest.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@RestController
@RequestMapping(value = "/account")
public class AccountAddController {

    private Logger LOGGER = Logger.getLogger(AccountAddController.class);

    private AccountControllerService accountControllerService;

    @Autowired
    public void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public ResponseEntity createUser(@RequestBody @Valid SignUpRequestDTO user) {

		/*
         * saving the new user
		 */
        try {
            accountControllerService.saveNewUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (ConflictEmailException e) {
            LOGGER.error("Error creating user : " + user.getFirstName() + " " + user.getLastName() + " Email already exist!");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ConflictPhoneException e) {
            LOGGER.error("Error creating user : " + user.getFirstName() + " " + user.getLastName() + " Phone already exist!");
            return ResponseEntity.status(410).build();
        } catch (ConflictUsernameException e) {
            LOGGER.error("Error creating user : " + user.getFirstName() + " " + user.getLastName() + " Username already exist!");
            return ResponseEntity.status(411).build();
        }

    }

}