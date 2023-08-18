package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String DELETE_USER_ERROR_MESSAGE = "Only admin and user who own profile can delete it";
    private static final String MODIFY_USER_ERROR_MESSAGE = "Only admin  can modify a user";
    private final UserRepository userRepository;

    private JavaMailSender mailSender;

//    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void createUser(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {

        User existingUser = userRepository.getByEmail(user.getEmail());
        if (existingUser != null) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }

        existingUser = userRepository.getByUsername(user.getUsername());
        if (existingUser != null) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
        if (user.getUsername() == null) {
            throw new NullPointerException();
        }

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
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
        if (authenticatedUser.getId() == user.getId()) throw new AuthorizationException(MODIFY_USER_ERROR_MESSAGE);
    }

    @Override
    public void checkModifyPermissionsForDeleting(User authenticatedUser, User user) {
        if (authenticatedUser.getId() == user.getId()) {
            throw new AuthorizationException(DELETE_USER_ERROR_MESSAGE);
        }
    }

    @Override
    public void checkModifyPermissionsForUpdating(User authenticatedUser) throws AuthorizationException {
        throw new AuthorizationException("Only admin or blocked user can modify a user.");
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
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
    public User findUserByEmailOrUsername(String emailOrUsername) {
        return userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername);
    }

    private void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "Your email address";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>" + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
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
            userRepository.create(user);
            return true;
        }

    }

}
