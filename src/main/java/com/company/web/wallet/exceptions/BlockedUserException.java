package com.company.web.wallet.exceptions;

public class BlockedUserException extends RuntimeException {
    public BlockedUserException(String message) {
        super("Blocked user can not perform any action");
    }
}