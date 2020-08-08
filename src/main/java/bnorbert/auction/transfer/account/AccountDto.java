package bnorbert.auction.transfer.account;

import bnorbert.auction.exception.ResourceNotFoundException;

public class AccountDto {
    private double balance;
    private String firstName;
    private String lastName;

    public double getBalance() {
        if (balance <= 0){
            throw new ResourceNotFoundException("One is minimum minimorum");
        }else return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
