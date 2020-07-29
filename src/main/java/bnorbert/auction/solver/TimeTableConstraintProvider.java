package bnorbert.auction.solver;

import bnorbert.auction.domain.Home;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class TimeTableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard constraints
                roomConflict(constraintFactory)

        };
    }

    private Constraint roomConflict(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one home at the same time.
        // If you don't have enough time slots, the home will be placed in room #2
        return constraintFactory
                // Select each pair of 2 different homes
                .fromUniquePair(Home.class,
                        // ... in the same timeslot ...
                        Joiners.equal(Home::getTimeSlot),
                        // ... in the same room ...
                        Joiners.equal(Home::getRoom))
                // ... and penalize each pair with a hard weight.
                .penalize("Room conflict", HardSoftScore.ONE_HARD);
    }

}
