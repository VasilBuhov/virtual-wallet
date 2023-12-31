package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.NotEnoughMoneyInWalletException;
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
import java.util.stream.Collectors;

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
    public List<Transaction> getTransactions(String username, LocalDateTime startDate, LocalDateTime endDate, TransactionType direction, String sortBy, String sortDirection) {
        return null;
    }

    @Override
    public List<Transaction> getTransactions(
            String username,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TransactionType direction,
            String sortBy,
            String sortDirection,
            String filterBy) {
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

        // Sorting based on sortBy and sortDirection
        if ("timestamp".equals(sortBy)) {
            transactions.sort(Comparator.comparing(Transaction::getTimestamp));
        } else if ("amount".equals(sortBy)) {
            transactions.sort(Comparator.comparing(Transaction::getAmount));
        }

        // Reverse the list if sortDirection is "desc"
        if ("desc".equals(sortDirection)) {
            Collections.reverse(transactions);
        }

//        // Apply additional filtering based on filterBy (filter by date)
        if ("date".equals(filterBy)) {
            transactions = transactions.stream()
                    .filter(transaction -> {
                        LocalDateTime transactionTimestamp = transaction.getTimestamp();
                        // Modify the date range as needed
                        return transactionTimestamp.isAfter(startDate) && transactionTimestamp.isBefore(endDate);
                    })
                    .collect(Collectors.toList());
        }
        if ("received".equals(filterBy)) {
            transactions = transactions.stream()
                    .filter(transaction -> {
                        // Modify the username comparison as needed
                        return transaction.getRecipient().getUsername().equals(username);
                    })
                    .collect(Collectors.toList());
        }
        if ("sent".equals(filterBy)) {
            transactions = transactions.stream()
                    .filter(transaction -> {
                        // Modify the username comparison as needed
                        return transaction.getSender().getUsername().equals(username);
                    })
                    .collect(Collectors.toList());
        }

        return transactions;
    }



    @Override
    public List<Transaction> getAllTransactions(User authenticatedUser, int id) {
//        checkAccessPermissions(authenticatedUser, id);
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
            int walletIdForSender = transaction.getWallet().getId();
            walletService.addToBalance(walletService.getWalletIdForUser(transaction.getRecipient()),
                    transaction.getRecipient(), transaction.getAmount());
            walletService.removeFromBalance(walletIdForSender,
                    transaction.getSender(),
                    transaction.getAmount());
            transactionRepository.createTransaction(transaction);
        } catch (RuntimeException e) {
            throw new NotEnoughMoneyInWalletException("You don't have enough money in wallet to perform this tranaction");
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
