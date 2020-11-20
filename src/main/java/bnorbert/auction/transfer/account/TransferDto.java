package bnorbert.auction.transfer.account;

import bnorbert.auction.domain.TransactionType;

public class TransferDto {

    private TransactionType transactionType;
    private String accountId;
    private long amount;
    private String transferToAccountId;

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getTransferToAccountId() {
        return transferToAccountId;
    }

    public void setTransferToAccountId(String transferToAccountId) {
        this.transferToAccountId = transferToAccountId;
    }
}
