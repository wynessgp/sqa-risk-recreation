package domain;

import java.util.Random;

class Die {

    private final int maximumPossibleRoll;
    private final int minimumPossibleRoll;

    Die(int maximumPossibleRoll, int minimumPossibleRoll) {
        this.maximumPossibleRoll = maximumPossibleRoll;
        this.minimumPossibleRoll = minimumPossibleRoll;
    }

    Integer rollSingleDie(Random randomizer) {
        // JDK 11 doesn't include the lower bounded nextInt method; so we offset the maximum
        return randomizer.nextInt((maximumPossibleRoll - minimumPossibleRoll) + 1) + minimumPossibleRoll;
    }

}
