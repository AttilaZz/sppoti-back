package com.fr.rest.controllers.account;

import com.fr.commons.dto.UserDTO;
import com.fr.rest.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
public class AccountValidateController {

    private Logger LOGGER = Logger.getLogger(AccountValidateController.class);

    private AccountControllerService accountControllerService;

    @Autowired
    public void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    @PutMapping(value = "/validate/{code}")
    public ResponseEntity<Void> confirmUserEmail(@PathVariable("code") String code) {

        if (StringUtils.isEmpty(code)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // if given code exist in database confirm registration
        if(accountControllerService.tryActivateAccount(code)){
            LOGGER.info("Account with code (" + code + ") has been confirmed");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PutMapping(value = "/recover")
    public ResponseEntity<Void> confirmUserEmail(@RequestBody UserDTO userDTO) {

        if (StringUtils.isEmpty(userDTO.getEmail())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // if given code exist in database confirm registration
        accountControllerService.sendRecoverAccountEmail(userDTO.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
