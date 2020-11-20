package bnorbert.auction.service;

import bnorbert.auction.domain.Home;
import bnorbert.auction.domain.Room;
import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.domain.TimeTable;
import bnorbert.auction.repository.HomeRepository;
import bnorbert.auction.repository.RoomRepository;
import bnorbert.auction.repository.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TimeTableServiceTest {

    @Mock
    private TimeSlotRepository mockTimeSlotRepository;
    @Mock
    private RoomRepository mockRoomRepository;
    @Mock
    private HomeRepository mockHomeRepository;

    private TimeTableService timeTableServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        timeTableServiceUnderTest = new TimeTableService(mockTimeSlotRepository, mockRoomRepository, mockHomeRepository);
    }

    @Test
    void testFindById() {

        final List<TimeSlot> timeSlots = Collections.singletonList(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(10, 0, 0),
                LocalTime.of(12, 0, 0)));
        when(mockTimeSlotRepository.findAll()).thenReturn(timeSlots);

        when(mockRoomRepository.findAll()).thenReturn(Collections.singletonList(new Room("name")));

        final List<Home> homes = Collections.singletonList(new Home(1L, "neighborhood", 0, 0, "yearBuilt",
                0, 0, "garageYearBuilt", 0, 0, 1000000));
        when(mockHomeRepository.findAll()).thenReturn(homes);

        final TimeTable result = timeTableServiceUnderTest.findById(1L);
    }

    @Test
    void testFindByIdThenThrowIllegalStateException() {


        final List<TimeSlot> timeSlots = Collections.singletonList(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(10, 0, 0),
                LocalTime.of(12, 0, 0)));
        when(mockTimeSlotRepository.findAll()).thenReturn(timeSlots);

        when(mockRoomRepository.findAll()).thenReturn(Collections.singletonList(new Room("name")));

        final List<Home> homes = Collections.singletonList(new Home(0L, "neighborhood", 0, 0, "yearBuilt",
                0, 0, "garageYearBuilt", 0, 0, 1000000));
        when(mockHomeRepository.findAll()).thenReturn(homes);


        final TimeTable result = timeTableServiceUnderTest.findById(0L);
    }

    @Test
    void testSave() {

        final TimeTable timeTable = new TimeTable(Collections.singletonList(
                new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(10, 0, 0),
                        LocalTime.of(12, 0, 0))),
                Collections.singletonList(new Room("name")),
                Collections.singletonList(new Home(0L, "neighborhood", 0, 0, "yearBuilt",
                        0, 0, "garageYearBuilt", 0, 0, 50000)));

        final Home home = new Home(1L, "neighborhood", 0, 0, "yearBuilt",
                0, 0, "garageYearBuilt", 0, 0, 50000);
        when(mockHomeRepository.save(new Home(1L, "neighborhood", 0, 0, "yearBuilt",
                0, 0, "garageYearBuilt", 0, 0, 50000))).thenReturn(home);

        timeTableServiceUnderTest.save(timeTable);

    }


}
