package com.company.web.wallet.helpers;

import com.company.web.wallet.models.*;
import com.company.web.wallet.models.DTO.TransactionDto;
import com.company.web.wallet.models.DTO.UserRecipientDto;
import com.company.web.wallet.models.DTO.UserSenderDto;
import com.company.web.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TransactionMapper {

    private final UserMapper userMapper;
    private final UserRecipientMapper userRecipientMapper;
    private final UserSenderMapper userSenderMapper;
    private final WalletRepository walletService;

    public TransactionMapper(UserMapper userMapper, UserRecipientMapper userRecipientMapper, UserSenderMapper userSenderMapper, WalletRepository walletService) {
        this.userMapper = userMapper;
        this.userRecipientMapper = userRecipientMapper;
        this.userSenderMapper = userSenderMapper;
        this.walletService = walletService;
    }

    public Transaction fromDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setSender(userSenderMapper.toEntity(transactionDto.getSender()));
        transaction.setRecipient(userRecipientMapper.findUserByEmailOrUsername(transactionDto.getRecipient().getRecipientIdentifier()));
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDescription(transactionDto.getTransactionDescription());
        transaction.setStatus(transactionDto.getStatus());
        transaction.setWallet(walletService.get(transactionDto.getWalletId()));
        return transaction;
    }

    public TransactionDto toDto(Transaction transaction, User currentUser) {
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

        if (currentUser != null && currentUser.getUsername().equals(transaction.getRecipient().getUsername())) {
            transactionDto.setDirection("incoming");
        } else {
            transactionDto.setDirection("outgoing");
        }
        Wallet wallet = walletService.get(transaction.getWallet().getId());
        transactionDto.setWallet(wallet);
        return transactionDto;
    }

    public List<TransactionDto> toDtoList(List<Transaction> transactions, User currentUser) {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDto transactionDto = toDto(transaction, currentUser); // Pass the current user
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }

    public List<TransactionDto> toDtoList(List<Transaction> transactions) {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDto transactionDto = toDtoAll(transaction);
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }


    public TransactionDto toDtoAll(Transaction transaction) {

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
        transactionDto.setWallet(transaction.getWallet());
        transactionDto.setWalletId(transaction.getWallet().getId());
        return transactionDto;
    }
    public Set<TransactionDto> toDtoSet(List<Transaction> transactions, User currentUser) {
        Set<TransactionDto> transactionDtos = new HashSet<>();
        for (Transaction transaction : transactions) {
            TransactionDto transactionDto = toDto(transaction, currentUser); // Pass the current user
            transactionDtos.add(transactionDto);
        }
        return transactionDtos;
    }
}