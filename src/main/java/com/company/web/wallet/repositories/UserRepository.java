package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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

    List<User> getAllContacts(int id);

    byte[] getIdCard(int id);

    byte[] getSelfie(int id);

    void uploadIdCardAndSelfie(int userId, MultipartFile idCardFile, MultipartFile selfieFile);

    void create(User user);

    void addContact(int contactOwner, int contactTarget);

    void update(User user);

    void removeContact(int contactOwner, int contactTarget);

    void addAsAdmin(User user);

    void removeAsAdmin(User user);

    void delete(int id) throws EntityNotFoundException;

    User findByEmailOrUsername(String emailOrUsername, String emailOrUsername1);

    void save2FA(int userId, int code);

    // get2FA
    String get2FA(int userId);
}
