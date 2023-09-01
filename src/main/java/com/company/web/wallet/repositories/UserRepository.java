package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    List<User> get(String username);

    User getById(int id) throws EntityNotFoundException;

    User getByEmail(String email) throws EntityNotFoundException;

    User getByUsername(String username) throws EntityNotFoundException;

    User getByVerificationCode(String username) throws EntityNotFoundException;

    void create(User user);

    void update(User user);

    void delete(int id) throws EntityNotFoundException;

    User findByEmailOrUsername(String emailOrUsername, String emailOrUsername1);
}
