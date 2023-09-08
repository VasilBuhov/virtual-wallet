package com.company.web.wallet.helpers;

import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.models.SavingsWallet;
import com.company.web.wallet.models.DTO.SavingsWalletDtoIn;
import com.company.web.wallet.models.DTO.SavingsWalletDtoOut;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SavingsWalletMapper {


    public SavingsWalletMapper() {}
    public SavingsWallet createSavingsWalletFromDto(SavingsWalletDtoIn savingsWalletDtoIn, User user) {
        SavingsWallet savingsWallet = new SavingsWallet();
        Wallet fromWallet = user.getWallets().stream()
                .filter(wallet -> wallet.getNumberOfWallet() == savingsWalletDtoIn.getFromWalletNumber())
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        savingsWallet.setFromWallet(fromWallet);
        savingsWallet.setDeposit(savingsWalletDtoIn.getDeposit());
        savingsWallet.setOwner(user);
        savingsWallet.setDurationMonths(savingsWalletDtoIn.getDurationMonths());
        savingsWallet.setEndDate(LocalDateTime.now().plusMonths(savingsWalletDtoIn.getDurationMonths()));
        savingsWallet.setIsDeleted(0);
        return savingsWallet;
    }
    public SavingsWalletDtoOut returnSavingsWalletDto (SavingsWallet savingsWallet) {
        SavingsWalletDtoOut savingsWalletDtoOut = new SavingsWalletDtoOut();
        savingsWalletDtoOut.setDeposit(savingsWallet.getDeposit());
        savingsWalletDtoOut.setInterestRate(savingsWallet.getInterestRate());
        savingsWalletDtoOut.setOwnerUsername(savingsWallet.getOwner().getUsername());
        savingsWalletDtoOut.setDurationMonths(savingsWallet.getDurationMonths());
        savingsWalletDtoOut.setEndDate(savingsWallet.getEndDate());
        savingsWalletDtoOut.setInterestReward(savingsWallet.getInterestReward());
        return savingsWalletDtoOut;
    }
    public List<SavingsWalletDtoOut> savingsWalletDtoOutList(List<SavingsWallet> savingsWallets) {
        return savingsWallets.stream()
                .map(this::returnSavingsWalletDto)
                .collect(Collectors.toList());
    }
}
