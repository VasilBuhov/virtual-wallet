package com.company.web.wallet.repositories;

import com.company.web.wallet.models.InterestRate;

public interface InterestRateRepository {
    Double getLatestInterestRate();
    void create(InterestRate interestRate);
}
