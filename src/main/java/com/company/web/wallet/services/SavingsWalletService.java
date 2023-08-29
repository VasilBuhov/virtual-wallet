package com.company.web.wallet.services;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.SavingsWallet;
import com.company.web.wallet.models.User;

import java.util.List;

public interface SavingsWalletService {
    SavingsWallet get(int id, User user) throws EntityDeletedException, EntityNotFoundException;

    List<SavingsWallet> getAllForUser(User user) throws EntityNotFoundException;

    void addInterestOnSavings();

    SavingsWallet create(User user, SavingsWallet wallet) throws AuthorizationException;

    void update(int id, User user, SavingsWallet wallet) throws AuthorizationException;

    void delete(int id, User user) throws AuthorizationException;
}
