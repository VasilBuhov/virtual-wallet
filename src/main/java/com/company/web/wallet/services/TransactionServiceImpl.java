package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.TransactionRepository;
import com.company.web.wallet.repositories.TransactionRepositoryImpl;
import com.company.web.wallet.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(TransactionRepositoryImpl.class);


    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, WalletService walletService, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
        this.userRepository = userRepository;
    }



    @Override
    public List<Transaction> getTransactions(
            String username,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TransactionType direction,
            String sortBy,
            String sortDirection) {
        List<Transaction> transactions;
        if (username != null) {
            User user = userRepository.getByUsername(username);
            if (direction != null) {
                transactions = transactionRepository.getTransactionsByUsernameAndDirection(user, direction);
            } else {
                transactions = transactionRepository.getTransactionsByUser(user);
            }
        } else if (startDate != null && endDate != null) {
            transactions = transactionRepository.getTransactionsByDateRange(startDate, endDate);
        } else if (direction != null) {
            transactions = transactionRepository.getTransactionsByDirection(direction);
        } else {
            transactions = transactionRepository.getAllTransactions();
        }
        if ("timestamp".equals(sortBy)) {
            transactions.sort(Comparator.comparing(Transaction::getTimestamp));
        } else if ("amount".equals(sortBy)) {
            transactions.sort(Comparator.comparing(Transaction::getAmount));
        }
        if ("desc".equals(sortDirection)) {
            Collections.reverse(transactions);
        }
        return transactions;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.getTransactionById(id);
    }

    @Override
    public List<Transaction> getTransactionsByUser(User user) {
        return transactionRepository.getTransactionsByUser(user);
    }

    @Override
    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactionRepository.getTransactionsByType(type);
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate,
                                                        LocalDateTime endDate) {
        return transactionRepository.getTransactionsByDateRange(startDate, endDate);
    }

    @Override
    public List<Transaction> getTransactionsBySender(User authenticatedUser, User sender) {

        checkAccessPermissions(authenticatedUser, sender.getId());
        return transactionRepository.getTransactionsBySender(sender);
    }

    @Override
    public List<Transaction> getTransactionsByRecipient(User authenticatedUser, User recipient) {
        checkAccessPermissions(authenticatedUser, recipient.getId());
        return transactionRepository.getTransactionsByRecipient(recipient);
    }

    @Override
    public List<Transaction> getTransactionsByDirection(TransactionType direction) {
        return transactionRepository.getTransactionsByDirection(direction);
    }

    @Override
    public void createTransaction(Transaction transaction) {
        try {
            if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("ZeroAmountTransactionException: Transaction amount must be greater than zero.");
                return;
            }
            User sender = userRepository.getByUsername(transaction.getSender().getUsername());
            User recipient = userRepository.getByUsername(transaction.getRecipient().getUsername());
            transaction.setSender(sender);
            transaction.setRecipient(recipient);
            walletService.addToBalance(walletService.getWalletIdForUser(transaction.getRecipient()),
                    transaction.getRecipient(), transaction.getAmount());
            walletService.removeFromBalance(walletService.getWalletIdForUser(userRepository.getByUsername(transaction.getSender().getUsername())),
                    transaction.getSender(),
                    transaction.getAmount());
            transactionRepository.createTransaction(transaction);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    @Override
    public List<Transaction> getTransactionsPage(int offset, int pageSize) {
        return transactionRepository.getTransactionsPage(offset, pageSize);
    }
    @Override
    public List<Transaction> getTransactionsPageForCurrentUser(User currentUser, int offset, int pageSize) {
        // Retrieve transactions where the current user is either the sender or recipient
        return transactionRepository.findTransactionsBySenderOrRecipient(currentUser, offset, pageSize);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteTransaction(id);
    }



    private void checkAccessPermissions(User authenticatedUser, int userId) {
        if (!(authenticatedUser.getUserLevel() == 1 ||
                authenticatedUser.getId() == userId)) {
            throw new AuthorizationException("You do not have permission for this operation");
        }
    }

}
