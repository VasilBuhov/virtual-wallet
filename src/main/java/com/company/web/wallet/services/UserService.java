package com.company.web.wallet.services;


import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;

public interface UserService {
    User getUserById(int id);

    User getUserByEmail(String email);

    void createUser(User user);

    void updateUser(User authenticatedUser, User user) throws EntityNotFoundException;

    void deleteUser(User authenticatedUser, int id) throws EntityNotFoundException;

    default void makeRegularUserAdmin(int id) {
    }

    User getUserByUsername(String username);

    void checkModifyPermissionsForUpdating(User authenticatedUser, User user);

    void checkModifyPermissionsForDeleting(User authenticatedUser, User user);

    void checkModifyPermissionsForUpdating(User authenticatedUser) throws AuthorizationException;
}
