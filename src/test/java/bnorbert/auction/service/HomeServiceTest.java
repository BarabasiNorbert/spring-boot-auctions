package bnorbert.auction.service;

import bnorbert.auction.domain.Home;
import bnorbert.auction.mapper.HomeMapper;
import bnorbert.auction.repository.HomeRepository;
import bnorbert.auction.transfer.home.HomeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class HomeServiceTest {

    @Mock
    private HomeRepository mockHomeRepository;
    @Mock
    private HomeMapper mockHomeMapper;

    private HomeService homeServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        homeServiceUnderTest = new HomeService(mockHomeRepository, mockHomeMapper);
    }

    @Test
    void testGetHome() {

        final Home expectedResult = new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0);

        final Optional<Home> home = Optional.of(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0));
        when(mockHomeRepository.findTopByTimeSlotId(1L)).thenReturn(home);


        final Home result = homeServiceUnderTest.getHome(1L);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetHomeThenReturnResourceNotFound() {

        final Home expectedResult = new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0);

        final Optional<Home> home = Optional.of(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0));
        when(mockHomeRepository.findTopByTimeSlotId(1L)).thenReturn(home);


        final Home result = homeServiceUnderTest.getHome(2L);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetHomes() {

        final Page<Home> homes = new PageImpl<>(Collections.singletonList(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0)));
        when(mockHomeRepository.findByNeighborhoodContaining(eq("neighborhood"), any(Pageable.class))).thenReturn(homes);


        final Page<Home> homes1 = new PageImpl<>(Collections.singletonList(new Home(2L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0)));
        when(mockHomeRepository.findAll(any(Pageable.class))).thenReturn(homes1);


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
        final List<HomeResponse> homeResponses = Collections.singletonList(homeResponse);
        when(mockHomeMapper.entitiesToEntityDTOs(Collections.singletonList(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0)))).thenReturn(homeResponses);


        final Page<HomeResponse> result = homeServiceUnderTest.getHomes("neighborhood", PageRequest.of(0, 1));

    }

    @Test
    void testGetHomesWithoutPartialName() {

        final Page<Home> homes = new PageImpl<>(Collections.singletonList(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0)));
        when(mockHomeRepository.findByNeighborhoodContaining(eq("neighborhood"), any(Pageable.class))).thenReturn(homes);


        final Page<Home> homes1 = new PageImpl<>(Collections.singletonList(new Home(2L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0)));
        when(mockHomeRepository.findAll(any(Pageable.class))).thenReturn(homes1);


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
        final List<HomeResponse> homeResponses = Collections.singletonList(homeResponse);
        when(mockHomeMapper.entitiesToEntityDTOs(Collections.singletonList(new Home(1L, "neighborhood", 0, 0, "yearBuilt", 0, 0, "garageYearBuilt", 0, 0, 0.0)))).thenReturn(homeResponses);


        final Page<HomeResponse> result = homeServiceUnderTest.getHomes(null , PageRequest.of(0, 1));

    }


}
