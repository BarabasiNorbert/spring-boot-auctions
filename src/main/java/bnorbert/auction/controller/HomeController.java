package bnorbert.auction.controller;

import bnorbert.auction.service.HomeService;
import bnorbert.auction.transfer.home.HomeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
