package bnorbert.auction.service;

import bnorbert.auction.domain.Home;
import bnorbert.auction.exception.ResourceNotFoundException;
import bnorbert.auction.mapper.HomeMapper;
import bnorbert.auction.repository.HomeRepository;
import bnorbert.auction.transfer.home.HomeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HomeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeService.class);
    private final HomeRepository homeRepository;
    private final HomeMapper homeMapper;

    public HomeService(HomeRepository homeRepository, HomeMapper homeMapper) {
        this.homeRepository = homeRepository;
        this.homeMapper = homeMapper;
    }

    public Home getHome(Long timeSlot_id) {
        LOGGER.info("Retrieving timeSlot {}", timeSlot_id);
        return homeRepository.findTopByTimeSlotId(timeSlot_id).orElseThrow(() ->
                new ResourceNotFoundException("TimeSlot" + timeSlot_id + " not found. #2"));
    }

    @Transactional
    public Page<HomeResponse> getHomes(String partialName, Pageable pageable){
        Page<Home> homes;
        if (partialName != null) {
            homes = homeRepository.findByNeighborhoodContaining(partialName, pageable);
        }else homes = homeRepository.findAll(pageable);
        List<HomeResponse> homeResponses = homeMapper.entitiesToEntityDTOs(homes.getContent());
        return new PageImpl<>(homeResponses, pageable, homes.getTotalElements());
    }


}


