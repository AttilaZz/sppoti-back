package com.fr.api.account;

import com.fr.commons.dto.UserDTO;
import com.fr.service.AccountControllerService;
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
class AccountValidateController {

    private Logger LOGGER = Logger.getLogger(AccountValidateController.class);

    private AccountControllerService accountControllerService;

    @Autowired
    void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    /**
     * Confirm email to get access to sppoti.
     *
     * @param code confirmation code.
     * @return 202 status if account enabled.
     */
    @PutMapping(value = "/validate/{code}")
    ResponseEntity<Void> confirmUserEmail(@PathVariable("code") String code) {

        if (StringUtils.isEmpty(code)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // if given code exist in database confirm registration
        accountControllerService.tryActivateAccount(code);
        LOGGER.info("Account with code (" + code + ") has been confirmed");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    /**
     * Confirm email recover account by editing the password.
     *
     * @param code    confirmation code.
     * @param userDTO user data.
     * @return 202 status if account recovered correctly.
     */
    @PutMapping("/validate/password/{code}")
    ResponseEntity<Void> confirmUserEmail(@RequestBody UserDTO userDTO, @PathVariable("code") String code) {

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(userDTO.getPassword())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // if given code exist in database confirm registration
        accountControllerService.recoverAccount(userDTO, code);
        LOGGER.info("Account with code (" + code + ") has been confirmed");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    /**
     * Send email to recover account.
     *
     * @param userDTO user data.
     * @return 200 if email found and email sent.
     */
    @PutMapping("/recover")
    ResponseEntity<Void> confirmUserEmail(@RequestBody UserDTO userDTO) {

        if (StringUtils.isEmpty(userDTO.getEmail())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // if given code exist in database confirm registration
        accountControllerService.sendRecoverAccountEmail(userDTO);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

}
