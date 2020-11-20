package bnorbert.auction.controller;

import bnorbert.auction.service.TimeSlotService;
import bnorbert.auction.transfer.timeslot.TimeSlotsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/timeSlots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @GetMapping("/getTimeSlots")
    public ResponseEntity<Page<TimeSlotsResponse>> getTimeSlotsWithBids(
            Pageable pageable) {
        Page<TimeSlotsResponse> timeSlot = timeSlotService.getTimeSlotsWithBids(pageable);
        return new ResponseEntity<>(timeSlot, HttpStatus.OK);
    }

    @GetMapping("/getTimeSlotsWithZeroBids")
    public ResponseEntity<Page<TimeSlotsResponse>> getTimeSlotsWithZeroBids(
            Pageable pageable) {
        Page<TimeSlotsResponse> timeSlot = timeSlotService.getTimeSlotsWithZeroBids(pageable);
        return new ResponseEntity<>(timeSlot, HttpStatus.OK);
    }
}
