package bnorbert.auction.service;

import bnorbert.auction.domain.Bid;
import bnorbert.auction.domain.Home;
import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.domain.User;
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

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
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
    private UserService mockAuthService;
    @Mock
    private BidRepository mockBidRepository;
    @Mock
    private TimeSlotRepository mockTimeSlotRepository;

    private BidService bidServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        bidServiceUnderTest = new BidService(mockTimeSlotService, mockHomeService, mockBidMapper, mockAuthService, mockBidRepository, mockTimeSlotRepository);
    }

    @Test
    void testSave() {

        final BidDto bidDto = new BidDto();
        bidDto.setAmount(10.0);
        bidDto.setTimeSlotId(1L);

        when(mockAuthService.isLoggedIn()).thenReturn(false);


        final TimeSlot timeSlot = new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0));
        when(mockTimeSlotService.getTimeSlot(1L)).thenReturn(timeSlot);


        final Home home = new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 9.0);
        when(mockHomeService.getHome(1L)).thenReturn(home);

        when(mockAuthService.getCurrentUser()).thenReturn(new User());


        final Bid bid = new Bid();
        bid.setId(1L);
        bid.setAmount(10.0);
        bid.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0)));
        bid.setUser(new User());
        bid.setHome(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0));
        bid.setCreatedDate(Instant.ofEpochSecond(0L));
        when(mockBidMapper.map(any(BidDto.class), eq(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of
                (12, 0, 0), LocalTime.of(12, 0, 0))), eq(new User()), eq(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 9.0)))).thenReturn(bid);


        final Bid bid1 = new Bid();
        bid1.setId(1L);
        bid1.setAmount(20.0);
        bid1.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0)));
        bid1.setUser(new User());
        bid1.setHome(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 9.0));
        bid1.setCreatedDate(Instant.ofEpochSecond(0L));
        when(mockBidRepository.save(new Bid())).thenReturn(bid1);


        final TimeSlot timeSlot1 = new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0));
        when(mockTimeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0)))).thenReturn(timeSlot1);


        bidServiceUnderTest.save(bidDto);
    }

    @Test
    void testGetWinnerThenReturnResourceNotFoundException() {

        final Bid bid1 = new Bid();
        bid1.setId(1L);
        bid1.setAmount(10.0);
        bid1.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0)));
        bid1.setUser(new User());
        bid1.setHome(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0));
        bid1.setCreatedDate(Instant.ofEpochSecond(0L));
        final Optional<Bid> bid = Optional.of(bid1);
        when(mockBidRepository.findTop1ByTimeSlot_IdOrderByAmountDesc(1L)).thenReturn(bid);


        final BidResponse bidResponse = new BidResponse();
        bidResponse.setId(0L);
        bidResponse.setEmail("email");
        bidResponse.setAmount(0.0);
        bidResponse.setUserId(0L);
        bidResponse.setTimeSlotId(0L);
        bidResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        bidResponse.setStartTime(LocalTime.of(12, 0, 0));
        bidResponse.setEndTime(LocalTime.of(12, 0, 0));
        bidResponse.setHomeId(0L);
        bidResponse.setNeighborhood("neighborhood");
        when(mockBidMapper.mapToDto(new Bid())).thenReturn(bidResponse);


        final BidResponse result = bidServiceUnderTest.getWinner(0L);

    }


    @Test
    void testGetWinner() {

        final Bid bid1 = new Bid();
        bid1.setId(0L);
        bid1.setAmount(0.0);
        bid1.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0)));
        bid1.setUser(new User());
        bid1.setHome(new Home(0L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0));
        bid1.setCreatedDate(Instant.ofEpochSecond(0L));
        final Optional<Bid> bid = Optional.of(bid1);
        when(mockBidRepository.findTop1ByTimeSlot_IdOrderByAmountDesc(0L)).thenReturn(bid);


        final BidResponse bidResponse = new BidResponse();
        bidResponse.setId(0L);
        bidResponse.setEmail("email");
        bidResponse.setAmount(0.0);
        bidResponse.setUserId(0L);
        bidResponse.setTimeSlotId(0L);
        bidResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        bidResponse.setStartTime(LocalTime.of(12, 0, 0));
        bidResponse.setEndTime(LocalTime.of(12, 0, 0));
        bidResponse.setHomeId(0L);
        bidResponse.setNeighborhood("neighborhood");
        when(mockBidMapper.mapToDto(new Bid())).thenReturn(bidResponse);

        final BidResponse result = bidServiceUnderTest.getWinner(0L);


    }

    @Test
    void testGetBidsByTimeSlot() {

        final Bid bid = new Bid();
        bid.setId(0L);
        bid.setAmount(0.0);
        bid.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0)));
        bid.setUser(new User());
        bid.setHome(new Home(0L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0));
        bid.setCreatedDate(Instant.ofEpochSecond(0L));
        final Page<Bid> bids = new PageImpl<>(Collections.singletonList(bid));
        when(mockBidRepository.findBidsByTimeSlot_IdOrderByAmountDesc(eq(0L), any(Pageable.class))).thenReturn(bids);


        final GetBidsResponse getBidsResponse = new GetBidsResponse();
        getBidsResponse.setId(0L);
        getBidsResponse.setEmail("email");
        getBidsResponse.setAmount(0.0);
        getBidsResponse.setUserId(0L);
        getBidsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        getBidsResponse.setStartTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setEndTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setHomeId(0L);
        getBidsResponse.setNeighborhood("neighborhood");
        getBidsResponse.setKitchen(0);
        final List<GetBidsResponse> getBidsResponses = Collections.singletonList(getBidsResponse);
        when(mockBidMapper.entitiesToEntityDTOs(Collections.singletonList(new Bid()))).thenReturn(getBidsResponses);


        final Page<GetBidsResponse> result = bidServiceUnderTest.getBidsByTimeSlot(0L, PageRequest.of(0, 1));
        verify(mockBidRepository).findTop1ByTimeSlot_IdOrderByAmountDesc(anyLong());
        verify(mockBidMapper).mapToDto(any(Bid.class));
    }

    @Test
    void testGetBidsByHomeId() {

        final Bid bid = new Bid();
        bid.setId(0L);
        bid.setAmount(0.0);
        bid.setTimeSlot(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0, 0), LocalTime.of(12, 0, 0)));
        bid.setUser(new User());
        bid.setHome(new Home(0L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0));
        bid.setCreatedDate(Instant.ofEpochSecond(0L));
        final List<Bid> bids = Collections.singletonList(bid);
        when(mockBidRepository.findBidsByHome_IdOrderByAmountDesc(0L)).thenReturn(bids);

        final GetBidsResponse getBidsResponse = new GetBidsResponse();
        getBidsResponse.setId(0L);
        getBidsResponse.setEmail("email");
        getBidsResponse.setAmount(0.0);
        getBidsResponse.setUserId(0L);
        getBidsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        getBidsResponse.setStartTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setEndTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setHomeId(0L);
        getBidsResponse.setNeighborhood("neighborhood");
        getBidsResponse.setKitchen(0);
        when(mockBidMapper.mapToDto2(new Bid())).thenReturn(getBidsResponse);

        final List<GetBidsResponse> result = bidServiceUnderTest.getBidsByHomeId(0L);

    }
}
