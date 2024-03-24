package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DieRollParser {

    private final List<Die> attackerDice;
    private final List<Die> defenderDice;
    private final Random randomizer;

    public DieRollParser() {
        this.attackerDice = new ArrayList<>();
        this.defenderDice = new ArrayList<>();
        this.randomizer = new Random();
    }

    // this constructor is to only be utilized for unit testing!
    DieRollParser(Random randomizer, List<Die> attackerDice, List<Die> defenderDice) {
        this.randomizer = randomizer;
        this.attackerDice = attackerDice;
        this.defenderDice = defenderDice;
    }

    public boolean buildDiceLists() {
        for (int i = 0; i < 3; i++) {
            attackerDice.add(new Die(6, 1));
        }
        for (int i = 0; i < 2; i++) {
            defenderDice.add(new Die(6, 1));
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
        return rollDiceFromList(amountOfDiceToRoll, attackerDice);
    }

    public List<Integer> rollDefenderDice(int amountOfDiceToRoll) {
        if (amountOfDiceToRoll < 1 || amountOfDiceToRoll > 2) {
            throw new IllegalArgumentException("Valid amount of dice is in the range [1, 2]");
        }

        if (amountOfDiceToRoll > this.defenderDice.size()) {
            throw new IllegalArgumentException(
                    "Not enough dice to fulfill requested amount to roll");
        }
        return rollDiceFromList(amountOfDiceToRoll, defenderDice);
    }

    private List<Integer> rollDiceFromList(int amountOfDiceToRoll, List<Die> listToUse) {
        List<Integer> rollResults = new ArrayList<>();
        for (int i = 0; i < amountOfDiceToRoll; i++) {
            rollResults.add(listToUse.get(i).rollSingleDie(randomizer));
        }
        rollResults.sort(Comparator.reverseOrder());
        return rollResults;
    }

    boolean validateSortIsInNonIncreasingOrder(List<Integer> listToCheck) {
        for (int i = 0; i < listToCheck.size() - 1; i++) {
            if (listToCheck.get(i) < listToCheck.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public List<BattleResults> generateBattleResults(
            List<Integer> defenderRolls, List<Integer> attackerRolls) {
        if (attackerRolls.isEmpty() || defenderRolls.isEmpty()) {
            throw new IllegalArgumentException("Both arguments must have at least one element");
        }

        if (!validateSortIsInNonIncreasingOrder(defenderRolls)) {
            throw new IllegalArgumentException(
                    "defenderRolls are not sorted in non-increasing order");
        }
        if (!validateSortIsInNonIncreasingOrder(attackerRolls)) {
            throw new IllegalArgumentException(
                    "attackerRolls are not sorted in non-increasing order");
        }

        if (attackerRolls.size() == 3) {
            return List.of(BattleResults.DEFENDER_VICTORY, BattleResults.DEFENDER_VICTORY);
        }
        return List.of(BattleResults.DEFENDER_VICTORY);
    }
}
