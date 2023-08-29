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
    public Transaction getTransactionById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.get(Transaction.class, id);
            if (transaction == null) {
                throw new EntityNotFoundException("Transaction", "id", String.valueOf(id));
            }
            return transaction;
        }
//        catch (Exception e) {
//            throw new EntityNotFoundException("Transaction", "id", String.valueOf(id));
//        }
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
                    cb.equal(root.get("recipient"), user)
            ));
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

}
