package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.SavingsWallet;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.repositories.SavingsWalletRepository;
import com.company.web.wallet.repositories.UserRepository;
import com.company.web.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingsWalletServiceImpl implements SavingsWalletService {
    private static final String MODIFY_WALLET_ERROR_MESSAGE = "Only the wallet owner can modify the wallet information.";
    private final SavingsWalletRepository savingsWalletRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final InterestRateService interestRateService;

    @Autowired
    public SavingsWalletServiceImpl(SavingsWalletRepository savingsWalletRepository, WalletRepository walletRepository, UserRepository userRepository, InterestRateService interestRateService) {
        this.savingsWalletRepository = savingsWalletRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.interestRateService = interestRateService;
    }

    @Override
    public SavingsWallet get(int id, User user) {
        checkModifyPermissions(id, user);
        SavingsWallet savingsWallet = savingsWalletRepository.get(id);
        if (savingsWallet.getIsDeleted() == 1) {
            throw new EntityDeletedException("Savings wallet", "ID", String.valueOf(id));
        }
        return savingsWallet;
    }

    @Override
    public List<SavingsWallet> getAllForUser(User user) {
        List<SavingsWallet> result = savingsWalletRepository.getAllForUser(user);
        List<SavingsWallet> matchingWallets = result.stream()
                .filter(savingsWallet -> savingsWallet.getOwner().equals(user) && savingsWallet.getIsDeleted() == 0)
                .collect(Collectors.toList());
        if (matchingWallets.isEmpty()) {
            throw new EntityNotFoundException("Savings Wallets", "owner", user.getUsername());
        }
        return matchingWallets;
    }

    @Override
    @Scheduled(cron = "0 0 0 1 * ?")
    public void addInterestOnSavings() {
        List<SavingsWallet> savingsWallets = savingsWalletRepository.getAllWallets();
        if (!savingsWallets.isEmpty()) {
            for (SavingsWallet savingsWallet : savingsWallets) {
                if (savingsWallet.getEndDate().isBefore(LocalDateTime.now()) && savingsWallet.getIsDeleted() == 0) {
                    Wallet walletToAddInterestReward = savingsWallet.getFromWallet();
                    walletToAddInterestReward.setBalance(savingsWallet.getInterestReward().add(walletToAddInterestReward.getBalance()));
                    User user = savingsWallet.getOwner();
                    user.getSavingsWallets().remove(savingsWallet);
                    savingsWalletRepository.delete(savingsWallet.getId());
                    walletRepository.update(walletToAddInterestReward);
                }
            }
        }
    }

    @Override
    public SavingsWallet create(User user, SavingsWallet savingsWallet) {
        checkModifyPermissions(savingsWallet.getId(), user);
        double latestInterestRate = interestRateService.getLatestInterestRate();
        savingsWallet.setInterestRate(latestInterestRate);
        savingsWallet.setInterestReward(BigDecimal.valueOf(savingsWallet.getInterestRate() * savingsWallet.getDurationMonths()));
        user.getSavingsWallets().add(savingsWallet);
        userRepository.update(user);
        savingsWalletRepository.create(savingsWallet);
        return savingsWallet;
    }

    @Override
    public void update(int id, User user, SavingsWallet savingsWallet) {
        checkModifyPermissions(id, user);
        savingsWalletRepository.update(savingsWallet);
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        user.getSavingsWallets().remove(savingsWalletRepository.get(id));
        userRepository.update(user);
        savingsWalletRepository.delete(id);
    }

    private void checkModifyPermissions(int savingsWalletId, User user) {
        SavingsWallet savingsWallet = savingsWalletRepository.get(savingsWalletId);
        if (!savingsWallet.getOwner().equals(user)) {
            throw new AuthorizationException(MODIFY_WALLET_ERROR_MESSAGE);
        }
    }
}
