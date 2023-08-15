package com.company.web.wallet.services;

import com.company.web.wallet.models.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> get();
    List<Transaction> getByUsername();
}
