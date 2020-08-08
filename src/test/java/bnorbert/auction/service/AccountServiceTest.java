package bnorbert.auction.service;

import bnorbert.auction.domain.Account;
import bnorbert.auction.domain.Transaction;
import bnorbert.auction.domain.TransactionType;
import bnorbert.auction.domain.User;
import bnorbert.auction.mapper.AccountMapper;
import bnorbert.auction.mapper.TransactionMapper;
import bnorbert.auction.repository.AccountRepository;
import bnorbert.auction.repository.TransactionRepository;
import bnorbert.auction.transfer.account.AccountDto;
import bnorbert.auction.transfer.account.TransactionDto;
import bnorbert.auction.transfer.account.TransactionsResponse;
import bnorbert.auction.transfer.account.TransferDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class AccountServiceTest {

    @Mock
    private AccountRepository mockAccountRepository;
    @Mock
    private AccountMapper mockAccountMapper;
    @Mock
    private UserService mockAuthService;
    @Mock
    private TransactionRepository mockTransactionRepository;
    @Mock
    private TransactionMapper mockTransactionMapper;

    private AccountService accountServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        accountServiceUnderTest = new AccountService(mockAccountRepository, mockAccountMapper, mockAuthService, mockTransactionRepository, mockTransactionMapper);
    }

    @Test
    void testOpenAccount() {
        final AccountDto accountDto = new AccountDto();
        accountDto.setBalance(100000.0);
        accountDto.setFirstName("firstName");
        accountDto.setLastName("lastName");


        final Account account = new Account();
        account.setId("id");
        account.setBalance(100000.0);
        account.setFirstName("firstName");
        account.setLastName("lastName");
        account.setUser(new User());
        account.setTransactionCount(0);
        when(mockAccountMapper.map(any(AccountDto.class), eq(new User()))).thenReturn(account);

        when(mockAuthService.getCurrentUser()).thenReturn(new User());

        accountServiceUnderTest.openAccount(accountDto);
    }

    @Test
    void testWithdraw() {

        final TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.WITHDRAWAL);
        transactionDto.setAccountId("id");
        transactionDto.setAmount(200.0);

        final Account account1 = new Account();
        account1.setId("id");
        account1.setBalance(2000.0);
        account1.setFirstName("firstName");
        account1.setLastName("lastName");
        account1.setUser(new User());
        account1.setTransactionCount(0);
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findById("id")).thenReturn(account);

        final Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccount(account1);
        transaction.setAmount(200.0);
        transaction.setDescription("description");
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setUser(new User());
        when(mockTransactionMapper.map(any(TransactionDto.class), any(Account.class), eq(new User()))).thenReturn(transaction);

        when(mockAuthService.getCurrentUser()).thenReturn(new User());

        accountServiceUnderTest.depositOrWithdraw(transactionDto);
    }


    @Test
    void testDeposit() {

        final TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setAccountId("id");
        transactionDto.setAmount(100.0);

        final Account account1 = new Account();
        account1.setId("id");
        account1.setBalance(1000.0);
        account1.setFirstName("firstName");
        account1.setLastName("lastName");
        account1.setUser(new User());
        account1.setTransactionCount(1);
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findById("id")).thenReturn(account);

        final Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccount(account1);
        transaction.setAmount(100.0);
        transaction.setDescription("description");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setUser(new User());
        when(mockTransactionMapper.map(any(TransactionDto.class), any(Account.class), eq(new User()))).thenReturn(transaction);

        when(mockAuthService.getCurrentUser()).thenReturn(new User());

        accountServiceUnderTest.depositOrWithdraw(transactionDto);
    }


    @Test
    void testTransfer() {
        final TransferDto transferDto = new TransferDto();
        transferDto.setTransactionType(TransactionType.TRANSFER);
        transferDto.setAccountId("1");
        transferDto.setAmount(100.0);
        transferDto.setTransferToAccountId("2");

        final Account account1 = new Account();
        account1.setId("1");
        account1.setBalance(1000.0);
        account1.setFirstName("firstName");
        account1.setLastName("lastName");
        account1.setUser(new User());
        account1.setTransactionCount(0);
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findById("1")).thenReturn(account);

        final Transaction transaction = new Transaction();
        transaction.setId(1L);

        final Account account2 = new Account();
        account2.setId("2");
        account2.setBalance(1000.0);
        account2.setFirstName("firstName");
        account2.setLastName("lastName");
        account2.setUser(new User());
        account2.setTransactionCount(0);

        transaction.setAccount(account1);
        transaction.setAmount(100.0);
        transaction.setDescription("description");
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setUser(new User());
        transaction.setTransferToAccountId("2");
        when(mockTransactionMapper.map2(any(TransferDto.class), any(Account.class), eq(new User()))).thenReturn(transaction);

        when(mockAuthService.getCurrentUser()).thenReturn(new User());

        accountServiceUnderTest.transfer(transferDto);

    }

    @Test
    void testGetTransactions() {

        final Transaction transaction = new Transaction();
        transaction.setId(1L);
        final Account account = new Account();
        account.setId("account_id");
        account.setBalance(1000.0);
        account.setFirstName("firstName");
        account.setLastName("lastName");
        account.setUser(new User());
        account.setTransactionCount(0);
        transaction.setAccount(account);
        transaction.setAmount(100.0);
        transaction.setDescription("description");
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setUser(new User());
        transaction.setTransferToAccountId("transferToAccountId");
        final Page<Transaction> transactions = new PageImpl<>(Collections.singletonList(transaction));
        when(mockTransactionRepository.findTransactionsByAccount_Id(eq("account_id"), any(Pageable.class))).thenReturn(transactions);


        final TransactionsResponse transactionsResponse = new TransactionsResponse();
        transactionsResponse.setAmount(100.0);
        transactionsResponse.setDescription("description");
        transactionsResponse.setTransactionType(TransactionType.WITHDRAWAL);
        transactionsResponse.setTransferToAccountId("transferToAccountId");
        transactionsResponse.setUserId(1L);
        transactionsResponse.setAccountId("accountId");
        final List<TransactionsResponse> transactionsResponses = Collections.singletonList(transactionsResponse);
        when(mockTransactionMapper.entitiesToEntityDTOs(Collections.singletonList(new Transaction()))).thenReturn(transactionsResponses);


        final Page<TransactionsResponse> result = accountServiceUnderTest.getTransactions("account_id", PageRequest.of(0, 1));
    }
}
