package com.fr.security;

import com.fr.entities.Sport;
import com.fr.entities.Users;
import com.fr.models.SportModel;
import com.fr.models.User;
import com.fr.repositories.UserRepository;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.fr.filter.HeadersAttributes.*;
import static com.fr.filter.HeadersValues.*;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
public class AuthSuccess extends SimpleUrlAuthenticationSuccessHandler {

    UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final String ATT_USER_ID = "USER_ID";

    private static Logger LOGGER = Logger.getLogger(AuthSuccess.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        LOGGER.info("User has been logged :-)");

        request.getSession().setAttribute(ATT_USER_ID, getAuthenticationId());

        //request.getSession().setMaxInactiveInterval(0);
//        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//        if (csrf != null) {
//            Cookie cookie = new Cookie("XSRF-TOKEN", csrf.getToken());
//            cookie.setPath("/");
//            response.addCookie(cookie);
//        }

        response.setHeader(ATTR_ORIGIN.getValue(), Origins.getValue());
        response.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
        response.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
        response.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
        response.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        Users users = userRepository.getById(accountUserDetails.getId());
        String username = users.getUsername();
        int uid = users.getUuid();

        User user = new User();
        user.setUsername(username);
        user.setId(uid);

        Set<Sport> sports = users.getRelatedSports();
        List<SportModel> sportModels = new ArrayList<>();

        for (Sport sport : sports) {
            SportModel sportModel = new SportModel();
            sportModel.setId(sport.getId());
            sportModel.setName(sport.getName());

            sportModels.add(sportModel);
        }

        user.setSportModels(sportModels);

        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(user));

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * @return logged user id
     */
    private Long getAuthenticationId() {
        Long id = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            id = ((MyUserDetails) principal).getId();
        }

        return id;
    }

}
