package bnorbert.auction;

import bnorbert.auction.dataset.DatasetController;
import bnorbert.auction.domain.Home;
import bnorbert.auction.repository.HomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@SpringBootApplication
public class AuctionApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuctionApplication.class);
	private final HomeRepository homeRepository;

	public AuctionApplication(HomeRepository homeRepository) {
		this.homeRepository = homeRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {

		LOGGER.info("ForeclosedHomesApplication....");

		DatasetController datasetController = new DatasetController();
		List<Home> homeList = new ArrayList<>();

		datasetController.getHomes().getHomes().forEach((key, home) -> {
			if (!homeRepository.existsById(key)) {
				homeList.add(home);
			}
		});

		homeRepository.saveAll(homeList);

	}

}
