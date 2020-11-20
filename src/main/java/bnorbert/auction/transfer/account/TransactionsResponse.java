package bnorbert.auction.transfer.account;

import bnorbert.auction.domain.TransactionType;

public class TransactionsResponse {

    private long amount;
    private String description;
    private TransactionType transactionType;
    private String transferToAccountId;
    private Long userId;
    private String accountId;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransferToAccountId() {
        return transferToAccountId;
    }

    public void setTransferToAccountId(String transferToAccountId) {
        this.transferToAccountId = transferToAccountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
