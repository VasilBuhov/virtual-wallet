package com.company.web.wallet.helpers;

import com.company.web.wallet.controllers.RestController.TransactionRestController;
import com.company.web.wallet.exceptions.AuthorizationException;
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


@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    public static final String AUTHENTIFICATION_FAILURE_MESSAGE = "Wrong username or password.";

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

    //todo implement userService methods
    public User tryGetUser(HttpHeaders headers) {



        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
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


}
