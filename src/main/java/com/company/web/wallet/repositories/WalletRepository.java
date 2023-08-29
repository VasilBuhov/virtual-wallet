package com.company.web.wallet.repositories;


import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletRepository {
    Wallet get(int id) throws EntityNotFoundException;
    Wallet get(String owner) throws EntityNotFoundException, EntityDeletedException;

    List<Wallet> getAll();

    void create(Wallet wallet);

    void update(Wallet wallet);

    void delete(int id);

    Integer getWalletIdForUser(User user);
}
