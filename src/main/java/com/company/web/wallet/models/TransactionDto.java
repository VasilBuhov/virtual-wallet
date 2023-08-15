package com.company.web.wallet.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {
    private User sender;
    private User recipient;
    private BigDecimal amount;
    private Transaction.TransactionType transactionType;
    private LocalDateTime timestamp;
    private String transactionDescription;
    public TransactionDto(){

    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Transaction.TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionType(Transaction.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }
}
