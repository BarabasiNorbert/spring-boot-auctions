package bnorbert.auction.service;

import bnorbert.auction.domain.Home;
import bnorbert.auction.domain.TimeTable;
import bnorbert.auction.repository.HomeRepository;
import bnorbert.auction.repository.RoomRepository;
import bnorbert.auction.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimeTableService {

    public static final Long SINGLETON_TIME_TABLE_ID = 1L;

    private final TimeSlotRepository timeSlotRepository;
    private final RoomRepository roomRepository;
    private final HomeRepository homeRepository;

    public TimeTableService(TimeSlotRepository timeSlotRepository, RoomRepository roomRepository, HomeRepository homeRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.roomRepository = roomRepository;
        this.homeRepository = homeRepository;
    }

    @Transactional
    public TimeTable findById(Long id) {
        if (!SINGLETON_TIME_TABLE_ID.equals(id)) {
            throw new IllegalStateException("There is no timeTable with id (" + id + ").");
        }
        return new TimeTable(
                timeSlotRepository.findAll(),
                roomRepository.findAll(),
                homeRepository.findAll());
    }

    public void save(TimeTable timeTable) {
        for (Home home : timeTable.getHomeList()) {
            homeRepository.save(home);
        }
    }


}