package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.ContactForm;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String DELETE_USER_ERROR_MESSAGE = "Only admin and user who own profile can delete it";
    private static final String MODIFY_USER_ERROR_MESSAGE = "Only admin  can modify a user";
    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public Page<User> findByUsernameContaining(String username, Pageable pageable) {
        return userRepository.findByUsernameContaining(username, pageable);
    }

    @Override
    public Page<User> getAllUsersPage(Pageable pageable) {
        return userRepository.findAllUsers(pageable);
    }


    @Override
    public void create(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
//        User existingUserByEmail = userRepository.getByEmail(user.getEmail());
//        if (existingUserByEmail != null) {
//            throw new EntityDuplicateException("User", "email", user.getEmail());
//        }
//
//        User existingUserByUsername = userRepository.getByUsername(user.getUsername());
//        if (existingUserByUsername != null) {
//            throw new EntityDuplicateException("User", "username", user.getUsername());
//        }

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new NullPointerException("Username and password cannot be null.");
        }

        user.setVerificationCode(RandomString.make(64));
        user.setEnabled(false);
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setPassword(user.getPassword());
        userRepository.create(user);
        sendVerificationEmail(user, siteURL);
    }


    @Override
    public void update(User authenticatedUser, User user) throws EntityNotFoundException {
        checkModifyPermissionsForUpdating(authenticatedUser, user);
        userRepository.update(user);
    }
    @Override
    public void deleteUser(User authenticatedUser, int id) throws EntityNotFoundException {
        User user = userRepository.getById(id);
        checkModifyPermissionsForDeleting(authenticatedUser, user);
        userRepository.delete(id);
    }

    @Override
    public void addWallet(Wallet wallet, User user) {
//        user.getWallets().add(wallet);
        userRepository.update(user);
    }

    @Override
    public void addCard(Card card, User user) {
//        user.getCards().add(card);
        userRepository.update(user);
    }

    @Override
    public void delete(User authenticatedUser, int id) throws EntityNotFoundException {
        User user = userRepository.getById(id);
        checkModifyPermissionsForDeleting(authenticatedUser, user);
        userRepository.delete(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void checkModifyPermissionsForUpdating(User authenticatedUser, User user) {
        if (authenticatedUser.getId() != user.getId()) throw new AuthorizationException(MODIFY_USER_ERROR_MESSAGE);
    }

    @Override
    public void checkModifyPermissionsForDeleting(User authenticatedUser, User user) {
        if (authenticatedUser.getId() != user.getId()) {
            throw new AuthorizationException(DELETE_USER_ERROR_MESSAGE);
        }
    }

    @Override
    public void checkModifyPermissionsForUpdating(User authenticatedUser) throws AuthorizationException {
        throw new AuthorizationException("Only admin can block a user.");
    }

    @Override
    public void blockOrUnblock(int userId, boolean block) throws EntityNotFoundException {
        User user = userRepository.getById(userId);
        if (user == null) throw new EntityNotFoundException("User not found", userId);
        if (block) user.setUserLevel(-1);
        else user.setUserLevel(0);
        userRepository.update(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "The Wallet Project Team";

        String mailContent = "<p>Dear " + user.getUsername() + ",</p>";
        mailContent += "<p><br>Please click on the link below to verify to your registration:<br></p>";

        mailContent += "<p> Or you can manually verify your email with the code provided</p>";
        mailContent += "<br><p>" + user.getVerificationCode() +"</p><br>";

        mailContent += "<p><br>Thank you,<br>The Wallet Project<br><br><br></p>";
        String verifyURL = siteURL + "/users/verify?code=" + user.getVerificationCode();
        sendMessage(user, subject, senderName, mailContent, verifyURL);
    }

    private void sendMessage(User user, String subject,
                             String senderName,
                             String mailContent,
                             String verifyURL) throws MessagingException, UnsupportedEncodingException {
        mailContent += "<h3><a href=\"" + verifyURL + "\">VERIFY</a></h3>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("wallet.project.a48@badmin.org", senderName);
        helper.setSubject(subject);
        helper.setTo(user.getEmail());
        helper.setText(mailContent, true);
        mailSender.send(message);
    }

    @Override
    public void sendContactEmail(ContactForm contactForm) throws MessagingException, UnsupportedEncodingException {
        String subject = contactForm.getSubject();
        String senderName = contactForm.getFirstName() + ' ' + contactForm.getLastName();
        String recipientEmail = "wallet.project.a48@badmin.org";
        String mailContent = contactForm.getMessage();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("wallet.project.a48@badmin.org", senderName);
        helper.setSubject(subject);
        helper.setTo(recipientEmail);
        helper.setText(mailContent, false);
        mailSender.send(message);
    }


    @Override
    public boolean verify(String verificationCode) {
        User user = userRepository.getByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.update(user);
            return true;
        }
    }

}
