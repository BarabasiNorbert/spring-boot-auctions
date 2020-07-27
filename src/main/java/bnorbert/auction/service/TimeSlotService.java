package bnorbert.auction.service;

import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.exception.ResourceNotFoundException;
import bnorbert.auction.mapper.TimeSlotMapper;
import bnorbert.auction.repository.TimeSlotRepository;
import bnorbert.auction.transfer.timeslot.TimeSlotsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimeSlotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSlotService.class);
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper timeSlotMapper;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, TimeSlotMapper timeSlotMapper) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotMapper = timeSlotMapper;
    }

    public TimeSlot getTimeSlot(long id) {
        LOGGER.info("Retrieving timeSlot {}", id);
        return timeSlotRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("There is no timeSlot with id (" + id + ")."));
    }

    @Transactional
    public Page<TimeSlotsResponse> getTimeSlots(Pageable pageable){
        Page<TimeSlot> timeSlots = timeSlotRepository.findTimeSlotsByUserIsNotNull(pageable);
        List<TimeSlotsResponse> timeSlotsResponses = timeSlotMapper.entitiesToEntityDTOs(timeSlots.getContent());
        return new PageImpl<>(timeSlotsResponses, pageable, timeSlots.getTotalElements());
    }

    @Transactional
    public Page<TimeSlotsResponse> getTimeSlotsWithZeroBids(Pageable pageable){
        Page<TimeSlot> timeSlots = timeSlotRepository.findTimeSlotsByUserIsNull(pageable);
        List<TimeSlotsResponse> timeSlotsResponses = timeSlotMapper.entitiesToEntityDTOs(timeSlots.getContent());
        return new PageImpl<>(timeSlotsResponses, pageable, timeSlots.getTotalElements());
    }

}
