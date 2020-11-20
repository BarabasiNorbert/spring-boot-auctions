package bnorbert.auction.service;

import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.domain.User;
import bnorbert.auction.mapper.TimeSlotMapper;
import bnorbert.auction.repository.TimeSlotRepository;
import bnorbert.auction.transfer.timeslot.HomeResponse;
import bnorbert.auction.transfer.timeslot.TimeSlotsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TimeSlotServiceTest {


    @Mock
    private TimeSlotRepository mockTimeSlotRepository;
    @Mock
    private TimeSlotMapper mockTimeSlotMapper;

    private TimeSlotService timeSlotServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        timeSlotServiceUnderTest = new TimeSlotService(mockTimeSlotRepository, mockTimeSlotMapper);
    }

    @Test
    void testGetTimeSlot() {

        final Optional<TimeSlot> timeSlot = Optional.of(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(9, 0, 0),
                LocalTime.of(12, 0, 0)));
        when(mockTimeSlotRepository.findById(1L)).thenReturn(timeSlot);

        final TimeSlot result = timeSlotServiceUnderTest.getTimeSlot(1L);

    }

    @Test
    void testGetTimeSlotThenThrowResourceNotFoundException() {

        final Optional<TimeSlot> timeSlot = Optional.of(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(9, 0, 0),
                LocalTime.of(12, 0, 0)));
        when(mockTimeSlotRepository.findById(1L)).thenReturn(timeSlot);

        final TimeSlot result = timeSlotServiceUnderTest.getTimeSlot(0L);

    }

    @Test
    void testGetTimeSlots() {

        final Page<TimeSlot> timeSlots = new PageImpl<>(Collections.singletonList(new TimeSlot(DayOfWeek.FRIDAY,
                LocalTime.of(8, 0, 0), LocalTime.of(12, 0, 0))));
        when(mockTimeSlotRepository.findTimeSlotsByUserIsNotNull(any(Pageable.class))).thenReturn(timeSlots);

        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        final TimeSlotsResponse timeSlotsResponse = new TimeSlotsResponse();
        timeSlotsResponse.setId(1L);
        timeSlotsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        timeSlotsResponse.setStartTime(LocalTime.of(8, 0, 0));
        timeSlotsResponse.setEndTime(LocalTime.of(12, 0, 0));
        timeSlotsResponse.setUserId(user.getId());
        timeSlotsResponse.setUserEmail(user.getEmail());
        final HomeResponse homeResponse = new HomeResponse();
        homeResponse.setId(1L);
        homeResponse.setNeighborhood("neighborhood");
        homeResponse.setKitchen(1);
        homeResponse.setLotArea(8742);
        homeResponse.setYearBuilt("yearBuilt");
        homeResponse.setFullBath(1);
        homeResponse.setBedroom(1);
        homeResponse.setGarageYearBuilt("garageYearBuilt");
        homeResponse.setGarageCars(2);
        homeResponse.setGarageArea(545);
        timeSlotsResponse.setHomes(new HashSet<>(Collections.singletonList(homeResponse)));
        final List<TimeSlotsResponse> timeSlotsResponses = Collections.singletonList(timeSlotsResponse);
        when(mockTimeSlotMapper.entitiesToEntityDTOs(Collections.singletonList(new TimeSlot(DayOfWeek.FRIDAY,
                LocalTime.of(8, 0, 0), LocalTime.of(12, 0, 0))))).thenReturn(timeSlotsResponses);

        final Page<TimeSlotsResponse> result = timeSlotServiceUnderTest.getTimeSlotsWithBids(PageRequest.of(0, 1));
    }

    @Test
    void testGetTimeSlotsWithZeroBids() {

        final Page<TimeSlot> timeSlots = new PageImpl<>(Collections.singletonList(new TimeSlot(DayOfWeek.FRIDAY,
                LocalTime.of(8, 0, 0), LocalTime.of(12, 0, 0))));
        when(mockTimeSlotRepository.findTimeSlotsByUserIsNull(any(Pageable.class))).thenReturn(timeSlots);

        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        final TimeSlotsResponse timeSlotsResponse = new TimeSlotsResponse();
        timeSlotsResponse.setId(1L);
        timeSlotsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        timeSlotsResponse.setStartTime(LocalTime.of(8, 0, 0));
        timeSlotsResponse.setEndTime(LocalTime.of(12, 0, 0));
        timeSlotsResponse.setUserId(user.getId());
        timeSlotsResponse.setUserEmail(user.getEmail());
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
        final List<TimeSlotsResponse> timeSlotsResponses = Collections.singletonList(timeSlotsResponse);
        when(mockTimeSlotMapper.entitiesToEntityDTOs(Collections.singletonList(new TimeSlot(DayOfWeek.FRIDAY,
                LocalTime.of(8, 0, 0), LocalTime.of(12, 0, 0))))).thenReturn(timeSlotsResponses);

        final Page<TimeSlotsResponse> result = timeSlotServiceUnderTest.getTimeSlotsWithZeroBids(PageRequest.of(0, 1));
    }


}
