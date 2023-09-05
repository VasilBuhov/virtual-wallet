package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    Page<User> findAllUsers(Pageable pageable);

    Page<User> findAllUnverifiedUsers(Pageable pageable);

    Page<User> findAllBlockedUsers(Pageable pageable);

    Page<User> findAllAdminUsers(Pageable pageable);

    Page<User> findAllDeletedUsers(Pageable pageable);

    Page<User> findByUsernameContaining(String username, Pageable pageable);

    User getById(int id) throws EntityNotFoundException;

    User getByEmail(String email) throws EntityNotFoundException;

    User getByUsername(String username) throws EntityNotFoundException;

    User getByPhone(String phone);

    List<User> getAdmins();

    List<User> getBlocked();

    User getByIdUnverified(int id);

    User getByUsernameUnverified(String username);

    User getByVerificationCode(String username) throws EntityNotFoundException;

    void create(User user);

    void update(User user);

    void delete(int id) throws EntityNotFoundException;

    User findByEmailOrUsername(String emailOrUsername, String emailOrUsername1);
}
