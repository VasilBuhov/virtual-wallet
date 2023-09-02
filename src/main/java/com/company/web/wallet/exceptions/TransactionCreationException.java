package com.company.web.wallet.exceptions;

public class TransactionCreationException extends RuntimeException {
    public TransactionCreationException(String message) {
        super(message);
    }
}
