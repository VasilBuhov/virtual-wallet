package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.TopUpHelper;
import com.company.web.wallet.helpers.WalletMapper;
import com.company.web.wallet.helpers.WithdrawMapper;
import com.company.web.wallet.models.DTO.CardTopUpDto;
import com.company.web.wallet.models.DTO.WithdrawMoneyDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.models.DTO.WalletDtoOut;

import com.company.web.wallet.services.UserService;
import com.company.web.wallet.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletRestController {
    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;
    private final WalletMapper walletMapper;
    private final TopUpHelper topUpHelper;
    private final UserService userService;
    private final WithdrawMapper withdrawMapper;
    private final Logger logger = LoggerFactory.getLogger(WalletRestController.class);

    @Autowired
    public WalletRestController(WalletService walletService, AuthenticationHelper authenticationHelper, WalletMapper walletMapper, TopUpHelper topUpHelper, UserService userService, WithdrawMapper withdrawMapper) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
        this.walletMapper = walletMapper;
        this.topUpHelper = topUpHelper;
        this.userService = userService;
        this.withdrawMapper = withdrawMapper;
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
            userService.addWallet(wallet, user);
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
    public WalletDtoOut updateOverdraft(@PathVariable int id, @RequestHeader HttpHeaders httpheaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpheaders);
            walletService.updateOverdraft(id, user);
            Wallet updatedWallet = walletService.get(id, user);
            return walletMapper.walletDtoOut(updatedWallet);
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

    @PutMapping("/{id}/top-up")
    public WalletDtoOut topUp(@PathVariable int id, @RequestHeader HttpHeaders httpheaders, @Valid @RequestBody CardTopUpDto cardTopUpDto) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpheaders);
            try {
                 topUpHelper.tryTopUp();
            } catch (HttpClientErrorException e) {
                throw new ResponseStatusException(e.getStatusCode(), "Unable to deposit money at the moment. Please try again later.");
            }

            walletService.addToBalance(id, user, cardTopUpDto.getAmount());
            Wallet affectedWallet = walletService.get(id, user);
            return walletMapper.walletDtoOut(affectedWallet);

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
    public WalletDtoOut withdraw(@PathVariable int id, @RequestHeader HttpHeaders httpheaders, @Valid @RequestBody WithdrawMoneyDto withdrawMoneyDto) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpheaders);
            BigDecimal amount = withdrawMapper.getAmount(withdrawMoneyDto);
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
