package bnorbert.auction.repository;

import bnorbert.auction.domain.TimeSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    @Override
    List<TimeSlot> findAll();

    Page<TimeSlot> findTimeSlotsByUserIsNotNull(Pageable pageable);

    Page<TimeSlot> findTimeSlotsByUserIsNull(Pageable pageable);
}
