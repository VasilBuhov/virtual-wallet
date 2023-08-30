package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.SavingsWalletMapper;
import com.company.web.wallet.models.SavingsWallet;
import com.company.web.wallet.models.DTO.SavingsWalletDtoIn;
import com.company.web.wallet.models.DTO.SavingsWalletDtoOut;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.SavingsWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/savings")
public class SavingsWalletRestController {
    private final SavingsWalletService savingsWalletService;
    private final AuthenticationHelper authenticationHelper;
    private final SavingsWalletMapper savingsWalletMapper;
    private final Logger logger = LoggerFactory.getLogger(WalletRestController.class);

    @Autowired
    public SavingsWalletRestController(SavingsWalletService savingsWalletService, AuthenticationHelper authenticationHelper, SavingsWalletMapper savingsWalletMapper) {
        this.savingsWalletService = savingsWalletService;
        this.authenticationHelper = authenticationHelper;
        this.savingsWalletMapper = savingsWalletMapper;
    }

    @GetMapping("/{id}")
    public SavingsWalletDtoOut get(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            SavingsWallet searchedSavingsWallet = savingsWalletService.get(id, user);
            return savingsWalletMapper.returnSavingsWalletDto(searchedSavingsWallet);
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

    @GetMapping
    public List<SavingsWalletDtoOut> get(@RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            List<SavingsWallet> resultList = savingsWalletService.getAllForUser(user);
            return savingsWalletMapper.savingsWalletDtoOutList(resultList);
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
    public SavingsWalletDtoOut create(@RequestHeader HttpHeaders httpHeaders, @Valid @RequestBody SavingsWalletDtoIn savingsWalletDtoIn) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            SavingsWallet savingsWallet = savingsWalletMapper.createSavingsWalletFromDto(savingsWalletDtoIn, user);
            savingsWalletService.create(user, savingsWallet);
            return savingsWalletMapper.returnSavingsWalletDto(savingsWallet);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id, @RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            savingsWalletService.delete(id, user);
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
