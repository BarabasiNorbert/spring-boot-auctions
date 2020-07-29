package bnorbert.auction.controller;



import bnorbert.auction.domain.TimeTable;
import bnorbert.auction.service.TimeTableService;
import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeTable")
public class TimeTableController {

    private final SolverManager<TimeTable, Long> solverManager;
    private final ScoreManager<TimeTable> scoreManager;
    private final TimeTableService timeTableService;

    public TimeTableController(SolverManager<TimeTable, Long> solverManager, ScoreManager<TimeTable> scoreManager, TimeTableService timeTableService) {
        this.solverManager = solverManager;
        this.scoreManager = scoreManager;
        this.timeTableService = timeTableService;
    }

    @PostMapping("/solve")
    public void solve() {
        solverManager.solveAndListen(TimeTableService.SINGLETON_TIME_TABLE_ID,
                timeTableService::findById,
                timeTableService::save);
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(TimeTableService.SINGLETON_TIME_TABLE_ID);
    }

    @GetMapping()
    public TimeTable getTimeTable() {
        SolverStatus solverStatus = getSolverStatus();
        TimeTable solution = timeTableService.findById(TimeTableService.SINGLETON_TIME_TABLE_ID);
        scoreManager.updateScore(solution);
        solution.setSolverStatus(solverStatus);
        return solution;
    }

    public SolverStatus getSolverStatus() {
        return solverManager.getSolverStatus(TimeTableService.SINGLETON_TIME_TABLE_ID);
    }
}
