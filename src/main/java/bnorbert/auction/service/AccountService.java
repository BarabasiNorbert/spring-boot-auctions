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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static bnorbert.auction.domain.TransactionType.*;

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

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
    public void openAccount(AccountDto request) {
        Account account = accountMapper.map(request, authService.getCurrentUser());
        accountRepository.save(account);
    }

    public Account getDueDiligence(Long user_id){
        LOGGER.info("Retrieving account for user: {}", user_id);
        return accountRepository.findTopByUser_IdOrderByBalanceDesc(user_id)
                .orElseThrow((EntityNotFoundException::new));
    }

    @Transactional
    public void depositOrWithdraw(TransactionDto request) {
        Account account = accountRepository
                .findById(request.getAccountId()).orElseThrow(() ->
                        new ResourceNotFoundException("Account with id ("
                                + request.getAccountId() + ") not found"));
        Transaction transaction = transactionMapper.map(request, account, authService.getCurrentUser());
        transactionType(request, account, transaction);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    private void transactionType(TransactionDto request, Account account, Transaction transaction) {
        if (DEPOSIT.equals(request.getTransactionType())) {
            account.setBalance(account.getBalance() + transaction.getAmount());
            account.setTransactionCount(account.getTransactionCount() + 1);
        }else if(WITHDRAWAL.equals(request.getTransactionType())) {
            account.setBalance(account.getBalance() - transaction.getAmount());
            account.setTransactionCount(account.getTransactionCount() + 1);
            if (account.getBalance() <= 0)
                throw new ResourceNotFoundException("err");
        }else throw new ResourceNotFoundException(request.getTransactionType() + "TransactionType issue");
    }


    @Transactional
    public void transfer(TransferDto request) {
        Account account = accountRepository
                .findById(request.getAccountId()).orElseThrow(() ->
                        new ResourceNotFoundException("Account with id ("
                                + request.getAccountId() + ") not found"));

        Account toAccount = accountRepository
                .findById(request.getTransferToAccountId()).orElseThrow(() ->
                        new ResourceNotFoundException("Account with id ("
                                + request.getTransferToAccountId() + ") not found"));


        Transaction transaction = transactionMapper.map2(request, account, authService.getCurrentUser());
        if (TRANSFER.equals(request.getTransactionType())) {

            account.setBalance(account.getBalance() - transaction.getAmount());
            if (account.getBalance() <= 0)
                throw new ResourceNotFoundException("err");
            toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());

            account.setTransactionCount(account.getTransactionCount() + 1);
            toAccount.setTransactionCount(toAccount.getTransactionCount() + 1);

            accountRepository.save(toAccount);
            accountRepository.save(account);

        }else throw new ResourceNotFoundException(request.getTransactionType()+ "TransactionType issue");

        transactionRepository.save(transaction);
    }


    @Transactional
    public Page<TransactionsResponse> getTransactions(String account_id, Pageable pageable){
        Page<Transaction> transactions = transactionRepository.findTransactionsByAccount_Id(account_id, pageable);
        List<TransactionsResponse> getTransactionResponses = transactionMapper.entitiesToEntityDTOs(transactions.getContent());
        return new PageImpl<>(getTransactionResponses, pageable, transactions.getTotalElements());
    }


}
