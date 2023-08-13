package com.company.web.wallet.exceptions;

public class EntityDeletedException extends RuntimeException {
    public EntityDeletedException(String type, String attribute, String value) {
        super(String.format("%s with %s %s was deleted.", type, attribute, value));
    }
}