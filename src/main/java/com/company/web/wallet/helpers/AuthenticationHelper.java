package com.company.web.wallet.helpers;

import com.company.web.wallet.exceptions.AuthenticationFailureException;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.Objects;


@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong username or password.";

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationHelper.class);

    @Autowired
    public AuthenticationHelper(UserService userService) {

        this.userService = userService;
    }

    public User checkForRegisteredUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This operation requires Authentication.");
        }
        try {
            return userService.getByUsername(headers.getFirst(AUTHORIZATION_HEADER_NAME));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "User is not registered in the system.");
        }
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(Objects.requireNonNull(userInfo));
            String password = getPassword(userInfo);

            User user = userService.getByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;

        }
         catch (EntityNotFoundException e) {
             logger.error(e.getMessage());
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpace + 1);
    }

    public void verifyAuthentication(String username, String password, int userLevel){
        try {
            User user = userService.getByUsername(username);

            if(!user.getPassword().equals(password))
            {
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }
            if (userLevel == -1) {
                throw new BlockedUserException("Blocked user can not perform any action");
            }
        } catch (EntityNotFoundException e){
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
        }
    }

    public void verifyAuthentication(String username, String password){
        try {
            User user = userService.getByUsername(username);

            if(!user.getPassword().equals(password))
            {
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }
        } catch (EntityNotFoundException e){
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
        }

    }

    public boolean isAdmin(HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            User user = userService.getByUsername(username);
            return user != null && user.getUserLevel() == 1;
        }
        return false;
    }
    public User tryGetUser(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");

        if (currentUser == null) {
            throw new AuthenticationFailureException("No user logged in.");
        }

        return userService.getByUsername(currentUser);
    }

}
