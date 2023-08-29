package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.models.InterestRate;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.InterestRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestRateServiceImpl implements InterestRateService {
    private final InterestRateRepository interestRateRepository;
    public static final String AUTHORIZATION_ERROR = "Unauthorized access";

    @Autowired
    public InterestRateServiceImpl(InterestRateRepository interestRateRepository) {
        this.interestRateRepository = interestRateRepository;
    }

    @Override
    public Double getLatestInterestRate() {
        return interestRateRepository.getLatestInterestRate();
    }

    @Override
    public void create(InterestRate interestRate, User user) {
        if (user.getUserLevel() != 1) {
            throw new AuthorizationException(AUTHORIZATION_ERROR);
        }
        interestRateRepository.create(interestRate);
    }
}
