package com.company.web.wallet.repositories;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.User;
import org.hibernate.SessionFactory;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(Long id) throws EntityNotFoundException;

    void createTransaction(Transaction transaction);

    List<Transaction> getTransactionsByUser(User user);

    List<Transaction> getTransactionsByType(TransactionType type);

    void deleteTransaction(Long id) throws EntityNotFoundException;
}
