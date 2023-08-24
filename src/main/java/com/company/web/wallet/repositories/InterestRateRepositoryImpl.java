package com.company.web.wallet.repositories;

import com.company.web.wallet.models.InterestRate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterestRateRepositoryImpl implements InterestRateRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public InterestRateRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(InterestRate interestRate) {
        try (Session session = sessionFactory.openSession()) {
            session.save(interestRate);
        }
    }
}
