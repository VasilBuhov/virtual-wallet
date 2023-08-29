package com.company.web.wallet.services;


import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    User getUserById(int id);

    User getUserByEmail(String email);

    void createUser(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    void update(User authenticatedUser, User user) throws EntityNotFoundException;
    void addWallet(Wallet wallet, User user);
    void addCard(Card card, User user);

    void delete(User authenticatedUser, int id) throws EntityNotFoundException;

    default void makeRegularUserAdmin(int id) {
    }

    User getByUsername(String username);

    void checkModifyPermissionsForUpdating(User authenticatedUser, User user);

    void checkModifyPermissionsForDeleting(User authenticatedUser, User user);

    void checkModifyPermissionsForUpdating(User authenticatedUser) throws AuthorizationException;

    void blockOrUnblock(int userId, boolean block) throws EntityNotFoundException;

    List<User> getAll(User user);

    boolean verify(String verificationCode);
}
