package com.fr.rest.controllers.account;

import com.fr.rest.service.AccountControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
public class AccountValidateController {

    private AccountControllerService accountControllerService;

    @Autowired
    public void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    @PutMapping(value = "/validate/{code}")
    public ResponseEntity<Void> confirmUserEmail(@PathVariable("code") String code) {

        if (code == null || code.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if given code exist in database confirm registration
        if (accountControllerService.tryActivateAccount(code)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // code is not valid
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
