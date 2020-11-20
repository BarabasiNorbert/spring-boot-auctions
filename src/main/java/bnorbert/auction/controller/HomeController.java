package bnorbert.auction.controller;

import bnorbert.auction.service.HomeService;
import bnorbert.auction.transfer.home.HomeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@CrossOrigin
@RestController
@RequestMapping("/homes")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/getHomes")
    public ResponseEntity<Page<HomeResponse>> getHomes(String partialName,
            Pageable pageable) {
        Page<HomeResponse> timeSlot = homeService.getHomes(partialName, pageable);
        return new ResponseEntity<>(timeSlot, HttpStatus.OK);
    }

    @GetMapping("/getWinnerPageable{timeSlot_Id}")
    public ResponseEntity<Page<HomeResponse>> getHomesPageable(@PathVariable Long timeSlot_Id) {
        return status(HttpStatus.OK).body(homeService.getHomeForTimeslotId(timeSlot_Id));
    }
}
