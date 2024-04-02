package domain;

//import java.util.List;
import java.util.Set;

public class TradeInManager {

    private int setsTradedInSoFar;

    public TradeInManager() {
        setsTradedInSoFar = 0;
    }

    public int startTrade(Set<Card> attemptedCards) {
        if (this.verifyValidCombo(attemptedCards)) {
            int numNewPieces = this.calculateNumNewPieces();
            updateSetsTradedIn();
            return numNewPieces;
        }
        return 0;
    }

    protected boolean verifyValidCombo(Set<Card> attemptedCards) throws IllegalArgumentException{
        if (attemptedCards.size() == 3) {
            return (hasWild(attemptedCards)
                    || (hasInfantry(attemptedCards) && hasCavalry(attemptedCards) && hasArtillery(attemptedCards))
                    || (hasInfantry(attemptedCards) && !hasCavalry(attemptedCards) && !hasArtillery(attemptedCards))
                    || (!hasInfantry(attemptedCards) && hasCavalry(attemptedCards) && !hasArtillery(attemptedCards))
                    || (!hasInfantry(attemptedCards) && !hasCavalry(attemptedCards) && hasArtillery(attemptedCards)));
        }
        throw new IllegalArgumentException();
    }

    private boolean hasWild(Set<Card> attemptedCards) {
        for (Card card : attemptedCards) {
            if (card.isWild()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInfantry(Set<Card> attemptedCards) {
        for (Card card : attemptedCards) {
            if (card.matchesPieceType(PieceType.INFANTRY)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCavalry(Set<Card> attemptedCards) {
        for (Card card : attemptedCards) {
            if (card.matchesPieceType(PieceType.CAVALRY)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasArtillery(Set<Card> attemptedCards) {
        for (Card card : attemptedCards) {
            if (card.matchesPieceType(PieceType.ARTILLERY)) {
                return true;
            }
        }
        return false;
    }

    protected int calculateNumNewPieces() throws IllegalStateException{
        if (this.setsTradedInSoFar < 5) {
            return 2 * this.setsTradedInSoFar + 4;
        } else if (this.setsTradedInSoFar >= 14) { //all cards traded in
            throw new IllegalStateException();
        }
        return (this.setsTradedInSoFar - 5) * 5 + 15;
    }

    protected boolean updateSetsTradedIn() {
        if (this.setsTradedInSoFar >= 14) {
            return false; //add 1 would overflow
        }
        this.setsTradedInSoFar++;
        return true;
    }

    public int getSetsTradedInSoFar() {
        return this.setsTradedInSoFar;
    }

}
