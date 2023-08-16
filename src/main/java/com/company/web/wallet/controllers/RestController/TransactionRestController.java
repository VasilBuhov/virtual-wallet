package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.helpers.AuthenticationHelper;

import com.company.web.wallet.helpers.TransactionMapper;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.TransactionDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public TransactionRestController(TransactionService transactionService,
                                     TransactionMapper transactionMapper,
                                     AuthenticationHelper authenticationHelper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.authenticationHelper = authenticationHelper;

    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {

        List<Transaction> transactions = transactionService.getAllTransactions();
        List<TransactionDto> transactionDtos = transactionMapper.toDtoList(transactions);
        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {

        Transaction transaction = transactionService.getTransactionById(id);
        TransactionDto transactionDto = transactionMapper.toDto(transaction);
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(HttpHeaders headers,
                                                            @RequestBody TransactionDto transactionDto) {
        User currentUser = authenticationHelper.tryGetUser(headers);
        Transaction transaction = transactionMapper.fromDto(transactionDto);
        transaction.setSender(currentUser);
        transactionService.createTransaction(transaction);
        TransactionDto createdTransactionDto = transactionMapper.toDto(transaction);
        return new ResponseEntity<>(createdTransactionDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {

        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
