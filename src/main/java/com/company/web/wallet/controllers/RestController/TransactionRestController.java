package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.TransactionMapper;
import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.models.DTO.TransactionDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.UserRepository;
import com.company.web.wallet.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(TransactionRestController.class);

    @Autowired
    public TransactionRestController(TransactionService transactionService,
                                     TransactionMapper transactionMapper,
                                     AuthenticationHelper authenticationHelper, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.authenticationHelper = authenticationHelper;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<TransactionDto> getAllTransactions(@RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            List<Transaction> transactions = transactionService.getAllTransactions();
            return transactionMapper.toDtoList(transactions);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable Long id, @RequestHeader HttpHeaders httpHeaders) {
        try {
            User user= authenticationHelper.tryGetUser(httpHeaders);
            Transaction transaction = transactionService.getTransactionById(id);
            return transactionMapper.toDtoAll(transaction);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping()
    public TransactionDto createTransaction(@RequestHeader HttpHeaders httpHeaders,
                                            @Valid @RequestBody TransactionDto transactionDto) {
        try {
            User currentUser = authenticationHelper.tryGetUser(httpHeaders);
            Transaction transaction = transactionMapper.fromDto(transactionDto);
            transaction.setSender(currentUser);
            transactionService.createTransaction(transaction);
            return transactionMapper.toDto(transaction,currentUser);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @GetMapping("/user/{username}")
    public List<TransactionDto> getTransactionsByUser(@PathVariable String username, @RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            User user = userRepository.getByUsername(username);
            List<Transaction> transactions = transactionService.getTransactionsByUser(user);
            return transactionMapper.toDtoList(transactions,user);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/sender/{username}")
    public List<TransactionDto> getTransactionsBySender(@PathVariable String username, @RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            User sender = userRepository.getByUsername(username);
            List<Transaction> transactions = transactionService.getTransactionsBySender(sender);
            return transactionMapper.toDtoList(transactions,sender);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/recipient/{username}")
    public List<TransactionDto> getTransactionsByRecipient(@PathVariable String username, @RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            User recipient = userRepository.getByUsername(username);
            List<Transaction> transactions = transactionService.getTransactionsByRecipient(recipient);
            return transactionMapper.toDtoList(transactions,recipient);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @GetMapping("/direction/{direction}")
    public List<TransactionDto> getTransactionsByDirection(@PathVariable TransactionType direction, @RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            List<Transaction> transactions = transactionService.getTransactionsByDirection(direction);
            return transactionMapper.toDtoList(transactions);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }



}
