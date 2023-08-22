package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.WalletMapper;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.models.WalletDtoIn;
import com.company.web.wallet.models.WalletDtoOut;

import com.company.web.wallet.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
//TODO Need to implement catching for Authorization exceptions after Nikolai fixes the Authentication helper.
@RestController
@RequestMapping("/api/wallets")
public class WalletRestController {
    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;
    private final WalletMapper walletMapper;
    private final Logger logger = LoggerFactory.getLogger(WalletRestController.class);

    @Autowired
    public WalletRestController(WalletService walletService, AuthenticationHelper authenticationHelper, WalletMapper walletMapper) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
        this.walletMapper = walletMapper;
    }

    @GetMapping
    public List<WalletDtoOut> get(@RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            List<Wallet> resultList = walletService.getAll(user);
            return walletMapper.walletDtoOutList(resultList);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public WalletDtoOut get(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            Wallet searchedWallet = walletService.get(id, user);
            return walletMapper.walletDtoOut(searchedWallet);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException | EntityDeletedException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public WalletDtoOut create(@RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            Wallet wallet = walletMapper.createWalletDto(user);
            walletService.create(wallet);
            return walletMapper.walletDtoOut(wallet);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/overdraft")
    public WalletDtoOut updateOverdraft(@PathVariable int id, @RequestHeader HttpHeaders httpheaders, @Valid @RequestBody WalletDtoIn walletDtoIn) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpheaders);
            Wallet wallet = walletMapper.updateOverdraftDto(id, walletDtoIn, user);
            walletService.updateOverdraft(id, user, wallet);
            return walletMapper.walletDtoOut(wallet);
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

    @PutMapping("/{id}/deposit")
    public WalletDtoOut updateDeposit(@PathVariable int id, @RequestHeader HttpHeaders httpheaders, @Valid @RequestBody BigDecimal amount) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpheaders);
            walletService.addToBalance(id, user, amount);
            return walletMapper.walletDtoOut(walletService.get(id, user));
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
    @PutMapping("/{id}/withdraw")
    public WalletDtoOut updateWithdraw(@PathVariable int id, @RequestHeader HttpHeaders httpheaders, @Valid @RequestBody BigDecimal amount) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpheaders);
            walletService.removeFromBalance(id, user, amount);
            return walletMapper.walletDtoOut(walletService.get(id, user));
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
    public void delete(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            walletService.delete(id, user);
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
