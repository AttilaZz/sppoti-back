package com.fr.api.account;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;
import com.fr.service.AccountControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
class AccountGetController {

    private Logger LOGGER = Logger.getLogger(AccountGetController.class);
    private static final String ATT_USER_ID = "USER_ID";


    private AccountControllerService accountControllerService;

    @Autowired
    void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    ResponseEntity<UserDTO> connectedUserInfo(Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
        UserEntity targetUser = accountControllerService.getUserById(accountUserDetails.getId());

        return new ResponseEntity<>(accountControllerService.fillUserResponse(targetUser, null), HttpStatus.OK);

    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/other/{username}/**")
    ResponseEntity<UserDTO> otherUserInfo(@PathVariable("username") String username, HttpServletRequest request) {

//        String path = (String) httpServletRequest.getAttribute(
//                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//        AntPathMatcher apm = new AntPathMatcher();
//        String bestMatchPattern = (String ) httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
//        String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity connected_user = accountControllerService.getUserById(userId);

        UserEntity targetUser = accountControllerService.getUserByUsername(username);

        if (targetUser == null) {
            LOGGER.error("ACCOUNT-OTHER: Username not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(accountControllerService.fillUserResponse(targetUser, connected_user), HttpStatus.OK);

    }
}
