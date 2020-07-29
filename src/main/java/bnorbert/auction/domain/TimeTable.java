package bnorbert.auction.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolverStatus;

import java.util.List;

@PlanningSolution
public class TimeTable {

    @ProblemFactProperty
    @ValueRangeProvider(id = "timeSlotRange")
    private List<TimeSlot> timeslotList;

    @ProblemFactProperty
    @ValueRangeProvider(id = "roomRange")
    private List<Room> roomList;

    @PlanningEntityCollectionProperty
    private List<Home> homeList;

    private SolverStatus solverStatus;
    @PlanningScore
    private HardSoftScore score;

    public TimeTable() {
    }

    public TimeTable(List<TimeSlot> timeslotList, List<Room> roomList, List<Home> homeList) {
        this.timeslotList = timeslotList;
        this.roomList = roomList;
        this.homeList = homeList;
    }

    public List<TimeSlot> getTimeslotList() {
        return timeslotList;
    }

    public void setTimeslotList(List<TimeSlot> timeslotList) {
        this.timeslotList = timeslotList;
    }


    public List<Home> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<Home> homeList) {
        this.homeList = homeList;
    }

    public SolverStatus getSolverStatus() {
        return solverStatus;
    }

    public void setSolverStatus(SolverStatus solverStatus) {
        this.solverStatus = solverStatus;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
}
