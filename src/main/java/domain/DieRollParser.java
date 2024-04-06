package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DieRollParser {

    private final List<Die> attackerDice;
    private final List<Die> defenderDice;
    private Die setupDie;
    private final Random randomizer;

    public DieRollParser() {
        this.attackerDice = List.of(new Die(6, 1), new Die(6, 1), new Die(6, 1));
        this.defenderDice = List.of(new Die(6, 1), new Die(6, 1));
        // Changed when we go to roll it.
        this.setupDie = new Die(0, 0);
        this.randomizer = new Random();
    }

    // this constructor is to only be utilized for unit testing!
    DieRollParser(Random randomizer, List<Die> attackerDice,
                  List<Die> defenderDice) {
        this.randomizer = randomizer;
        this.attackerDice = attackerDice;
        this.defenderDice = defenderDice;
        // Changed when we go to roll it.
        this.setupDie = new Die(0, 0);
    }

    public List<Integer> rollDiceToDeterminePlayerOrder(int amountOfDiceToRoll) {
        validateRequestedAmountOfDiceToRollIsInRange(amountOfDiceToRoll, 2, 6);
        setupDie = new Die(amountOfDiceToRoll, 1);

        Set<Integer> rollResults = new HashSet<>();
        while (rollResults.size() != amountOfDiceToRoll) { // continually re-roll the die until we have no duped rolls.
            rollResults.add(setupDie.rollSingleDie(randomizer));
        }
        return List.copyOf(rollResults);
    }

    public List<Integer> rollAttackerDice(int amountOfDiceToRoll) {
        validateRequestedAmountOfDiceToRollIsInRange(amountOfDiceToRoll, 1, 3);
        return rollDiceFromList(amountOfDiceToRoll, attackerDice);
    }

    public List<Integer> rollDefenderDice(int amountOfDiceToRoll) {
        validateRequestedAmountOfDiceToRollIsInRange(amountOfDiceToRoll, 1, 2);
        return rollDiceFromList(amountOfDiceToRoll, defenderDice);
    }

    private void validateRequestedAmountOfDiceToRollIsInRange(
            int requestedAmount, int minAllowedAmount, int maxAllowedAmount) {
        if (requestedAmount < minAllowedAmount || requestedAmount > maxAllowedAmount) {
            throw new IllegalArgumentException(String.format(
                    "Valid amount of dice is in the range [%d, %d]", minAllowedAmount, maxAllowedAmount));
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

    public List<BattleResult> generateBattleResults(List<Integer> defenderRolls, List<Integer> attackerRolls) {
        validateListsAreNotEmpty(defenderRolls, attackerRolls);
        validateListsAreSortedProperly(defenderRolls, attackerRolls);
        List<BattleResult> battleResults = new ArrayList<>();
        int smallerListSize = Math.min(defenderRolls.size(), attackerRolls.size());
        for (int i = 0; i < smallerListSize; i++) {
            battleResults.add(calculateBattleResult(defenderRolls.get(i), attackerRolls.get(i)));
        }
        return battleResults;
    }

    private void validateListsAreNotEmpty(List<Integer> defenderRollsList, List<Integer> attackerRollsList) {
        if (attackerRollsList.isEmpty() || defenderRollsList.isEmpty()) {
            throw new IllegalArgumentException("Both arguments must have at least one element");
        }
    }

    private void validateListsAreSortedProperly(List<Integer> defenderRollsList, List<Integer> attackerRollsList) {
        if (!validateSortIsInNonIncreasingOrder(defenderRollsList)) {
            throw new IllegalArgumentException("defenderRolls are not sorted in non-increasing order");
        }

        if (!validateSortIsInNonIncreasingOrder(attackerRollsList)) {
            throw new IllegalArgumentException("attackerRolls are not sorted in non-increasing order");
        }
    }

    private BattleResult calculateBattleResult(Integer defenderRoll, Integer attackerRoll) {
        return (defenderRoll >= attackerRoll) ? BattleResult.DEFENDER_VICTORY : BattleResult.ATTACKER_VICTORY;
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
