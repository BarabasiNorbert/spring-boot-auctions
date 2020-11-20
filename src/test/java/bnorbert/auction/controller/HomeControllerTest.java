package bnorbert.auction.controller;

import bnorbert.auction.service.HomeService;
import bnorbert.auction.transfer.home.HomeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class HomeControllerTest {

    @Mock
    private HomeService mockHomeService;

    private HomeController homeControllerUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        homeControllerUnderTest = new HomeController(mockHomeService);
    }

    @Test
    void testGetHomes() {

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
        final Page<HomeResponse> homeResponses = new PageImpl<>(Collections.singletonList(homeResponse));
        when(mockHomeService.getHomes(eq("neighborhood"), any(Pageable.class))).thenReturn(homeResponses);

        final ResponseEntity<Page<HomeResponse>> result = homeControllerUnderTest.getHomes("neighborhood",
                PageRequest.of(0, 1));


    }

    @Test
    void testGetHomesPageable() {

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
        final Page<HomeResponse> homeResponses = new PageImpl<>(Collections.singletonList(homeResponse));
        when(mockHomeService.getHomeForTimeslotId(1L)).thenReturn(homeResponses);


        final ResponseEntity<Page<HomeResponse>> result = homeControllerUnderTest.getHomesPageable(1L);
    }
}
