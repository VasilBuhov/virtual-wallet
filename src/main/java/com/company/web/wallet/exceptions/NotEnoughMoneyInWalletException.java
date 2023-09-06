package com.company.web.wallet.exceptions;

public class NotEnoughMoneyInWalletException extends RuntimeException{
    public NotEnoughMoneyInWalletException(String message) {
        super(message);
    }
}
