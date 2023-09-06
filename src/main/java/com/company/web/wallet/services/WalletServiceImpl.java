package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.exceptions.OperationNotSupportedException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.repositories.UserRepository;
import com.company.web.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {
    private static final String MODIFY_WALLET_ERROR_MESSAGE = "Only the wallet owner can modify the wallet information.";
    public static final String AUTHORIZATION_ERROR = "Unauthorized access";
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final InterestRateService interestRateService;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, InterestRateService interestRateService, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.interestRateService = interestRateService;
        this.userRepository = userRepository;
    }


    @Override
    public Wallet get(int id, User user) {
        checkModifyPermissions(id, user);
        Wallet wallet = walletRepository.get(id);
        if (wallet.getStatusDeleted() == 1) {
            throw new EntityDeletedException("Wallet", "ID", String.valueOf(id));
        }
        return wallet;
    }

    @Override
    public Wallet getByNumber(int number, User user) {
        return null;
    }

    @Override
    public int getWalletIdForUser(User user) {
        return walletRepository.getWalletIdForUser(user);
    }

    @Override
    public List<Wallet> getAll(User user) {
        List<Wallet> result = walletRepository.getAllForUser(user);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("Wallets", "owner", user.getUsername());
        }
        return result;
    }

    @Override
    public void create(Wallet wallet) {
        double latestInterestRate = interestRateService.getLatestInterestRate();
        wallet.setInterestRate(latestInterestRate);
        walletRepository.create(wallet);
    }

    @Override
    public void updateOverdraft(int id, User user, Wallet wallet) {
        checkModifyPermissions(id, user);
        walletRepository.update(wallet);
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void chargeInterestOnOverdraft() {
        List<Wallet> allWallets = walletRepository.getAll();
        List<Wallet> walletsWithOverdraft = allWallets.stream()
                .filter(wallet -> wallet.getOverdraftEnabled() == 1)
                .collect(Collectors.toList());
        walletsWithOverdraft.forEach(wallet -> {
            BigDecimal interestAmount = wallet.getBalance().multiply(BigDecimal.valueOf(wallet.getInterestRate()));
            wallet.setBalance(wallet.getBalance().subtract(interestAmount));
            walletRepository.update(wallet);
        });
    }


    @Override
    public void addToBalance(int id, User user, BigDecimal amount) {
        checkModifyPermissions(id, user);
        Wallet wallet = walletRepository.get(id);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.update(wallet);
    }

    public void removeFromBalance(int id, User user, BigDecimal amount) {
        checkModifyPermissions(id, user);
        Wallet wallet = walletRepository.get(id);
        if (wallet.isOverdraftEnabled() == 0 && (wallet.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0)) {
            throw new OperationNotSupportedException();
        } else {
            wallet.setBalance(wallet.getBalance().subtract(amount));
            walletRepository.update(wallet);
        }
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        user.getWallets().remove(walletRepository.get(id));
        userRepository.update(user);
        walletRepository.delete(id);
    }

    private void checkModifyPermissions(int walletId, User user) {
        Wallet wallet = walletRepository.get(walletId);
        if (!wallet.getOwner().equals(user)) {
            throw new AuthorizationException(MODIFY_WALLET_ERROR_MESSAGE);
        }
    }
}
