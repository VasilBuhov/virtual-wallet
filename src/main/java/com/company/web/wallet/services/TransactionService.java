package com.company.web.wallet.services;

import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.User;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(Long id);

    List<Transaction> getTransactionsByUser(User user);

    List<Transaction> getTransactionsByType(TransactionType type);

    void createTransaction(Transaction transaction);

    void deleteTransaction(Long id);
}
