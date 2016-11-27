/**
 * 
 */
package com.fr.controllers.serviceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.fr.controllers.service.IndexService;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component( "indexService" )
public class IndexServiceImpl implements IndexService {

    @Override
    public boolean logOut( HttpServletRequest request, HttpServletResponse response ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println( auth.isAuthenticated() );
        if ( auth.isAuthenticated() ) {
            new SecurityContextLogoutHandler().logout( request, response, auth );
            return true;
        }
        return false;
    }

}
