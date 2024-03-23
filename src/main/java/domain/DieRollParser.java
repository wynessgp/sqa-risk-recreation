package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DieRollParser {

    private final List<Die> attackerDice;
    private Random randomizer;

    public DieRollParser() {
        this.attackerDice = new ArrayList<>();
    }

    // this method is to only be utilized for unit testing!
    DieRollParser(Random randomizer, List<Die> attackerDice) {
        this.randomizer = randomizer;
        this.attackerDice = attackerDice;
    }

    public boolean buildDiceLists() {
        return true;
    }

    boolean buildDiceLists(int numDiceToInitialize) {
        for (int i = 0; i < numDiceToInitialize; i++) {
            attackerDice.add(new Die(1, 6));
        }
        return true;
    }

    public List<Integer> rollAttackerDice(int amountOfDiceToRoll) {
        if (amountOfDiceToRoll < 1 || amountOfDiceToRoll > 3) {
            throw new IllegalArgumentException("Valid amount of dice is in the range [1, 3]");
        }

        if (amountOfDiceToRoll > this.attackerDice.size()) {
            throw new IllegalArgumentException(
                    "Not enough dice to fulfill requested amount to roll");
        }

        List<Integer> rollResults = new ArrayList<>();
        if (amountOfDiceToRoll == 3) {
            rollResults.add(attackerDice.get(0).rollSingleDie(randomizer));
            rollResults.add(attackerDice.get(1).rollSingleDie(randomizer));
            rollResults.add(attackerDice.get(2).rollSingleDie(randomizer));
        }
        rollResults.sort(Comparator.reverseOrder());
        return rollResults;
    }
}
