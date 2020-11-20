package bnorbert.auction;

import bnorbert.auction.dataset.DatasetController;
import bnorbert.auction.domain.Home;
import bnorbert.auction.domain.Room;
import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.repository.HomeRepository;
import bnorbert.auction.repository.RoomRepository;
import bnorbert.auction.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@SpringBootApplication
public class AuctionApplication{ //implements CommandLineRunner {

	//Step #1. Insert data from homes.txt
/*
	private static final Logger LOGGER = LoggerFactory.getLogger(AuctionApplication.class);
	private final HomeRepository homeRepository;

	public AuctionApplication(HomeRepository homeRepository) {
		this.homeRepository = homeRepository;
	}
 */
	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class, args);


	}

/*
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
 */

	/*

	//Step #2. Insert time and space then hit solve. Make changes as needed.
	@Value("${timeTable.demoData:SMALL}")
	private DemoData demoData;

	@Bean
	public CommandLineRunner demoData(
			TimeSlotRepository timeSlotRepository,
			RoomRepository roomRepository,
			HomeRepository homeRepositoryy) {
		return (args) -> {
			if (demoData == DemoData.NONE) {
				return;

			}
			timeSlotRepository.save(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(13, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(15, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(15, 0), LocalTime.of(16, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(16, 0), LocalTime.of(17, 0)));

			timeSlotRepository.save(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(9, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(12, 0), LocalTime.of(13, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(15, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(15, 0), LocalTime.of(16, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(16, 0), LocalTime.of(17, 0)));

			timeSlotRepository.save(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(9, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(12, 0), LocalTime.of(13, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(15, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(15, 0), LocalTime.of(16, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(16, 0), LocalTime.of(17,0)));

			timeSlotRepository.save(new TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(9, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(12, 0), LocalTime.of(13, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(15, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(15, 0), LocalTime.of(16, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(16, 0), LocalTime.of(17, 0)));

			timeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(9, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(12, 0), LocalTime.of(13, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(15, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(15, 0), LocalTime.of(16, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(16, 0), LocalTime.of(17, 0)));

			timeSlotRepository.save(new TimeSlot(DayOfWeek.SATURDAY, LocalTime.of(8, 0), LocalTime.of(9, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SATURDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SATURDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SATURDAY, LocalTime.of(12, 0), LocalTime.of(13, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SATURDAY, LocalTime.of(14, 0), LocalTime.of(15, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SATURDAY, LocalTime.of(15, 0), LocalTime.of(16, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SATURDAY, LocalTime.of(16, 0), LocalTime.of(17, 0)));

			timeSlotRepository.save(new TimeSlot(DayOfWeek.SUNDAY, LocalTime.of(8, 0), LocalTime.of(9, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SUNDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SUNDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SUNDAY, LocalTime.of(11, 0), LocalTime.of(12, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SUNDAY, LocalTime.of(12, 0), LocalTime.of(13, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SUNDAY, LocalTime.of(14, 0), LocalTime.of(15, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SUNDAY, LocalTime.of(15, 0), LocalTime.of(16, 0)));
			timeSlotRepository.save(new TimeSlot(DayOfWeek.SUNDAY, LocalTime.of(16, 0), LocalTime.of(17, 0)));

			roomRepository.save(new Room("A1"));
			roomRepository.save(new Room("B3"));



		};
	}

	public enum DemoData {
		NONE,
		SMALL,
		LARGE
	}


	 */


}
