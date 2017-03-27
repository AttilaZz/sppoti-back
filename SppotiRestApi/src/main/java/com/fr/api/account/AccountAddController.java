package com.fr.api.account;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.exception.ConflictEmailException;
import com.fr.commons.exception.ConflictPhoneException;
import com.fr.commons.exception.ConflictUsernameException;
import com.fr.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

    /**
     *
     * @param user user to add.
     * @return http status 201 if created, ...
     */
    @PostMapping(value = "/create")
    @ResponseBody
    public ResponseEntity createUser(@RequestBody @Valid SignUpRequestDTO user) {

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