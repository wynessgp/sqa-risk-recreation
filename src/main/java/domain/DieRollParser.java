package domain;

import java.util.ArrayList;
import java.util.List;

public class DieRollParser {

    private List<Die> attackerDice;

    public DieRollParser() {
        this.attackerDice = new ArrayList<>();
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

    public void rollAttackerDice(int amountOfDiceToRoll) {
        if (amountOfDiceToRoll < 1 || amountOfDiceToRoll > 3) {
            throw new IllegalArgumentException("Valid amount of dice is in the range [1, 3]");
        }

        if (amountOfDiceToRoll > this.attackerDice.size()) {
            throw new IllegalArgumentException(
                    "Not enough dice to fulfill requested amount to roll");
        }
    }
}
