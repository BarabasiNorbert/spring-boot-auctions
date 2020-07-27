package bnorbert.auction.controller;

import bnorbert.auction.service.TimeSlotService;
import bnorbert.auction.transfer.timeslot.HomeResponse;
import bnorbert.auction.transfer.timeslot.TimeSlotsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TimeSlotControllerTest {

    @Mock
    private TimeSlotService mockTimeSlotService;

    private TimeSlotController timeSlotControllerUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        timeSlotControllerUnderTest = new TimeSlotController(mockTimeSlotService);
    }

    @Test
    void testGetTimeSlots() {

        final TimeSlotsResponse timeSlotsResponse = new TimeSlotsResponse();
        timeSlotsResponse.setId(1L);
        timeSlotsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        timeSlotsResponse.setStartTime(LocalTime.of(12, 0, 0));
        timeSlotsResponse.setEndTime(LocalTime.of(12, 0, 0));
        timeSlotsResponse.setUserId(1L);
        timeSlotsResponse.setUserEmail("userEmail");
        final HomeResponse homeResponse = new HomeResponse();
        homeResponse.setId(1L);
        homeResponse.setNeighborhood("neighborhood");
        homeResponse.setKitchen(0);
        homeResponse.setLotArea(0);
        homeResponse.setYearBuilt("yearBuilt");
        homeResponse.setFullBath(0);
        homeResponse.setBedroom(0);
        homeResponse.setGarageYearBuilt("garageYearBuilt");
        homeResponse.setGarageCars(0);
        homeResponse.setGarageArea(0);
        timeSlotsResponse.setHomes(new HashSet<>(Collections.singletonList(homeResponse)));
        final Page<TimeSlotsResponse> timeSlotsResponses = new PageImpl<>(Collections.singletonList(timeSlotsResponse));
        when(mockTimeSlotService.getTimeSlots(any(Pageable.class))).thenReturn(timeSlotsResponses);

        final ResponseEntity<Page<TimeSlotsResponse>> result = timeSlotControllerUnderTest.getTimeSlots(PageRequest.of(0, 1));

    }

    @Test
    void testGetTimeSlotsWithZeroBids() {

        final TimeSlotsResponse timeSlotsResponse = new TimeSlotsResponse();
        timeSlotsResponse.setId(20L);
        timeSlotsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        timeSlotsResponse.setStartTime(LocalTime.of(12, 0, 0));
        timeSlotsResponse.setEndTime(LocalTime.of(12, 0, 0));
        timeSlotsResponse.setUserId(2L);
        timeSlotsResponse.setUserEmail("userEmail");
        final HomeResponse homeResponse = new HomeResponse();
        homeResponse.setId(3L);
        homeResponse.setNeighborhood("neighborhood");
        homeResponse.setKitchen(0);
        homeResponse.setLotArea(0);
        homeResponse.setYearBuilt("yearBuilt");
        homeResponse.setFullBath(0);
        homeResponse.setBedroom(0);
        homeResponse.setGarageYearBuilt("garageYearBuilt");
        homeResponse.setGarageCars(0);
        homeResponse.setGarageArea(0);
        timeSlotsResponse.setHomes(new HashSet<>(Collections.singletonList(homeResponse)));
        final Page<TimeSlotsResponse> timeSlotsResponses = new PageImpl<>(Collections.singletonList(timeSlotsResponse));
        when(mockTimeSlotService.getTimeSlotsWithZeroBids(any(Pageable.class))).thenReturn(timeSlotsResponses);

        final ResponseEntity<Page<TimeSlotsResponse>> result = timeSlotControllerUnderTest.getTimeSlotsWithZeroBids(PageRequest.of(0, 1));

    }
}
