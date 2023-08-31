package com.company.web.wallet.helpers;

import com.company.web.wallet.models.DTO.WithdrawMoneyDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawMapper {
    public BigDecimal getAmount(WithdrawMoneyDto withdrawMoneyDto) {
        return withdrawMoneyDto.getAmount();
    }
}
