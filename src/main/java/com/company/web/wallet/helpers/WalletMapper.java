package com.company.web.wallet.helpers;

import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.models.WalletDtoIn;
import com.company.web.wallet.models.WalletDtoOut;
import com.company.web.wallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WalletMapper {
    private final WalletService walletService;

    @Autowired
    public WalletMapper(WalletService walletService) {
        this.walletService = walletService;
    }

    public Wallet createWalletDto(User user) {
        Wallet wallet = new Wallet();
        wallet.setOwner(user);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setOverdraftEnabled(0);
        wallet.setTransactions(new HashSet<>());
        return wallet;
    }

    public Wallet updateOverdraftDto(int id, WalletDtoIn walletDtoIn, User user) {
        Wallet walletToBeUpdated = walletService.get(id, user);
        walletToBeUpdated.setOverdraftEnabled(walletDtoIn.getOverdraftEnabled());
        return walletToBeUpdated;
    }

    public WalletDtoOut walletDtoOut(Wallet wallet) {
        WalletDtoOut walletDtoOut = new WalletDtoOut();
        walletDtoOut.setBalance(wallet.getBalance());
        walletDtoOut.setOwner(wallet.getOwner().getUsername());
        walletDtoOut.setTransactions(wallet.getTransactions());
        walletDtoOut.setOverdraftEnabled(wallet.getOverdraftEnabled());
        return walletDtoOut;
    }

    public List<WalletDtoOut> walletDtoOutList(List<Wallet> wallets) {
        return wallets.stream()
                .map(this::walletDtoOut)
                .collect(Collectors.toList());
    }
}
