package com.company.web.wallet.helpers;

import org.springframework.stereotype.Component;

import java.time.YearMonth;
@Component
public class CardExpiration {
    public static boolean isExpired(String date) {
        String mount = date.substring(0, 2);
        String year = date.substring(3);
        int mountInt = Integer.parseInt(mount);
        int yearInt = Integer.parseInt(year) + 2000;
        YearMonth expirationDate = YearMonth.of(yearInt, mountInt);
        return expirationDate.isBefore(YearMonth.now());
    }
}
