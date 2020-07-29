package bnorbert.auction.controller;

import bnorbert.auction.domain.Home;
import bnorbert.auction.domain.Room;
import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.domain.TimeTable;
import bnorbert.auction.service.TimeTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TimeTableControllerTest {

    @Mock
    private SolverManager<TimeTable, Long> mockSolverManager;
    @Mock
    private ScoreManager<TimeTable> mockScoreManager;
    @Mock
    private TimeTableService mockTimeTableService;

    private TimeTableController timeTableControllerUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        timeTableControllerUnderTest = new TimeTableController(mockSolverManager, mockScoreManager, mockTimeTableService);
    }

    @Test
    void testSolve() {
        when(mockSolverManager.solveAndListen(eq(1L), any(Function.class), any(Consumer.class))).thenReturn(null);

        final TimeTable timeTable = new TimeTable(Collections.singletonList(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0))), Arrays.asList(new Room("name")), Arrays.asList(new Home(0L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0)));
        when(mockTimeTableService.findById(1L)).thenReturn(timeTable);

        timeTableControllerUnderTest.solve();


        verify(mockTimeTableService).save(any(TimeTable.class));
    }

    @Test
    void testStopSolving() {
        timeTableControllerUnderTest.stopSolving();

        verify(mockSolverManager).terminateEarly(1L);
    }

    @Test
    void testGetTimeTable() {
        when(mockSolverManager.getSolverStatus(1L)).thenReturn(SolverStatus.SOLVING_SCHEDULED);

        final TimeTable timeTable = new TimeTable(Collections.singletonList(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0))), Arrays.asList(new Room("name")), Arrays.asList(new Home(0L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0)));
        when(mockTimeTableService.findById(1L)).thenReturn(timeTable);

        when(mockScoreManager.updateScore(any(TimeTable.class))).thenReturn(null);

        final TimeTable result = timeTableControllerUnderTest.getTimeTable();
    }

    @Test
    void testGetSolverStatus() {
        when(mockSolverManager.getSolverStatus(1L)).thenReturn(SolverStatus.SOLVING_SCHEDULED);

        final SolverStatus result = timeTableControllerUnderTest.getSolverStatus();

        assertThat(result).isEqualTo(SolverStatus.SOLVING_SCHEDULED);
    }
}
