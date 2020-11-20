package bnorbert.auction.mapper;

import bnorbert.auction.domain.Account;
import bnorbert.auction.domain.Transaction;
import bnorbert.auction.domain.User;
import bnorbert.auction.transfer.account.TransactionDto;
import bnorbert.auction.transfer.account.TransactionsResponse;
import bnorbert.auction.transfer.account.TransferDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionType", source = "transactionDto.transactionType")
    @Mapping(target = "amount", source = "transactionDto.amount")
    @Mapping(target = "account", source = "account")
    @Mapping(target = "user", source = "user")
    public abstract Transaction map(TransactionDto transactionDto, Account account, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionType", source = "transferDto.transactionType")
    @Mapping(target = "amount", source = "transferDto.amount")
    @Mapping(target = "account", source = "account")
    @Mapping(target = "transferToAccountId", source = "transferDto.transferToAccountId")
    @Mapping(target = "user", source = "user")
    public abstract Transaction map2(TransferDto transferDto, Account account, User user);


    //@Mapping(target = "amount", source = "transaction.amount")
    //@Mapping(target = "description", source = "transaction.description")
    @Mapping(target = "userId", source = "user.id")//#manytoone
    @Mapping(target = "accountId", source = "account.id")
    public abstract TransactionsResponse mapToTransactionResponse(Transaction transaction);

    public abstract List<TransactionsResponse> entitiesToEntityDTOs(List<Transaction> transactions);
}
