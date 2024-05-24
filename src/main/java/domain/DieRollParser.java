package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

class DieRollParser {

    private final List<Die> attackerDice;
    private final List<Die> defenderDice;
    private Die setupDie;
    private final Random randomizer;

    private static final int MINIMUM_DIE_ROLL = 1;
    private static final int MAXIMUM_DIE_ROLL = 6;

    private static final int MINIMUM_VALID_AMOUNT_OF_SETUP_DICE = 3;
    private static final int MAXIMUM_VALID_AMOUNT_OF_SETUP_DICE = 6;

    private static final int MINIMUM_VALID_AMOUNT_OF_ATTACKER_OR_DEFENDER_DICE = 1;
    private static final int MAXIMUM_VALID_AMOUNT_OF_DEFENDER_DICE = 2;
    private static final int MAXIMUM_VALID_AMOUNT_OF_ATTACKER_DICE = 3;

    DieRollParser() {
        this(new Random(),
                List.of(new Die(MAXIMUM_DIE_ROLL, MINIMUM_DIE_ROLL), new Die(MAXIMUM_DIE_ROLL, MINIMUM_DIE_ROLL),
                        new Die(MAXIMUM_DIE_ROLL, MINIMUM_DIE_ROLL)),
                List.of(new Die(MAXIMUM_DIE_ROLL, MINIMUM_DIE_ROLL), new Die(MAXIMUM_DIE_ROLL, MINIMUM_DIE_ROLL)));
    }

    DieRollParser(Random randomizer, List<Die> attackerDice,
                  List<Die> defenderDice) {
        this.randomizer = randomizer;
        this.attackerDice = attackerDice;
        this.defenderDice = defenderDice;
        this.setupDie = new Die(MAXIMUM_DIE_ROLL, MINIMUM_DIE_ROLL);
    }

    List<Integer> rollDiceToDeterminePlayerOrder(int amountOfDiceToRoll) {
        validateRequestedAmountOfDiceToRollIsInRange("player setup", amountOfDiceToRoll,
                MINIMUM_VALID_AMOUNT_OF_SETUP_DICE, MAXIMUM_VALID_AMOUNT_OF_SETUP_DICE);
        List<Integer> rollResults = new ArrayList<>();
        while (rollResults.size() != amountOfDiceToRoll) { // continually re-roll the die until we have no duped rolls.
            int rollResult = setupDie.rollSingleDie(randomizer);
            addResultIfNoDuplicate(rollResults, rollResult);
        }
        return List.copyOf(rollResults);
    }

    private void addResultIfNoDuplicate(List<Integer> rollResults, int rollResult) {
        if (!rollResults.contains(rollResult)) {
            rollResults.add(rollResult);
        }
    }

    List<Integer> rollAttackerDice(int amountOfDiceToRoll) {
        validateRequestedAmountOfDiceToRollIsInRange("attacker", amountOfDiceToRoll,
                MINIMUM_VALID_AMOUNT_OF_ATTACKER_OR_DEFENDER_DICE, MAXIMUM_VALID_AMOUNT_OF_ATTACKER_DICE);

        return rollDiceFromList(amountOfDiceToRoll, attackerDice);
    }

    List<Integer> rollDefenderDice(int amountOfDiceToRoll) {
        validateRequestedAmountOfDiceToRollIsInRange("defender", amountOfDiceToRoll,
                MINIMUM_VALID_AMOUNT_OF_ATTACKER_OR_DEFENDER_DICE, MAXIMUM_VALID_AMOUNT_OF_DEFENDER_DICE);

        return rollDiceFromList(amountOfDiceToRoll, defenderDice);
    }

    private void validateRequestedAmountOfDiceToRollIsInRange(
            String rollType, int requestedAmount, int minAllowedAmount, int maxAllowedAmount) {
        if (requestedAmount < minAllowedAmount || requestedAmount > maxAllowedAmount) {
            throw new IllegalArgumentException(String.format(
                    "Valid amount of dice for %s roll must be in the range [%d, %d]", rollType,
                    minAllowedAmount, maxAllowedAmount));
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

    List<BattleResult> generateBattleResults(List<Integer> defenderRolls, List<Integer> attackerRolls) {
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

    void setSetupDie(Die setupDie) {
        this.setupDie = setupDie;
    }

}
