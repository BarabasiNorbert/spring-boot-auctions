package bnorbert.auction.repository;

import bnorbert.auction.domain.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HomeRepository extends JpaRepository<Home, Long> {

    Optional<Home> findTopByTimeSlotId(Long timeSlot_id);

    @Override
    List<Home> findAll();
}
