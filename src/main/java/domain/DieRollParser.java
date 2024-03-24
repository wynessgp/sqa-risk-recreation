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

    public void rollDiceToDeterminePlayerOrder(int amountOfDiceToRoll) {
        if (amountOfDiceToRoll < 2 || amountOfDiceToRoll > 6) {
            throw new IllegalArgumentException("Valid amount of dice is in the range [2, 6]");
        }
    }

    public List<Integer> rollAttackerDice(int amountOfDiceToRoll) {
        validateRequestedAmountOfDiceToRollIsInRange(amountOfDiceToRoll, 3);
        return rollDiceFromList(amountOfDiceToRoll, attackerDice);
    }

    public List<Integer> rollDefenderDice(int amountOfDiceToRoll) {
        validateRequestedAmountOfDiceToRollIsInRange(amountOfDiceToRoll, 2);
        return rollDiceFromList(amountOfDiceToRoll, defenderDice);
    }

    private void validateRequestedAmountOfDiceToRollIsInRange(
            int requestedAmount, int maxAllowedAmount) {
        if (requestedAmount < 1 || requestedAmount > maxAllowedAmount) {
            throw new IllegalArgumentException(
                    "Valid amount of dice is in the range [1, " + maxAllowedAmount + "]");
        }
    }

    private List<Integer> rollDiceFromList(int amountOfDiceToRoll, List<Die> listToUse) {
        List<Integer> rollResults = new ArrayList<>();
        for (int i = 0; i < amountOfDiceToRoll; i++) {
            rollResults.add(listToUse.get(i).rollSingleDie(randomizer));
        }
        rollResults.sort(Comparator.reverseOrder());
        return rollResults;
    }

    public List<BattleResult> generateBattleResults(
            List<Integer> defenderRolls, List<Integer> attackerRolls) {
        validateListsAreNotEmpty(defenderRolls, attackerRolls);
        validateListsAreSortedProperly(defenderRolls, attackerRolls);

        List<BattleResult> battleResults = new ArrayList<>();
        int smallerListSize = Math.min(defenderRolls.size(), attackerRolls.size());
        for (int i = 0; i < smallerListSize; i++) {
            battleResults.add(calculateBattleResult(defenderRolls.get(i), attackerRolls.get(i)));
        }
        return battleResults;
    }

    private void validateListsAreNotEmpty(
            List<Integer> defenderRollsList, List<Integer> attackerRollsList) {
        if (attackerRollsList.isEmpty() || defenderRollsList.isEmpty()) {
            throw new IllegalArgumentException("Both arguments must have at least one element");
        }
    }

    private void validateListsAreSortedProperly(
            List<Integer> defenderRollsList, List<Integer> attackerRollsList) {
        if (!validateSortIsInNonIncreasingOrder(defenderRollsList)) {
            throw new IllegalArgumentException(
                    "defenderRolls are not sorted in non-increasing order");
        }
        if (!validateSortIsInNonIncreasingOrder(attackerRollsList)) {
            throw new IllegalArgumentException(
                    "attackerRolls are not sorted in non-increasing order");
        }
    }

    private BattleResult calculateBattleResult(Integer defenderRoll, Integer attackerRoll) {
        return (defenderRoll >= attackerRoll) ? BattleResult.DEFENDER_VICTORY :
                                                BattleResult.ATTACKER_VICTORY;
    }

    boolean validateSortIsInNonIncreasingOrder(List<Integer> listToCheck) {
        for (int i = 0; i < listToCheck.size() - 1; i++) {
            if (listToCheck.get(i) < listToCheck.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
