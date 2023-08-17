package com.company.web.wallet.services;


import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    User getUserById(int id);

    User getUserByEmail(String email);

    void createUser(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    void updateUser(User authenticatedUser, User user) throws EntityNotFoundException;

    void deleteUser(User authenticatedUser, int id) throws EntityNotFoundException;

    default void makeRegularUserAdmin(int id) {
    }

    User getUserByUsername(String username);

    void checkModifyPermissionsForUpdating(User authenticatedUser, User user);

    void checkModifyPermissionsForDeleting(User authenticatedUser, User user);

    void checkModifyPermissionsForUpdating(User authenticatedUser) throws AuthorizationException;

    void blockOrUnblockUser(int userId, boolean block) throws EntityNotFoundException;

    List<User> getAllUsers();
}
