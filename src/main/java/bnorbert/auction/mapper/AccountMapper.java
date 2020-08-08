package bnorbert.auction.mapper;

import bnorbert.auction.domain.Account;
import bnorbert.auction.domain.User;
import bnorbert.auction.transfer.account.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public abstract class AccountMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", source = "accountDto.balance")
    @Mapping(target = "firstName", source = "accountDto.firstName")
    @Mapping(target = "lastName", source = "accountDto.lastName")

    @Mapping(target = "user", source = "user")
    public abstract Account map(AccountDto accountDto, User user);

}

