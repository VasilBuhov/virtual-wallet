package com.company.web.wallet.models.DTO;

import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.Wallet;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {

    @NotNull(message = "Sender is required")
    private UserSenderDto sender;

    @NotNull(message = "Recipient is required")
    private UserRecipientDto recipient;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;

    @NotEmpty(message = "Transaction description is required")
    private String transactionDescription;

    @NotEmpty(message = "Transaction status is required")
    private String status = "Completed";
//    @JsonIgnore

    private int walletId;
    private Wallet wallet;

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    private String direction;






    public UserSenderDto getSender() {
        return sender;
    }

    public void setSender(UserSenderDto sender) {
        this.sender = sender;
    }

    public UserRecipientDto getRecipient() {
        return recipient;
    }

    public void setRecipient(UserRecipientDto recipient) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }
}
