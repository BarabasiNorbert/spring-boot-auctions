package bnorbert.auction.repository;

import bnorbert.auction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findTransactionsByAccount_Id(String account_id, Pageable pageable);
}
