package com.company.web.wallet.repositories;

import com.company.web.wallet.models.SavingsWallet;
import com.company.web.wallet.models.User;

import java.util.List;

public interface SavingsWalletRepository {
    SavingsWallet get(int id);

    List<SavingsWallet> getAllForUser(User user);
    List<SavingsWallet> getAllWallets();

    void create(SavingsWallet wallet);

    void update(SavingsWallet wallet);

    void delete(int id);
}
