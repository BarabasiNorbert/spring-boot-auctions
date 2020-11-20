package bnorbert.auction.repository;

import bnorbert.auction.domain.Home;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HomeRepository extends JpaRepository<Home, Long> {

    Optional<Home> findTopByTimeSlotId(Long timeSlot_id);

    Page<Home> findTopByTimeSlotId(Long timeSlot_id, Pageable pageable);

    Page<Home> findByNeighborhoodContaining(String neighborhood, Pageable pageable);

    @Override
    List<Home> findAll();
}
