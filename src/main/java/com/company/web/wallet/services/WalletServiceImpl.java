package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.exceptions.OperationNotSupportedException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class WalletServiceImpl implements WalletService {
    private static final String MODIFY_WALLET_ERROR_MESSAGE = "Only the wallet owner can modify the wallet information.";
    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
    public List<Wallet> getAll(User user) {
        List<Wallet> result = walletRepository.getAll();
        List<Wallet> matchingWallets = result.stream()
                .filter(wallet -> wallet.getOwner().equals(user))
                .collect(Collectors.toList());
        if (matchingWallets.isEmpty()) {
            throw new EntityNotFoundException("Wallets", "owner", user.getUsername());
        }
        return matchingWallets;
    }

    @Override
    public void create(Wallet wallet) {
        walletRepository.create(wallet);
    }

    @Override
    public void updateOverdraft(int id, User user, Wallet wallet) {
        checkModifyPermissions(id, user);
        walletRepository.update(wallet);
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
        walletRepository.delete(id);
    }

    private void checkModifyPermissions(int walletId, User user) {
        Wallet wallet = walletRepository.get(walletId);
        if (!wallet.getOwner().equals(user)) {
            throw new AuthorizationException(MODIFY_WALLET_ERROR_MESSAGE);
        }
    }
}
