package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
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
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
    public List<TransactionDto> getAllTransactions(@RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            List<Transaction> transactions = transactionService.getAllTransactions();
            return transactionMapper.toDtoList(transactions);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable Long id, @RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            Transaction transaction = transactionService.getTransactionById(id);
            return transactionMapper.toDto(transaction);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, @RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/create")
    public TransactionDto createTransaction(@RequestHeader HttpHeaders httpHeaders,
                                            @Valid @RequestBody TransactionDto transactionDto) {
        try {
            User currentUser = authenticationHelper.tryGetUser(httpHeaders);
            Transaction transaction = transactionMapper.fromDto(transactionDto);
            transaction.setSender(currentUser);
            transactionService.createTransaction(transaction);
            return transactionMapper.toDto(transaction);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }



}
