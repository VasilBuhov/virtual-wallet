package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.models.InterestRate;
import com.company.web.wallet.models.User;

public interface InterestRateService {
    Double getLatestInterestRate();
    void create(InterestRate interestRate, User user) throws AuthorizationException;
}
