package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Contact;
import com.company.web.wallet.models.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        }
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class);
        List<User> users = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Long total = countQuery.getSingleResult();
        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Page<User> findAllUnverifiedUsers(Pageable pageable) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.verified = 0", User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.verified = 0", Long.class);
        List<User> users = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Long total = countQuery.getSingleResult();
        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Page<User> findAllBlockedUsers(Pageable pageable) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.userLevel = -1", User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.userLevel = -1", Long.class);
        List<User> users = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Long total = countQuery.getSingleResult();
        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Page<User> findAllAdminUsers(Pageable pageable) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.userLevel = 1", User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.userLevel = 1", Long.class);
        List<User> users = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Long total = countQuery.getSingleResult();
        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Page<User> findAllDeletedUsers(Pageable pageable) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.statusDeleted = true", User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.statusDeleted = true", Long.class);
        List<User> users = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Long total = countQuery.getSingleResult();
        return new PageImpl<>(users, pageable, total);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> findByUsernameContaining(String username, Pageable pageable) {
        String queryString = "SELECT u FROM User u WHERE u.username LIKE :username";
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        query.setParameter("username", "%" + username + "%");

        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username LIKE :username",
                Long.class
        );
        countQuery.setParameter("username", "%" + username + "%");

        List<User> users = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        Long total = countQuery.getSingleResult();

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public User getById(int id) throws EntityNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.get("id"), id));
            User user = session.createQuery(cq).uniqueResultOptional().orElse(null);
            if (user == null) {
                throw new EntityNotFoundException("User", "id", String.valueOf(id));
            }
            return user;
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new UnknownError("Something went wrong");
        }
    }

    @Override
    public User getByEmail(String email) throws EntityNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.get("email"), email));
            User user = session.createQuery(cq).uniqueResultOptional().orElse(null);
            if (user == null) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong(HERE)");
        }
    }

    @Override
    public User getByUsername(String username) throws EntityNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.get("username"), username));
            User user = session.createQuery(cq).uniqueResultOptional().orElse(null);
            if (user == null) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong");
        }
    }

    @Override
    public User getByPhone(String phone) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where phone = :phone and enabled = true", User.class);
            query.setParameter("phone", phone);
            List<User> result = query.list();
            if (!result.isEmpty()) return result.get(0);
        }
        throw new EntityNotFoundException("User", "phone number", phone);
    }

    @Override
    public List<User> getAdmins() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User u WHERE u.userLevel = 1", User.class);
            List<User> result = query.list();
            if (result.isEmpty())
                throw new EntityNotFoundException("User", "level", "admin");
            else return result;
        }
    }

    @Override
    public List<User> getBlocked() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User u WHERE u.userLevel = 0", User.class);
            List<User> result = query.list();
            if (result.isEmpty())
                throw new EntityNotFoundException("User", "level", "blocked");
            else return result;
        }
    }

    @Override
    public User getByIdUnverified(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where id = :id ", User.class);
            query.setParameter("id", id);
            User user = query.uniqueResult();
            if (user == null || user.getVerified() != 0) {
                throw new EntityNotFoundException("User", "id", String.valueOf(id));
            } else return user;
        }
    }

    @Override
    public User getByUsernameUnverified(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username ", User.class);
            query.setParameter("username", username);
            User user = query.uniqueResult();

            if (user == null || user.getVerified() != 0) {
                throw new EntityNotFoundException("User", "username", username);
            } else return user;
        }
    }

    @Override
    public User getByVerificationCode(String verificationCode) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where verificationCode = :verificationCode", User.class);
            query.setParameter("verificationCode", verificationCode);
            List<User> result = query.list();
            if (result.isEmpty()) throw new EntityNotFoundException("User", "verificationCode", verificationCode);
            else return result.get(0);
        }
    }

    @Override
    public List<User> getAllContacts(int id) {
            try (Session session = sessionFactory.openSession()) {
                Query<User> query = session.createQuery("SELECT u FROM User u\n" +
                        "JOIN Contact c ON c.contactTarget = u.id\n" +
                        "WHERE c.contactOwner = :id", User.class);
                query.setParameter("id", id);
                return query.list();
            }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            user.setCreateDate(LocalDateTime.now());
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong(REPO create)");
        }
    }

    @Override
    public void addContact(int contactOwner, int contactTarget) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Contact newContact = new Contact();
            newContact.setContactOwner(contactOwner);
            newContact.setContactTarget(contactTarget);
            session.save(newContact);
            transaction.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong(REPO add contact)");
        }
    }

    @Override
    public void removeContact(int contactOwner, int contactTarget) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<?> deleteQuery = session.createQuery("DELETE FROM Contact WHERE contactOwner = :ownerId AND contactTarget = :targetId");
            deleteQuery.setParameter("ownerId", contactOwner);
            deleteQuery.setParameter("targetId", contactTarget);
            deleteQuery.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong(REPO delete contact)");
        }
    }

    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            user.setLastUpdateDate(LocalDateTime.now());
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong(REPO update)");
        }
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User not found", id);
            }
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong(REPO delete)");
        }
    }

    @Override
    public User findByEmailOrUsername(String emailOrUsername, String emailOrUsername1) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT u FROM User u WHERE u.email = :emailOrUsername OR u.username = :emailOrUsername1";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("emailOrUsername", emailOrUsername);
            query.setParameter("emailOrUsername1", emailOrUsername1);
            return query.uniqueResult();
        }
    }
}
