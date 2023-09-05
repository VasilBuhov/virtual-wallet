package com.company.web.wallet.services;


import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.ContactForm;
import com.company.web.wallet.models.DTO.UserPasswordDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    User getUserById(int id);
    User getByUsername(String username);
    User getUserByEmail(String email);
    List<User> getAll();

    User getByPhone(String phone);

    User getByIdUnverified(int id);

    User getByUsernameUnverified(String username);

    List<User> getAdmins();

    List<User> getBlocked();

    Page<User> findByUsernameContaining(String username, Pageable pageable);

    Page<User> getAllUsersPage(Pageable pageable);

    Page<User> getAllUnverifiedUsersPage(Pageable pageable);

    Page<User> getAllBlockedUsersPage(Pageable pageable);

    Page<User> getAllAdminUsersPage(Pageable pageable);

    Page<User> getAllDeletedUsersPage(Pageable pageable);

    void create(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;
    void update(User authenticatedUser, User user) throws EntityNotFoundException;
    void delete(User authenticatedUser, int id) throws EntityNotFoundException;

    void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    void sendContactEmail(ContactForm contactForm) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);

    void deleteUser(User authenticatedUser, int id) throws EntityNotFoundException;

    void addWallet(Wallet wallet, User user);
    void addCard(Card card, User user);


    default void makeRegularUserAdmin(int id) {}
    void checkModifyPermissionsForUpdating(User authenticatedUser, User user);
    void checkModifyPermissionsForDeleting(User authenticatedUser, User user);
    void checkModifyPermissionsForUpdating(User authenticatedUser) throws AuthorizationException;
    void blockOrUnblock(int userId, boolean block) throws EntityNotFoundException;

    void changePassword(User user, UserPasswordDto userPasswordDto);
}
