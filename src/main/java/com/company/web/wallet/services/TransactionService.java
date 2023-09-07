package com.company.web.wallet.services;

import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.DTO.TransactionDto;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> getTransactions(
            String username,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TransactionType direction,
            String sortBy,
            String sortDirection);


    List<Transaction> getTransactions(
            String username,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TransactionType direction,
            String sortBy,
            String sortDirection,
            String filterBy);

    List<Transaction> getAllTransactions(User authenticatedUser, int id);

    Transaction getTransactionById(Long id);

    List<Transaction> getTransactionsByUser(User user);

    List<Transaction> getTransactionsByType(TransactionType type);

    List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);


    List<Transaction> getTransactionsBySender(User authenticatedUser, User sender);



    List<Transaction> getTransactionsByRecipient(User authenticatedUser, User recipient);

    List<Transaction> getTransactionsByDirection(TransactionType direction);

    void createTransaction(Transaction transaction);

    List<Transaction> getTransactionsPage(int offset, int pageSize);

    List<Transaction> getTransactionsPageForCurrentUser(User currentUser, int offset, int pageSize);

    void deleteTransaction(Long id);

}
