package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DieRollParser {

    private final List<Die> attackerDice;
    private List<Die> defenderDice;
    private final Random randomizer;

    public DieRollParser() {
        this.attackerDice = new ArrayList<>();
        this.defenderDice = new ArrayList<>();
        this.randomizer = new Random();
    }

    // this method is to only be utilized for unit testing!
    DieRollParser(Random randomizer, List<Die> attackerDice) {
        this.randomizer = randomizer;
        this.attackerDice = attackerDice;
    }

    public boolean buildDiceLists() {
        for (int i = 0; i < 3; i++) {
            attackerDice.add(new Die(6, 1));
        }
        return true;
    }

    // this method is only to be used for unit testing!
    boolean buildDiceLists(int numDiceToInitialize) {
        for (int i = 0; i < numDiceToInitialize; i++) {
            attackerDice.add(new Die());
        }
        for (int i = 0; i < numDiceToInitialize; i++) {
            defenderDice.add(new Die());
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
        for (int i = 0; i < amountOfDiceToRoll; i++) {
            rollResults.add(attackerDice.get(i).rollSingleDie(randomizer));
        }
        rollResults.sort(Comparator.reverseOrder());
        return rollResults;
    }

    public void rollDefenderDice(int amountOfDiceToRoll) {
        if (amountOfDiceToRoll < 1 || amountOfDiceToRoll > 2) {
            throw new IllegalArgumentException("Valid amount of dice is in the range [1, 2]");
        }

        if (amountOfDiceToRoll > this.defenderDice.size()) {
            throw new IllegalArgumentException(
                    "Not enough dice to fulfill requested amount to roll");
        }
    }
}
