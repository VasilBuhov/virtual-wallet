package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.Card;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class WalletRepositoryImpl implements WalletRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Wallet get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Wallet wallet = session.get(Wallet.class, id);
            if (wallet == null) {
                throw new EntityNotFoundException("Wallet", id);
            }
            if (wallet.getStatusDeleted() == 1) {
                throw new EntityDeletedException("Wallet", "ID", String.valueOf(id));
            }
            return wallet;
        }
    }
@Override
    public Integer getWalletIdForUser(User user) {
    try (Session session = sessionFactory.openSession()) {
        String hql = "SELECT w.id FROM Wallet w WHERE w.owner = :user";
        Query<Integer> query = session.createQuery(hql, Integer.class);
        query.setParameter("user", user);

        List<Integer> result = query.getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }
}

    @Override
    public Wallet get(String owner) {
        try (Session session = sessionFactory.openSession()) {
            Wallet wallet = session.get(Wallet.class, owner);
            if (wallet == null) {
                throw new EntityNotFoundException("Wallet", "owner", owner);
            }
            if (wallet.getStatusDeleted() == 1) {
                throw new EntityDeletedException("Wallet", "owner", owner);
            }
            return wallet;
        }
    }

    @Override
    public List<Wallet> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Wallet ", Wallet.class)
                    .list()
                    .stream()
                    .filter(card -> card.getStatusDeleted() == 0)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void create(Wallet wallet) {
        try (Session session = sessionFactory.openSession()) {
            session.save(wallet);
        }
    }

    @Override
    public void update(Wallet wallet) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(wallet);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Wallet walletToBeDeleted = get(id);
        walletToBeDeleted.setStatusDeleted(1);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(walletToBeDeleted);
            session.getTransaction().commit();
        }
    }
}
