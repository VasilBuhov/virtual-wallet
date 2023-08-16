package com.company.web.wallet.services;

import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;



    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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
    public void createTransaction(Transaction transaction) {
        transactionRepository.createTransaction(transaction);
    }
    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteTransaction(id);
    }
}
