package domain;

import java.util.Random;

public class Die {

    private final int maximumPossibleRoll;
    private final int minimumPossibleRoll;

    protected Die() {
        this(0, 0); // for testing purposes.
    }

    public Die(int maximumPossibleRoll, int minimumPossibleRoll) {
        this.maximumPossibleRoll = maximumPossibleRoll;
        this.minimumPossibleRoll = minimumPossibleRoll;
    }

    public Integer rollSingleDie(Random randomizer) {
        if (randomizer == null) {
            throw new NullPointerException("Randomizer object is null, cannot roll Die!");
        }
        // JDK 11 doesn't include the lower bounded nextInt method; so we offset the maximum
        return randomizer.nextInt((maximumPossibleRoll - minimumPossibleRoll) + 1) + minimumPossibleRoll;
    }

}
