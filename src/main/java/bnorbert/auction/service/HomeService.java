package bnorbert.auction.service;

import bnorbert.auction.domain.Home;
import bnorbert.auction.exception.ResourceNotFoundException;
import bnorbert.auction.repository.HomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeService.class);
    private final HomeRepository homeRepository;

    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    public Home getHome(Long timeSlot_id) {
        LOGGER.info("Retrieving timeSlot {}", timeSlot_id);
        return homeRepository.findTopByTimeSlotId(timeSlot_id).orElseThrow(() ->
                new ResourceNotFoundException("TimeSlot" + timeSlot_id + " not found. #2"));
    }


}


