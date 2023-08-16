package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String DELETE_USER_ERROR_MESSAGE =
            "Only admin and user who own profile can delete it";
    private static final String MODIFY_USER_ERROR_MESSAGE =
            "Only admin  can modify a user";
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public void createUser(User user) {

        User existingUser = userRepository.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }

        existingUser = userRepository.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
        if (user.getUsername() == null) {
            throw new NullPointerException();
        }

        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setPassword(user.getPassword());
        userRepository.createUser(user);
    }

    @Override
    public void updateUser(User authenticatedUser, User user) throws EntityNotFoundException {
        checkModifyPermissionsForUpdating(authenticatedUser, user);
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(User authenticatedUser, int id) throws EntityNotFoundException {
        User user = userRepository.getUserById(id);
        checkModifyPermissionsForDeleting(authenticatedUser, user);
        userRepository.deleteUser(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public void checkModifyPermissionsForUpdating(User authenticatedUser, User user) {
        if (authenticatedUser.getId() == user.getId())
            throw new AuthorizationException(MODIFY_USER_ERROR_MESSAGE);
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
    public void blockOrUnblockUser(int userId, boolean block) throws EntityNotFoundException {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User not found", userId);
        }

        if (block) {
            user.setUserLevel(-1); // Block the user
        } else {
            user.setUserLevel(0); // Unblock the user
        }

        userRepository.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

}
