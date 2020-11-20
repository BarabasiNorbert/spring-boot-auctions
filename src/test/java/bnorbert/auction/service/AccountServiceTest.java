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
        accountDto.setBalance(100000);
        accountDto.setFirstName("firstName");
        accountDto.setLastName("lastName");

        User user = new User();
        user.setId(1L);

        final Account account = new Account();
        account.setId("iban");
        account.setBalance(100000);
        account.setFirstName("firstName");
        account.setLastName("lastName");
        account.setUser(user);
        account.setTransactionCount(1);
        when(mockAccountMapper.map(any(AccountDto.class), eq(user))).thenReturn(account);

        when(mockAuthService.getCurrentUser()).thenReturn(user);

        accountServiceUnderTest.openAccount(accountDto);
    }

    @Test
    void testWithdraw() {

        final TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.WITHDRAWAL);
        transactionDto.setAccountId("id");
        transactionDto.setAmount(200);

        User user = new User();
        user.setId(1L);

        final Account account1 = new Account();
        account1.setId("id");
        account1.setBalance(2000);
        account1.setFirstName("firstName");
        account1.setLastName("lastName");
        account1.setUser(new User());
        account1.setTransactionCount(1);
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findById(account1.getId())).thenReturn(account);

        final Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccount(account1);
        transaction.setAmount(200);
        transaction.setDescription("description");
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setUser(user);
        when(mockTransactionMapper.map(any(TransactionDto.class), any(Account.class), eq(user))).thenReturn(transaction);

        when(mockAuthService.getCurrentUser()).thenReturn(user);

        accountServiceUnderTest.depositOrWithdraw(transactionDto);
    }


    @Test
    void testDeposit() {

        final TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setAccountId("id");
        transactionDto.setAmount(100);

        User user = new User();
        user.setId(1L);

        final Account account1 = new Account();
        account1.setId("id");
        account1.setBalance(1000);
        account1.setFirstName("firstName");
        account1.setLastName("lastName");
        account1.setUser(user);
        account1.setTransactionCount(1);
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findById("id")).thenReturn(account);

        final Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccount(account1);
        transaction.setAmount(100);
        transaction.setDescription("description");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setUser(user);
        when(mockTransactionMapper.map(any(TransactionDto.class), any(Account.class), eq(user))).thenReturn(transaction);

        when(mockAuthService.getCurrentUser()).thenReturn(user);

        accountServiceUnderTest.depositOrWithdraw(transactionDto);
    }


    @Test
    void testTransfer() {
        User user = new User();
        user.setId(1L);

        final Account account1 = new Account();
        account1.setId("1");
        account1.setBalance(1000);
        account1.setFirstName("firstName");
        account1.setLastName("lastName");
        account1.setUser(user);
        account1.setTransactionCount(1);
        //final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findById(account1.getId())).thenReturn(Optional.of(account1));

        final Transaction transaction = new Transaction();
        transaction.setId(1L);

        final Account account2 = new Account();
        account2.setId("2");
        account2.setBalance(5);
        account2.setFirstName("firstName");
        account2.setLastName("lastName");
        account2.setUser(user);
        account2.setTransactionCount(1);
        when(mockAccountRepository.findById(account2.getId())).thenReturn(Optional.of(account2));

        final TransferDto transferDto = new TransferDto();
        transferDto.setTransactionType(TransactionType.TRANSFER);
        transferDto.setAccountId(account1.getId());
        transferDto.setAmount(100);
        transferDto.setTransferToAccountId(account2.getId());

        transaction.setAccount(account1);
        transaction.setAmount(100);
        transaction.setDescription("description");
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setUser(user);
        transaction.setTransferToAccountId(account2.getId());
        when(mockTransactionMapper.map2(any(TransferDto.class), any(Account.class), eq(user))).thenReturn(transaction);

        when(mockAuthService.getCurrentUser()).thenReturn(user);

        accountServiceUnderTest.transfer(transferDto);

    }

    @Test
    void testGetTransactions() {

        User user = new User();
        user.setId(1L);

        final Account account = new Account();
        account.setId("account_id");
        account.setBalance(1000);
        account.setFirstName("firstName");
        account.setLastName("lastName");
        account.setUser(user);
        account.setTransactionCount(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(100);
        transaction.setDescription("description");
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setUser(user);
        transaction.setTransferToAccountId("transferToAccountId");

        final Page<Transaction> transactions = new PageImpl<>(Collections.singletonList(transaction));
        when(mockTransactionRepository.findTransactionsByAccount_Id(eq(account.getId()), any(Pageable.class)))
                .thenReturn(transactions);


        final TransactionsResponse transactionsResponse = new TransactionsResponse();
        transactionsResponse.setAmount(100);
        transactionsResponse.setDescription("description");
        transactionsResponse.setTransactionType(TransactionType.WITHDRAWAL);
        transactionsResponse.setTransferToAccountId("transferToAccountId");
        transactionsResponse.setUserId(user.getId());
        transactionsResponse.setAccountId("accountId");
        final List<TransactionsResponse> transactionsResponses = Collections.singletonList(transactionsResponse);
        when(mockTransactionMapper.entitiesToEntityDTOs(Collections.singletonList(transaction))).thenReturn(transactionsResponses);


        final Page<TransactionsResponse> result = accountServiceUnderTest.getTransactions("account_id",
                PageRequest.of(0, 1));
    }
}
