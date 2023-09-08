package com.company.web.wallet.helpers;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TopUpHelper {
    public void tryTopUp() {
        RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(
                    "http://wallet.badmin.org:5000/api/cards",
                    HttpMethod.POST,
                    null,
                    Void.class);
    }
}
