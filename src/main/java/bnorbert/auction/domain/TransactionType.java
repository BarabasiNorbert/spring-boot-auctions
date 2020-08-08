package bnorbert.auction.domain;

public enum TransactionType {
    WITHDRAWAL(-1), DEPOSIT(1),TRANSFER(+2),
    ;

    private int direction;

    TransactionType(int direction) {
    }


}
