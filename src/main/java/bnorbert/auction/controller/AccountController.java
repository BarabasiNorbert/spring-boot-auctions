package bnorbert.auction.controller;

import bnorbert.auction.service.AccountService;
import bnorbert.auction.transfer.account.AccountDto;
import bnorbert.auction.transfer.account.TransactionDto;
import bnorbert.auction.transfer.account.TransactionsResponse;
import bnorbert.auction.transfer.account.TransferDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Void> openAccount(@RequestBody AccountDto accountDto) {
        accountService.openAccount(accountDto);
        return new ResponseEntity<>(CREATED);
    }

    @PostMapping("/depositOrWithdraw")
    public ResponseEntity<Void> funds(@RequestBody TransactionDto transactionDto) {
        accountService.depositOrWithdraw(transactionDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferDto transferDto) {
        accountService.transfer(transferDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getTransactions")
    public ResponseEntity<Page<TransactionsResponse>> getTransactions(String account_id, Pageable pageable) {
        Page<TransactionsResponse> transactions = accountService.getTransactions(account_id, pageable);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
