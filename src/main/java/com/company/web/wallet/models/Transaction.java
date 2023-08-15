package com.company.web.wallet.models;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Transaction {
    private Long id;
    private User sender;
    private User recipient;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime timestamp;
    private String transactionDescription;
    private String status;
    public Transaction() {
    }
    public Transaction(Long id,
                       User sender,
                       User recipient,
                       BigDecimal amount,
                       TransactionType transactionType,
                       LocalDateTime timestamp,
                       String transactionDescription,
                       String status) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        this.transactionDescription= transactionDescription;
        this.status= status;
    }
    public enum TransactionType {
        INCOMING,
        OUTGOING
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public User getRecipient() {
        return recipient;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public TransactionType getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getTransactionDescription() {
        return transactionDescription;
    }
    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }
}
