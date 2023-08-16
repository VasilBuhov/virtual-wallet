package com.company.web.wallet.helpers;

import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    public Transaction fromDto(TransactionDto transactionDto) {

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

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSender(transaction.getSender());
        transactionDto.setRecipient(transaction.getRecipient());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setTimestamp(transaction.getTimestamp());
        transactionDto.setTransactionDescription(transaction.getTransactionDescription());
        return transactionDto;
    }
    public List<TransactionDto> toDtoList(List<Transaction> transactions) {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDto transactionDto = toDto(transaction);
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }
}


