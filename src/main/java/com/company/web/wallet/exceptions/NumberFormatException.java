package com.company.web.wallet.exceptions;

public class NumberFormatException extends RuntimeException {

    public NumberFormatException(String type, int id) {
        this(type, "id", String.valueOf(id));
    }

    public NumberFormatException(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found.", type, attribute, value));
    }
}