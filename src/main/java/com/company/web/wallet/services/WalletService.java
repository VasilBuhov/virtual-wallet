package com.company.web.wallet.services;


import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;


import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    Wallet get(int id, User user);

    List<Wallet> getAll(User user);

    void create(Wallet wallet);

    void updateOverdraft(int id,User user, Wallet wallet);
    void addToBalance(int id, User user, BigDecimal amount);
    void removeFromBalance(int id, User user, BigDecimal amount);

    void delete(int id, User user);
}
