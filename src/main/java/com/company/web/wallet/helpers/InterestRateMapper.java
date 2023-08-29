package com.company.web.wallet.helpers;

import com.company.web.wallet.models.InterestRate;
import com.company.web.wallet.models.InterestRateDto;
import com.company.web.wallet.services.InterestRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InterestRateMapper {
    private final InterestRateService interestRateService;

    @Autowired
    public InterestRateMapper(InterestRateService interestRateService) {
        this.interestRateService = interestRateService;
    }

    public InterestRate create(InterestRateDto interestRateDto) {
        InterestRate interestRate = new InterestRate();
        interestRate.setInterest(interestRateDto.getInterest());
        interestRate.setTimestamp(LocalDateTime.now());
        return interestRate;
    }
}
