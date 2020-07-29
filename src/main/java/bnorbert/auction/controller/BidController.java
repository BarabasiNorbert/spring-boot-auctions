package bnorbert.auction.controller;

import bnorbert.auction.service.BidService;
import bnorbert.auction.transfer.bid.BidDto;
import bnorbert.auction.transfer.bid.BidResponse;
import bnorbert.auction.transfer.bid.GetBidsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@CrossOrigin
@RestController
@RequestMapping("/bids")
public class BidController {
    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping
    public ResponseEntity<Void> createBid(@RequestBody BidDto bidDto) {
        bidService.save(bidDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/getWinner{timeSlot_Id}")
    public ResponseEntity<BidResponse> getWinner(@PathVariable Long timeSlot_Id) {
        return status(HttpStatus.OK).body(bidService.getWinner(timeSlot_Id));
    }

    @GetMapping("/getBids")
    public ResponseEntity<Page<GetBidsResponse>> getBids(
            Long timeSlot_id, Pageable pageable) {
        Page<GetBidsResponse> bids = bidService.getBidsByTimeSlot(timeSlot_id, pageable);
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    @GetMapping("getBidsByHomeId/{home_id}")
    public ResponseEntity<List<GetBidsResponse>> getBidsByHomeId(@PathVariable long home_id) {
        return status(HttpStatus.OK).body(bidService.getBidsByHomeId(home_id));
    }


}
