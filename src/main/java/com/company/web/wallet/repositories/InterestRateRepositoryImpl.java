package com.company.web.wallet.repositories;

import com.company.web.wallet.models.InterestRate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class InterestRateRepositoryImpl implements InterestRateRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public InterestRateRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Double getLatestInterestRate() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);

            Root<InterestRate> root = criteriaQuery.from(InterestRate.class);
            criteriaQuery.select(root.get("interest"))
                    .orderBy(criteriaBuilder.desc(root.get("timestamp")));

            List<Double> results = session.createQuery(criteriaQuery)
                    .setMaxResults(1)
                    .getResultList();

            return results.isEmpty() ? null : results.get(0);
        }
    }

    @Override
    public void create(InterestRate interestRate) {
        try (Session session = sessionFactory.openSession()) {
            session.save(interestRate);
        }
    }
}
