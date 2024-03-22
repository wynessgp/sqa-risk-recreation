package domain;

public class DieRollParser {

    public boolean buildDiceLists() {
        return true;
    }

    public void rollAttackerDice(int amountOfDiceToRoll) {
        if (amountOfDiceToRoll < 1 || amountOfDiceToRoll > 3) {
            throw new IllegalArgumentException("Valid amount of dice is in the range [1, 3]");
        }
    }
}
