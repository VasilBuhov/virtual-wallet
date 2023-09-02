package com.company.web.wallet.exceptions;

public class ZeroAmountTransactionException extends RuntimeException{

    public ZeroAmountTransactionException(String message) {
        super(message);
    }
}
