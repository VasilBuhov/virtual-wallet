package com.company.web.wallet.services;

import com.company.web.wallet.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionServiceImpl implements TransactionService{

    @Override
    public List<Transaction> get() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Transaction> getByUsername() {
        throw new UnsupportedOperationException();
    }
}
