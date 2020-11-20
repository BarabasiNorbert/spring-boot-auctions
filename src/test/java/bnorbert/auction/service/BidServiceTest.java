package bnorbert.auction.service;

import bnorbert.auction.domain.*;
import bnorbert.auction.mapper.BidMapper;
import bnorbert.auction.repository.BidRepository;
import bnorbert.auction.repository.TimeSlotRepository;
import bnorbert.auction.transfer.bid.BidDto;
import bnorbert.auction.transfer.bid.BidResponse;
import bnorbert.auction.transfer.bid.GetBidsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BidServiceTest {


    @Mock
    private TimeSlotService mockTimeSlotService;
    @Mock
    private HomeService mockHomeService;
    @Mock
    private BidMapper mockBidMapper;
    @Mock
    private UserService mockUserService;
    @Mock
    private BidRepository mockBidRepository;
    @Mock
    private TimeSlotRepository mockTimeSlotRepository;
    @Mock
    private AccountService mockAccountService;


    private BidService bidServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        bidServiceUnderTest = new BidService(mockTimeSlotService, mockHomeService, mockBidMapper, mockUserService, mockBidRepository, mockTimeSlotRepository, mockAccountService);
    }

    @Test
    void testCreateBidOnTheFirstHomeFromThatTimeslot() {

        BidDto request = new BidDto();
        request.setAmount(500000);
        request.setTimeSlotId(1L);

        when(mockUserService.isLoggedIn()).thenReturn(true);

        User user = new User();
        user.setId(1L);

        Account account = new Account();
        account.setId("iban");
        account.setBalance(900000L);
        account.setFirstName("firstName");
        account.setLastName("lastName");
        account.setUser(user);
        account.setTransactionCount(1);
        when(mockAccountService.getDueDiligence(1L)).thenReturn(account);


        TimeSlot timeSlot = new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0, 0),
                LocalTime.of(12, 0, 0));
        when(mockTimeSlotService.getTimeSlot(1L)).thenReturn(timeSlot);

        Home home = new Home
                (1L, "neighborhood", 1, 8006, "yearBuilt", 1, 1,
                        "garageYearBuilt", 2, 567, 100000);
        when(mockHomeService.getHome(1L)).thenReturn(home);

        when(mockUserService.getCurrentUser()).thenReturn(user);

        Bid bid = new Bid();
        bid.setId(1L);
        bid.setAmount(500000);
        bid.setTimeSlot(timeSlot);
        bid.setUser(user);
        bid.setHome(home);

        bid.setCreatedDate(Instant.now());
        when(mockBidMapper.map(any(BidDto.class), any(TimeSlot.class), eq(user),
                eq(home))).thenReturn(bid);

        when(mockTimeSlotRepository.save(any(TimeSlot.class))).thenReturn(timeSlot);

        bidServiceUnderTest.bidOnTheFirstHomeFromThatTimeslot(request);
    }

    @Test
    void testCreateBidOnTheFirstHomeFromThatTimeslotBelowStartingPrice() {

        BidDto request = new BidDto();
        request.setAmount(500000);
        request.setTimeSlotId(1L);

        when(mockUserService.isLoggedIn()).thenReturn(true);

        User user = new User();
        user.setId(1L);

        TimeSlot timeSlot = new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0, 0),
                LocalTime.of(12, 0, 0));
        when(mockTimeSlotService.getTimeSlot(1L)).thenReturn(timeSlot);

        Home home = new Home
                (1L, "neighborhood", 1, 0, "yearBuilt", 1, 1,
                        "garageYearBuilt", 2, 0, 1000000);
        when(mockHomeService.getHome(1L)).thenReturn(home);

        when(mockUserService.getCurrentUser()).thenReturn(user);

        Bid bid = new Bid();
        bid.setId(1L);
        bid.setAmount(500000);
        bid.setTimeSlot(timeSlot);
        bid.setUser(user);
        bid.setHome(home);

        bid.setCreatedDate(Instant.now());
        when(mockBidMapper.map(any(BidDto.class), any(TimeSlot.class), eq(user),
                eq(home))).thenReturn(bid);

        when(mockTimeSlotRepository.save(any(TimeSlot.class))).thenReturn(timeSlot);

        bidServiceUnderTest.bidOnTheFirstHomeFromThatTimeslot(request);
    }

    @Test
    void testGetWinner() {

        User user = new User();
        user.setId(1L);

        Home home = new Home
                (1L, "neighborhood", 1, 0, "yearBuilt", 1, 1,
                        "garageYearBuilt", 2, 0, 1);
        when(mockHomeService.getHome(1L)).thenReturn(home);

        Bid bid1 = new Bid();
        bid1.setId(1L);
        bid1.setAmount(40000);
        bid1.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(6, 0, 0),
                LocalTime.of(12, 0, 0)));
        bid1.setUser(new User());
        bid1.setHome(home);
        bid1.setCreatedDate(Instant.now());
        final Optional<Bid> bid = Optional.of(bid1);
        when(mockBidRepository.findTop1ByTimeSlot_IdOrderByAmountDesc(1L)).thenReturn(bid);

        final BidResponse bidResponse = new BidResponse();
        bidResponse.setId(1L);
        bidResponse.setEmail("email");
        bidResponse.setAmount(5000);
        bidResponse.setUserId(user.getId());
        bidResponse.setTimeSlotId(1L);
        bidResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        bidResponse.setStartTime(LocalTime.of(6, 0, 0));
        bidResponse.setEndTime(LocalTime.of(12, 0, 0));
        bidResponse.setHomeId(home.getId());
        bidResponse.setNeighborhood("neighborhood");
        when(mockBidMapper.mapToBidResponse(any(Bid.class))).thenReturn(bidResponse);

        final BidResponse result = bidServiceUnderTest.getWinner(1L);

    }

    @Test
    void testGetBidsByTimeSlot() {

        User user = new User();
        user.setId(1L);

        Home home = new Home
                (1L, "neighborhood", 1, 0, "yearBuilt", 1, 1,
                        "garageYearBuilt", 2, 0, 1);
        when(mockHomeService.getHome(1L)).thenReturn(home);

        final Bid bid = new Bid();
        bid.setId(1L);
        bid.setAmount(2);
        bid.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(11, 0, 0),
                LocalTime.of(12, 0, 0)));
        bid.setUser(user);
        bid.setHome(home);
        bid.setCreatedDate(Instant.ofEpochSecond(1L));
        final Page<Bid> bids = new PageImpl<>(Collections.singletonList(bid));
        when(mockBidRepository.findBidsByTimeSlot_IdOrderByAmountDesc(eq(1L), any(Pageable.class))).thenReturn(bids);

        final GetBidsResponse getBidsResponse = new GetBidsResponse();
        getBidsResponse.setId(1L);
        getBidsResponse.setEmail("email");
        getBidsResponse.setAmount(bid.getAmount());
        getBidsResponse.setUserId(user.getId());
        getBidsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        getBidsResponse.setStartTime(LocalTime.of(11, 0, 0));
        getBidsResponse.setEndTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setHomeId(home.getId());
        getBidsResponse.setNeighborhood("neighborhood");
        getBidsResponse.setKitchen(1);
        final List<GetBidsResponse> getBidsResponses = Collections.singletonList(getBidsResponse);
        when(mockBidMapper.entitiesToEntityDTOs(Collections.singletonList(bid))).thenReturn(getBidsResponses);

        final Page<GetBidsResponse> result = bidServiceUnderTest.getBidsByTimeSlot(1L, PageRequest.of(0, 1));
    }

    @Test
    void testGetBidsByHomeId() {

        User user = new User();
        user.setId(1L);

        Home home = new Home
                (1L, "neighborhood", 1, 0, "yearBuilt", 1, 1,
                        "garageYearBuilt", 2, 0, 1);
        when(mockHomeService.getHome(1L)).thenReturn(home);

        final Bid bid = new Bid();
        bid.setId(1L);
        bid.setAmount(500);
        bid.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(10, 0, 0),
                LocalTime.of(12, 0, 0)));
        bid.setUser(user);
        bid.setHome(home);
        bid.setCreatedDate(Instant.ofEpochSecond(1L));
        final List<Bid> bids = Collections.singletonList(bid);
        when(mockBidRepository.findBidsByHome_IdOrderByAmountDesc(home.getId())).thenReturn(bids);


        final GetBidsResponse getBidsResponse = new GetBidsResponse();
        getBidsResponse.setId(bid.getId());
        getBidsResponse.setEmail("email");
        getBidsResponse.setAmount(bid.getAmount());
        getBidsResponse.setUserId(user.getId());
        getBidsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        getBidsResponse.setStartTime(LocalTime.of(10, 0, 0));
        getBidsResponse.setEndTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setHomeId(home.getId());
        getBidsResponse.setNeighborhood("neighborhood");
        getBidsResponse.setKitchen(1);
        when(mockBidMapper.mapToBidsResponse(any(Bid.class))).thenReturn(getBidsResponse);

        final List<GetBidsResponse> result = bidServiceUnderTest.getBidsByHomeId(home.getId());
    }


    @Test
    void testGetWinnerPageable() {

        User user = new User();
        user.setId(1L);

        Home home = new Home
                (1L, "neighborhood", 1, 0, "yearBuilt", 1, 1,
                        "garageYearBuilt", 2, 0, 1);
        when(mockHomeService.getHome(1L)).thenReturn(home);


        final Bid bid = new Bid();
        bid.setId(1L);
        bid.setAmount(2);
        bid.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(11, 0, 0),
                LocalTime.of(12, 0, 0)));
        bid.setUser(user);
        bid.setHome(home);
        bid.setCreatedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)
                .toInstant(ZoneOffset.UTC));
        final Page<Bid> bids = new PageImpl<>(Collections.singletonList(bid));
        when(mockBidRepository.findTop1ByTimeSlot_IdOrderByAmountDesc(eq(1L), any(Pageable.class))).thenReturn(bids);

        final BidResponse bidResponse = new BidResponse();
        bidResponse.setId(bid.getId());
        bidResponse.setEmail("email");
        bidResponse.setAmount(bid.getAmount());
        bidResponse.setUserId(user.getId());
        bidResponse.setTimeSlotId(1L);
        bidResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        bidResponse.setStartTime(LocalTime.of(11, 0, 0));
        bidResponse.setEndTime(LocalTime.of(12, 0, 0));
        bidResponse.setHomeId(home.getId());
        bidResponse.setNeighborhood("neighborhood");
        when(mockBidMapper.mapToBidResponse(any(Bid.class))).thenReturn(bidResponse);

        final Page<BidResponse> result = bidServiceUnderTest.getWinnerPageable(1L);
    }



}


