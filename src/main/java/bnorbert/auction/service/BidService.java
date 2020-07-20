package bnorbert.auction.service;

import bnorbert.auction.domain.Bid;
import bnorbert.auction.domain.Home;
import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.exception.ResourceNotFoundException;
import bnorbert.auction.mapper.BidMapper;
import bnorbert.auction.repository.BidRepository;
import bnorbert.auction.repository.TimeSlotRepository;
import bnorbert.auction.transfer.bid.BidDto;
import bnorbert.auction.transfer.bid.BidResponse;
import bnorbert.auction.transfer.bid.GetBidsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
public class BidService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BidService.class);

    private final TimeSlotService timeSlotService;
    private final HomeService homeService;
    private final BidMapper bidMapper;
    private final UserService authService;
    private final BidRepository bidRepository;
    private final TimeSlotRepository timeSlotRepository;

    public BidService(TimeSlotService timeSlotService, HomeService homeService, BidMapper bidMapper, UserService authService,
                      BidRepository bidRepository, TimeSlotRepository timeSlotRepository) {
        this.timeSlotService = timeSlotService;
        this.homeService = homeService;
        this.bidMapper = bidMapper;
        this.authService = authService;
        this.bidRepository = bidRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Transactional
    public void save(BidDto bidDto) {

        if (authService.isLoggedIn()) {
            TimeSlot timeSlot = timeSlotService.getTimeSlot(bidDto.getTimeSlotId());

            Home home = homeService.getHome(bidDto.getTimeSlotId());

            if(currentTime().isAfter(timeSlot.getStartTime()) && currentTime().isBefore(timeSlot.getEndTime())
                    && getCurrentDate().equals(timeSlot.getDayOfWeek()) ) {

                if (bidDto.getAmount() <= home.getStartingPrice()) {
                    throw new ResourceNotFoundException("Starting price: " + home.getStartingPrice() + "is minimum minimorum");
                }

                authService.getCurrentUser().addTimeSlot(timeSlot);

                Bid bid = bidMapper.map(bidDto, timeSlot, authService.getCurrentUser(), home);

                Double maxBidding = getMaxBidding(timeSlot.getBids());
                if (maxBidding >= bidDto.getAmount()) {
                    throw new ResourceNotFoundException("Bid has to be greater than max bid: " + maxBidding);
                } else bidMapper.map(bidDto, timeSlot, authService.getCurrentUser(), home);

                bidRepository.save(bid);
                timeSlotRepository.save(timeSlot);

            }else throw new ResourceNotFoundException("Time issue " + timeSlot.getStartTime() + " - "
                    + timeSlot.getEndTime() + " - "
                    + timeSlot.getDayOfWeek());
        }
    }

    private LocalTime currentTime() { return LocalTime.now(); }

    private LocalDate currentDate(){
        return LocalDate.now();
    }

    private DayOfWeek getCurrentDate(){
        return DayOfWeek.from(currentDate());
    }

    private Double getMaxBidding(Set<Bid> bids) {
        double max = Double.MIN_VALUE;
        for(Bid bid : bids)
            max = Double.max(bid.getAmount(),max);
        return max;
    }

    @Transactional
    public BidResponse getWinner(Long timeSlot_Id) {
       Bid bid = bidRepository.findTop1ByTimeSlot_IdOrderByAmountDesc(timeSlot_Id)
               .orElseThrow(() -> new ResourceNotFoundException
                       ("There is no timeSlot with id (" + timeSlot_Id + ")."));
       return bidMapper.mapToDto(bid);
    }

    @Transactional
    public Page<GetBidsResponse> getBidsByTimeSlot(Long timeSlot_id, Pageable pageable){
        Page<Bid> bids = bidRepository.findBidsByTimeSlot_IdOrderByAmountDesc(timeSlot_id, pageable);
        List<GetBidsResponse> getBidsResponses = bidMapper.entitiesToEntityDTOs(bids.getContent());
        return new PageImpl<>(getBidsResponses, pageable, bids.getTotalElements());
    }

    @Transactional
    public List<GetBidsResponse> getBidsByHomeId(long home_id) {
        List<Bid> bids = bidRepository.findBidsByHome_IdOrderByAmountDesc(home_id);
        return bids.stream().map(bidMapper::mapToDto2).collect(toList());
    }


}
