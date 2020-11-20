package bnorbert.auction.service;

import bnorbert.auction.domain.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final UserService userService;
    private final BidRepository bidRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final AccountService accountService;

    public BidService(TimeSlotService timeSlotService, HomeService homeService, BidMapper bidMapper,
                      UserService userService, BidRepository bidRepository, TimeSlotRepository timeSlotRepository,
                      AccountService accountService) {
        this.timeSlotService = timeSlotService;
        this.homeService = homeService;
        this.bidMapper = bidMapper;
        this.userService = userService;
        this.bidRepository = bidRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.accountService = accountService;
    }

    @Transactional
    public void bidOnTheFirstHomeFromThatTimeslot(BidDto request) {
        LOGGER.info("Successfully completed " + LocalDateTime.now());
        if (userService.isLoggedIn()) {
            TimeSlot timeSlot = timeSlotService.getTimeSlot(request.getTimeSlotId());
            Home home = homeService.getHome(request.getTimeSlotId());
            User user = userService.getCurrentUser();
            Account account = accountService.getDueDiligence(userService.getCurrentUser().getId());

            bidSteps(request, timeSlot, home, user, account);
        }
    }

    private void bidSteps(BidDto request, TimeSlot timeSlot, Home home, User user, Account account) {
        LOGGER.info("Creating bid {}", request);
        if(currentTime().isAfter(timeSlot.getStartTime()) && currentTime().isBefore(timeSlot.getEndTime())
                && getCurrentDate().equals(timeSlot.getDayOfWeek()) ) {

            assureThatBidIsGreaterThanStartingPrice(request, home);

            assureThatBalanceIsNotLowerThanTheIntendedAmount(request, account);

            user.addTimeSlot(timeSlot); //#current-winner #mercurial
            user.addHome(home);

            Bid bid = bidMapper.map(request, timeSlot, userService.getCurrentUser(), home);

            assureThatBidIsGreaterThanMaxBid(request, timeSlot);

            bidRepository.save(bid);
            timeSlotRepository.save(timeSlot);

        }else throw new ResourceNotFoundException("Time issue " + timeSlot.getStartTime() + " - "
                + timeSlot.getEndTime() + " - "
                + timeSlot.getDayOfWeek());
    }

    private void assureThatBalanceIsNotLowerThanTheIntendedAmount(BidDto request, Account account) {
        if (account.getBalance() < request.getAmount()) {
            throw new ResourceNotFoundException("Account balance has to be greater than amount:"
                    + request.getAmount());
        }
    }

    private void assureThatBidIsGreaterThanStartingPrice(BidDto request, Home home) {
        if (request.getAmount() <= home.getStartingPrice()) {
            throw new ResourceNotFoundException("Starting price " + home.getStartingPrice() + " plus one is minimum minimorum");
        }
    }

    private void assureThatBidIsGreaterThanMaxBid(BidDto request, TimeSlot timeSlot) {
        Long maxBidding = getMaxBidding(timeSlot.getBids());
        if (maxBidding >= request.getAmount()) {
            throw new ResourceNotFoundException("Bid has to be greater than max bid: " + maxBidding);
        }
        //} else bidMapper.map(request, timeSlot, userService.getCurrentUser(), home);
    }

    private Long getMaxBidding(Set<Bid> bids) {
        long max = Long.MIN_VALUE;
        for(Bid bid : bids)
            max = Long.max(bid.getAmount(), max);
        return max;
    }

    private LocalTime currentTime() { return LocalTime.now(); }

    private LocalDate currentDate(){
        return LocalDate.now();
    }

    private DayOfWeek getCurrentDate(){
        return DayOfWeek.from(currentDate());
    }

    @Transactional
    public BidResponse getWinner(Long timeSlot_Id) {
       Bid bid = bidRepository.findTop1ByTimeSlot_IdOrderByAmountDesc(timeSlot_Id)
               .orElseThrow(() -> new ResourceNotFoundException
                       ("There is no timeSlot with id (" + timeSlot_Id + ")."));
       return bidMapper.mapToBidResponse(bid);
    }

    @Transactional
    public Page<BidResponse> getWinnerPageable(long timeSlot_Id) {
        Pageable pageable = PageRequest.of(0, 1);
        return bidRepository.findTop1ByTimeSlot_IdOrderByAmountDesc(timeSlot_Id, pageable).map(bidMapper::mapToBidResponse);
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
        return bids.stream().map(bidMapper::mapToBidsResponse).collect(toList());
    }


}
