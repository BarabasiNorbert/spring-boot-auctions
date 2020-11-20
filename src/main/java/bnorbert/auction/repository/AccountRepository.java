package bnorbert.auction.repository;

import bnorbert.auction.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findTopByUser_IdOrderByBalanceDesc(long user_id);
}
