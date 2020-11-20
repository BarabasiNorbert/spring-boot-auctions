package bnorbert.auction.controller;

import bnorbert.auction.domain.TransactionType;
import bnorbert.auction.service.AccountService;
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
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class AccountControllerTest {

    @Mock
    private AccountService mockAccountService;

    private AccountController accountControllerUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        accountControllerUnderTest = new AccountController(mockAccountService);
    }

    @Test
    void testOpenAccount() {

        final AccountDto accountDto = new AccountDto();
        accountDto.setBalance(1000000);
        accountDto.setFirstName("firstName");
        accountDto.setLastName("lastName");

        final ResponseEntity<Void> result = accountControllerUnderTest.openAccount(accountDto);

        verify(mockAccountService).openAccount(any(AccountDto.class));
    }

    @Test
    void testFunds() {
        final TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.WITHDRAWAL);
        transactionDto.setAccountId("accountId");
        transactionDto.setAmount(100000);

        final ResponseEntity<Void> result = accountControllerUnderTest.funds(transactionDto);

        verify(mockAccountService).depositOrWithdraw(any(TransactionDto.class));
    }

    @Test
    void testTransfer() {
        final TransferDto transferDto = new TransferDto();
        transferDto.setTransactionType(TransactionType.WITHDRAWAL);
        transferDto.setAccountId("accountId");
        transferDto.setAmount(100000);
        transferDto.setTransferToAccountId("transferToAccountId");

        final ResponseEntity<Void> result = accountControllerUnderTest.transfer(transferDto);

        verify(mockAccountService).transfer(any(TransferDto.class));
    }


    @Test
    void testGetTransactions() {

        final TransactionsResponse transactionsResponse = new TransactionsResponse();
        transactionsResponse.setAmount(1000000);
        transactionsResponse.setDescription("description");
        transactionsResponse.setTransactionType(TransactionType.WITHDRAWAL);
        transactionsResponse.setTransferToAccountId("transferToAccountId");
        transactionsResponse.setUserId(10L);
        transactionsResponse.setAccountId("accountId");
        final Page<TransactionsResponse> transactionsResponses = new PageImpl<>(Collections.singletonList(transactionsResponse));
        when(mockAccountService.getTransactions(eq("account_id"), any(Pageable.class))).thenReturn(transactionsResponses);


        final ResponseEntity<Page<TransactionsResponse>> result = accountControllerUnderTest.getTransactions("account_id", PageRequest.of(0, 1));
    }
}
