package com.company.web.wallet.helpers;

import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction fromDto(TransactionDto transactionDto) {
        if (transactionDto == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setSender(transactionDto.getSender());
        transaction.setRecipient(transactionDto.getRecipient());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setTimestamp(transactionDto.getTimestamp());
        transaction.setTransactionDescription(transactionDto.getTransactionDescription());
        return transaction;
    }
    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSender(transaction.getSender());
        transactionDto.setRecipient(transaction.getRecipient());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setTimestamp(transaction.getTimestamp());
        transactionDto.setTransactionDescription(transaction.getTransactionDescription());
        return transactionDto;
    }
}

