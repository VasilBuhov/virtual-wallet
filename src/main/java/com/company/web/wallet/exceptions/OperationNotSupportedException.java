package com.company.web.wallet.exceptions;

public class OperationNotSupportedException extends RuntimeException{
    public OperationNotSupportedException() {
        super("Balance can not fall below zero, when overdraft is not enabled.");
    }
}
