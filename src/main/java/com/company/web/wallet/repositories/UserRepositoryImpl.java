package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
    public List<User> get(String username) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = "from User where username like :username";
            Query<User> query = session.createQuery(queryString, User.class);
            query.setParameter("username", "%" + username + "%"); // Add '%' before and after for partial matching
            return query.list();
        }
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
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong");
        }
    }

    @Override
    public User getByVerificationCode(String username) throws EntityNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.get("verification_code"), username));
            User user = session.createQuery(cq).uniqueResultOptional().orElse(null);
            if (user == null) {
                throw new EntityNotFoundException("User", "verification code", username);
            }
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong");
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UnknownError("Something went wrong(REPO create)");
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
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
