package bnorbert.auction.service

import bnorbert.auction.domain.Bid
import bnorbert.auction.domain.Home
import bnorbert.auction.domain.TimeSlot
import bnorbert.auction.domain.User
import bnorbert.auction.mapper.BidMapper
import bnorbert.auction.repository.BidRepository
import bnorbert.auction.repository.TimeSlotRepository
import bnorbert.auction.transfer.bid.BidDto
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.slf4j.Logger

import java.time.DayOfWeek
import java.time.LocalTime
import static org.mockito.Mockito.*

class BidServiceDetailsTest {
    @Mock
    Logger LOGGER
    @Mock
    TimeSlotService timeSlotService
    @Mock
    HomeService homeService
    @Mock
    BidMapper bidMapper
    @Mock
    UserService authService
    @Mock
    BidRepository bidRepository
    @Mock
    TimeSlotRepository timeSlotRepository
    @InjectMocks
    BidService bidService

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test//the time must be between startTime and endTime to work //DayOfWeek.MONDAY
    void testSave() {
        when(timeSlotService.getTimeSlot(anyLong())).thenReturn(new TimeSlot(dayOfWeek: DayOfWeek.MONDAY, startTime: LocalTime.of
                (8, 00, 15),
                endTime: LocalTime.of(9, 49, 15), bids: [new Bid(amount: 0D)] as Set<Bid>))
        when(homeService.getHome(anyLong())).thenReturn(new Home(0L, null, 0, 0, null,
                0, 0, null, 0, 0, 0D))
        when(bidMapper.map(any(), any(), any(), any())).thenReturn(new Bid(amount: 1D))
        when(authService.getCurrentUser()).thenReturn(new User())
        when(authService.isLoggedIn()).thenReturn(true)

        bidService.save(new BidDto(amount: 1D, timeSlotId: 1l))
    }


    @Test
    void testBidHasToBeGreaterThanMaxBid() {
        when(timeSlotService.getTimeSlot(anyLong())).thenReturn(new TimeSlot(dayOfWeek: DayOfWeek.MONDAY, startTime: LocalTime.of
                (8, 0, 45),
                endTime: LocalTime.of(11, 0, 45), bids: [new Bid(amount: 40D)] as Set<Bid>))
        when(homeService.getHome(anyLong())).thenReturn(new Home(0L, null, 0, 0, null,
                0, 0, null, 0, 0, 0D))
        when(bidMapper.map(any(), any(), any(), any())).thenReturn(new Bid(amount: 30D))
        when(authService.getCurrentUser()).thenReturn(new User())
        when(authService.isLoggedIn()).thenReturn(true)

        bidService.save(new BidDto(amount: 30D, timeSlotId: 1L))
    }

    @Test
    void testBidBelowStartingPrice() {
        when(timeSlotService.getTimeSlot(anyLong())).thenReturn(new TimeSlot(dayOfWeek: DayOfWeek.MONDAY, startTime: LocalTime.of
                (8, 0, 45),
                endTime: LocalTime.of(11, 0, 45), bids: [new Bid(amount: 0D)] as Set<Bid>))
        when(homeService.getHome(anyLong())).thenReturn(new Home(0L, null, 0, 0, null,
                0, 0, null, 0, 0, 100000D))
        when(bidMapper.map(any(), any(), any(), any())).thenReturn(new Bid(amount: 20D))
        when(authService.getCurrentUser()).thenReturn(new User())
        when(authService.isLoggedIn()).thenReturn(true)

        bidService.save(new BidDto(amount: 20D, timeSlotId: 1L))
    }

    @Test
    void testTimeIssue() {
        when(timeSlotService.getTimeSlot(anyLong())).thenReturn(new TimeSlot(dayOfWeek: DayOfWeek.MONDAY, startTime: LocalTime.of
                (0, 0, 0),
                endTime: LocalTime.of(1, 0, 45), bids: [new Bid(amount: 0D)] as Set<Bid>))
        when(homeService.getHome(anyLong())).thenReturn(new Home(0L, null, 0, 0, null,
                0, 0, null, 0, 0, 0D))
        when(bidMapper.map(any(), any(), any(), any())).thenReturn(new Bid(amount: 0D))
        when(authService.getCurrentUser()).thenReturn(new User())
        when(authService.isLoggedIn()).thenReturn(true)

        bidService.save(new BidDto(amount: 0D, timeSlotId: 1L))
    }

}

