package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.Transaction;
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
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Transaction";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            return query.getResultList();
        }
    }
    @Override
    public List<Transaction> getTransactionsPage(int offset, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            // Implement pagination using database-specific features
            String hql = "FROM Transaction";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }
//    @Override
//    public List<Transaction> getTransactions(
//            String username,
//            LocalDateTime startDate,
//            LocalDateTime endDate,
//            TransactionType direction,
//            String sortBy,
//            String sortDirection) {
//        try (Session session = sessionFactory.openSession()) {
//            String hql = "SELECT t FROM Transaction t " +
//                    "LEFT JOIN FETCH t.sender sender " +
//                    "LEFT JOIN FETCH t.recipient recipient " +
//                    "WHERE (:username IS NULL OR sender.username = :username OR recipient.username = :username) " +
//                    "AND (:startDate IS NULL OR t.timestamp >= :startDate) " +
//                    "AND (:endDate IS NULL OR t.timestamp <= :endDate) " +
//                    "AND (:direction IS NULL OR t.transactionType = :direction) " +
//                    "ORDER BY " +
//                    "CASE WHEN :sortBy = 'timestamp' THEN t.timestamp END " +
//                    ", CASE WHEN :sortBy = 'sender' THEN sender.username END " +
//                    ", CASE WHEN :sortBy = 'recipient' THEN recipient.username END ";
//
//            if ("desc".equalsIgnoreCase(sortDirection)) {
//                hql += "DESC NULLS LAST";
//            } else {
//                hql += "ASC NULLS LAST";
//            }
//
//            Query<Transaction> query = session.createQuery(hql, Transaction.class);
//            query.setParameter("username", username);
//            query.setParameter("startDate", startDate);
//            query.setParameter("endDate", endDate);
//            query.setParameter("direction", direction);
//
//            return query.getResultList();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return new ArrayList<>();
//        }
//    }

    @Override
    public Transaction getTransactionById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.get(Transaction.class, id);
            if (transaction == null) {
                throw new EntityNotFoundException("Transaction", "id", String.valueOf(id));
            }
            return transaction;
        }
    }

    @Override
    public void createTransaction(Transaction transaction) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(transaction);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Transaction> getTransactionsByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
            Root<Transaction> root = cq.from(Transaction.class);
            cq.select(root).where(cb.or(
                    cb.equal(root.get("sender"), user),
                    cb.equal(root.get("recipient"), user)));
            return session.createQuery(cq).list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Transaction> getTransactionsByType(TransactionType type) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
            Root<Transaction> root = cq.from(Transaction.class);
            cq.select(root).where(cb.equal(root.get("transactionType"), type));
            return session.createQuery(cq).list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Transaction> getTransactionsByUsernameAndDirection(User user, TransactionType direction) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Transaction WHERE (sender = :user OR recipient = :user) AND transactionType = :direction";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("user", user);
            query.setParameter("direction", direction);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate,
                                                        LocalDateTime endDate) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Transaction WHERE timestamp BETWEEN :startDate AND :endDate";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Transaction> getTransactionsBySender(User sender) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Transaction WHERE sender = :sender";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("sender", sender);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }


    @Override
    public List<Transaction> getTransactionsByRecipient(User recipient) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Transaction WHERE recipient = :recipient";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("recipient", recipient);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Transaction> getTransactionsByDirection(TransactionType direction) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Transaction WHERE transactionType = :direction";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("direction", direction);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }


        @Override
    public void deleteTransaction(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.get(Transaction.class, id);
            if (transaction == null) {
                throw new EntityNotFoundException("Transaction", "id", String.valueOf(id));
            }
            session.beginTransaction();
            session.delete(transaction);
            session.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            // Handle exceptions
        }
    }
    @Override
    public List<Transaction> findTransactionsBySenderOrRecipient(User currentUser, int offset, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Transaction t WHERE t.sender = :currentUser OR t.recipient = :currentUser";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("currentUser", currentUser);
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }



}
