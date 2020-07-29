package bnorbert.auction.controller;

import bnorbert.auction.service.BidService;
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
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BitControllerTest {

    @Mock
    private BidService mockBidService;

    private BitController bitControllerUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        bitControllerUnderTest = new BitController(mockBidService);
    }

    @Test
    void testCreateBid() {

        final BidDto bidDto = new BidDto();
        bidDto.setAmount(1000.0);
        bidDto.setTimeSlotId(1L);

        final ResponseEntity<Void> result = bitControllerUnderTest.createBid(bidDto);


        verify(mockBidService).save(any(BidDto.class));
    }

    @Test
    void testGetWinner() {

        final BidResponse bidResponse = new BidResponse();
        bidResponse.setId(1L);
        bidResponse.setEmail("email@gmail.com");
        bidResponse.setAmount(30000.0);
        bidResponse.setUserId(1L);
        bidResponse.setTimeSlotId(1L);
        bidResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        bidResponse.setStartTime(LocalTime.of(12, 0, 0));
        bidResponse.setEndTime(LocalTime.of(12, 0, 0));
        bidResponse.setHomeId(2L);
        bidResponse.setNeighborhood("neighborhood");
        when(mockBidService.getWinner(1L)).thenReturn(bidResponse);

        final ResponseEntity<BidResponse> result = bitControllerUnderTest.getWinner(1L);

    }

    @Test
    void testGetBids() {

        final GetBidsResponse getBidsResponse = new GetBidsResponse();
        getBidsResponse.setId(1L);
        getBidsResponse.setEmail("email");
        getBidsResponse.setAmount(3000.0);
        getBidsResponse.setUserId(3L);
        getBidsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        getBidsResponse.setStartTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setEndTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setHomeId(2L);
        getBidsResponse.setNeighborhood("neighborhood");
        getBidsResponse.setKitchen(0);
        final Page<GetBidsResponse> getBidsResponses = new PageImpl<>(Collections.singletonList(getBidsResponse));
        when(mockBidService.getBidsByTimeSlot(eq(1L), any(Pageable.class))).thenReturn(getBidsResponses);


        final ResponseEntity<Page<GetBidsResponse>> result = bitControllerUnderTest.getBids(0L, PageRequest.of(0, 1));

    }

    @Test
    void testGetBidsByHomeId() {

        final GetBidsResponse getBidsResponse = new GetBidsResponse();
        getBidsResponse.setId(1L);
        getBidsResponse.setEmail("email");
        getBidsResponse.setAmount(200.0);
        getBidsResponse.setUserId(1L);
        getBidsResponse.setDayOfWeek(DayOfWeek.FRIDAY);
        getBidsResponse.setStartTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setEndTime(LocalTime.of(12, 0, 0));
        getBidsResponse.setHomeId(1L);
        getBidsResponse.setNeighborhood("neighborhood");
        getBidsResponse.setKitchen(0);
        final List<GetBidsResponse> getBidsResponses = Collections.singletonList(getBidsResponse);
        when(mockBidService.getBidsByHomeId(1L)).thenReturn(getBidsResponses);


        final ResponseEntity<List<GetBidsResponse>> result = bitControllerUnderTest.getBidsByHomeId(1L);

    }
}
