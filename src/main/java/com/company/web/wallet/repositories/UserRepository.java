package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User getUserById(int id) throws EntityNotFoundException;

    User getUserByEmail(String email) throws EntityNotFoundException;

    User getUserByUsername(String username) throws EntityNotFoundException;

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id) throws EntityNotFoundException;
}
