package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.TransactionRepository;
import com.company.web.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final UserRepository userRepository;


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

        // Sort transactions based on sortBy and sortDirection
        if ("timestamp".equals(sortBy)) {
            transactions.sort(Comparator.comparing(Transaction::getTimestamp));
        } else if ("amount".equals(sortBy)) {
            transactions.sort(Comparator.comparing(Transaction::getAmount));
        } // Add more sorting criteria if needed

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
    public List<Transaction> getTransactionsBySender(User authenticatedUser,User sender) {

     checkAccessPermissions(authenticatedUser, sender.getId());
        return transactionRepository.getTransactionsBySender(sender);
    }

    @Override
    public List<Transaction> getTransactionsByRecipient(User authenticatedUser, User recipient) {
        checkAccessPermissions(authenticatedUser,recipient.getId());
        return transactionRepository.getTransactionsByRecipient(recipient);
    }

    @Override
    public List<Transaction> getTransactionsByDirection(TransactionType direction) {
        return transactionRepository.getTransactionsByDirection(direction);
    }
    @Override
    public void createTransaction(Transaction transaction) {
        // Assuming the transaction has sender, recipient, amount, and other relevant fields
        // Validate the sender, recipient, and other data as needed
        System.out.println("tova e "+transaction.getSender().getUsername());
        System.out.println("tova e "+transaction.getRecipient().getUsername());
        // Add amount to recipient's wallet
        walletService.addToBalance(walletService.getWalletIdForUser(transaction.getRecipient()),
                transaction.getRecipient(), transaction.getAmount());
        System.out.println("stiga li do tuk>? ");

        // Remove amount from sender's wallet
        walletService.removeFromBalance(walletService.getWalletIdForUser(transaction.getSender()), transaction.getSender(), transaction.getAmount());

        // Save the transaction
        transactionRepository.createTransaction(transaction);
    }


    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteTransaction(id);
    }

    private void checkAccessPermissions(User authenticatedUser,int userId) {
        if (!(authenticatedUser.getUserLevel() == 1 ||
               authenticatedUser.getId()==userId))
                 {
            throw new AuthorizationException("You do not have permission for this operation");
        }
    }

}
