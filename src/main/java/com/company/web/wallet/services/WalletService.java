package com.company.web.wallet.services;


import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.exceptions.OperationNotSupportedException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;


import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    Wallet get(int id, User user) throws EntityDeletedException, EntityNotFoundException, AuthorizationException;

    Wallet getByNumber(int number, User user);

    int getWalletIdForUser(User user);

    List<Wallet> getAll(User user) throws EntityNotFoundException;

    void create(Wallet wallet);

    void updateOverdraft(int id, User user, Wallet wallet) throws AuthorizationException;

    void chargeInterestOnOverdraft();

    void addToBalance(int id, User user, BigDecimal amount) throws AuthorizationException;

    void removeFromBalance(int id, User user, BigDecimal amount) throws AuthorizationException, OperationNotSupportedException;

    void delete(int id, User user) throws AuthorizationException;
}
