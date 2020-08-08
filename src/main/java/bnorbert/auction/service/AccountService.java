package bnorbert.auction.service;

import bnorbert.auction.domain.Account;
import bnorbert.auction.domain.Transaction;
import bnorbert.auction.exception.ResourceNotFoundException;
import bnorbert.auction.mapper.AccountMapper;
import bnorbert.auction.mapper.TransactionMapper;
import bnorbert.auction.repository.AccountRepository;
import bnorbert.auction.repository.TransactionRepository;
import bnorbert.auction.transfer.account.AccountDto;
import bnorbert.auction.transfer.account.TransactionDto;
import bnorbert.auction.transfer.account.TransactionsResponse;
import bnorbert.auction.transfer.account.TransferDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static bnorbert.auction.domain.TransactionType.*;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserService authService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, UserService authService,
                          TransactionRepository transactionRepository,
                          TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.authService = authService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    public void openAccount(AccountDto accountDto) {
        Account account = accountMapper.map(accountDto, authService.getCurrentUser());
        accountRepository.save(account);
    }


    @Transactional
    public void depositOrWithdraw(TransactionDto transactionDto) {
         Account account = accountRepository
                .findById(transactionDto.getAccountId()).orElseThrow(() ->
                         new ResourceNotFoundException("Account with id ("
                                 + transactionDto.getAccountId() + ") not found"));

         Transaction transaction = transactionMapper.map(transactionDto, account, authService.getCurrentUser());

        if (DEPOSIT.equals(transactionDto.getTransactionType())) {
            account.setBalance(account.getBalance() + transaction.getAmount());
            account.setTransactionCount(account.getTransactionCount() + 1);
        }else if(WITHDRAWAL.equals(transactionDto.getTransactionType())) {
            account.setBalance(account.getBalance() - transaction.getAmount());
            account.setTransactionCount(account.getTransactionCount() - 1);
            if (account.getBalance() <= 0)
                throw new ResourceNotFoundException("err");
        }else throw new ResourceNotFoundException("TransactionType issue");

         accountRepository.save(account);
         transactionRepository.save(transaction);
    }


    @Transactional
    public void transfer(TransferDto transferDto) {
        Account account = accountRepository
                .findById(transferDto.getAccountId()).orElseThrow(() ->
                        new ResourceNotFoundException("Account with id ("
                                + transferDto.getAccountId() + ") not found"));

        Account toAccount = accountRepository
                .findById(transferDto.getTransferToAccountId()).orElseThrow(() ->
                        new ResourceNotFoundException("Account with id ("
                                + transferDto.getTransferToAccountId() + ") not found"));


        Transaction transaction = transactionMapper.map2(transferDto, account, authService.getCurrentUser());
        if (TRANSFER.equals(transferDto.getTransactionType())) {

            account.setBalance(account.getBalance() - transaction.getAmount());
            if (account.getBalance() <= 0)
                throw new ResourceNotFoundException("err");
            toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());

            accountRepository.save(toAccount);
            accountRepository.save(account);

        }else throw new ResourceNotFoundException("TransactionType issue");

        transactionRepository.save(transaction);
    }


    @Transactional
    public Page<TransactionsResponse> getTransactions(String account_id, Pageable pageable){
        Page<Transaction> transactions = transactionRepository.findTransactionsByAccount_Id(account_id, pageable);
        List<TransactionsResponse> getTransactionResponses = transactionMapper.entitiesToEntityDTOs(transactions.getContent());
        return new PageImpl<>(getTransactionResponses, pageable, transactions.getTotalElements());
    }


}
