package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {
    private final TransactionService transactionService;
    private final AuthenticationHelper authenticationHelper;
    @Autowired
    public TransactionRestController(TransactionService transactionService,
                                     AuthenticationHelper authenticationHelper) {
        this.transactionService = transactionService;
        this.authenticationHelper = authenticationHelper;
    }
}
