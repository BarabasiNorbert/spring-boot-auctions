package bnorbert.auction.service;

import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.exception.ResourceNotFoundException;
import bnorbert.auction.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TimeSlotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSlotService.class);
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;

    }

    public TimeSlot getTimeSlot(long id) {
        LOGGER.info("Retrieving timeSlot {}", id);
        return timeSlotRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("There is no timeSlot with id (" + id + ")."));
    }


}
