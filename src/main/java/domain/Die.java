package domain;

import java.util.Random;

public class Die {

    private int maximumPossibleRoll;
    private int minimumPossibleRoll;

    protected Die() {
        this(0, 0); // for testing purposes.
    }

    public Die(int minimumPossibleRoll, int maximumPossibleRoll) {
        this.maximumPossibleRoll = maximumPossibleRoll;
        this.minimumPossibleRoll = minimumPossibleRoll;
    }

    public Integer rollSingleDie(Random randomizer) {
        if (randomizer == null) 
            throw new NullPointerException("Randomizer object is null, cannot roll Die!");
        // JDK 11 doesn't include the lowerbounded nextInt method; so we offset the maximum
        return randomizer.nextInt(maximumPossibleRoll - minimumPossibleRoll) + minimumPossibleRoll;
    }

}
