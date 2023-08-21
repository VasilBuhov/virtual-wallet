package com.company.web.wallet.helpers;

import com.company.web.wallet.models.*;
import com.company.web.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    private final UserMapper userMapper;
    private final UserRecipientMapper userRecipientMapper;
    private final UserSenderMapper userSenderMapper;
    private final WalletRepository walletRepository;

    @Autowired
    public TransactionMapper(UserMapper userMapper, UserRecipientMapper userRecipientMapper, UserSenderMapper userSenderMapper, WalletRepository walletRepository) {
        this.userMapper = userMapper;
        this.userRecipientMapper = userRecipientMapper;
        this.userSenderMapper = userSenderMapper;
        this.walletRepository = walletRepository;
    }

    public Transaction fromDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setSender(userSenderMapper.toEntity(transactionDto.getSender()));
        transaction.setRecipient(userRecipientMapper.findUserByEmailOrUsername(transactionDto.getRecipient().getRecipientIdentifier()));
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setTimestamp(transactionDto.getTimestamp());
        transaction.setTransactionDescription(transactionDto.getTransactionDescription());
        transaction.setStatus(transactionDto.getStatus());

        transaction.setWallet(transactionDto.getWallet());
    return transaction;
    }

    public TransactionDto toDto(Transaction transaction) {

        TransactionDto transactionDto = new TransactionDto();
        UserSenderDto senderDto = userSenderMapper.toDto(transaction.getSender());
        transactionDto.setSender(senderDto);
        UserRecipientDto recipientDto = new UserRecipientDto();
        recipientDto.setRecipientIdentifier(transaction.getRecipient().getUsername()); // Display only the username
        transactionDto.setRecipient(recipientDto);
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setTimestamp(transaction.getTimestamp());
        transactionDto.setTransactionDescription(transaction.getTransactionDescription());
        transactionDto.setStatus(transaction.getStatus());
        transactionDto.getWallet();
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
