package bnorbert.auction.repository;

import bnorbert.auction.domain.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    Optional<Bid> findTop1ByTimeSlot_IdOrderByAmountDesc(Long timeSlot_id);
    Page<Bid> findBidsByTimeSlot_IdOrderByAmountDesc(Long timeSlot_id, Pageable pageable);
    List<Bid> findBidsByHome_IdOrderByAmountDesc(long home_id);
    Page<Bid> findTop1ByTimeSlot_IdOrderByAmountDesc(Long timeSlot_id, Pageable pageable);

}
