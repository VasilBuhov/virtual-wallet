package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.SavingsWallet;
import com.company.web.wallet.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SavingsWalletRepositoryImpl implements SavingsWalletRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public SavingsWalletRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public SavingsWallet get(int id) {
        try (Session session = sessionFactory.openSession()) {
            SavingsWallet savingsWallet = session.get(SavingsWallet.class, id);
            if (savingsWallet == null) {
                throw new EntityNotFoundException("Savings Wallet", id);
            }
            return savingsWallet;
        }
    }

    @Override
    public List<SavingsWallet> getAllForUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<SavingsWallet> query = session.createQuery("from SavingsWallet where owner.id = :id", SavingsWallet.class);
            query.setParameter("id", user.getId());
            return query.list();
        }
    }

    @Override
    public List<SavingsWallet> getAllWallets() {
        try (Session session = sessionFactory.openSession()) {
            Query<SavingsWallet> query = session.createQuery("from SavingsWallet", SavingsWallet.class);
            return query.list();
        }
    }

    @Override
    public void create(SavingsWallet savingsWallet) {
        try (Session session = sessionFactory.openSession()) {
            session.save(savingsWallet);
        }
    }

    @Override
    public void update(SavingsWallet savingsWallet) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(savingsWallet);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        SavingsWallet savingsWalletToBeDeleted = get(id);
        savingsWalletToBeDeleted.setIsDeleted(1);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(savingsWalletToBeDeleted);
            session.getTransaction().commit();
        }
    }
}
