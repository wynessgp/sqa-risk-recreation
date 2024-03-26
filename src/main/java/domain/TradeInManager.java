package domain;

import java.util.List;

public class TradeInManager {

    private int setsTradedInSoFar;

    public TradeInManager() {
        setsTradedInSoFar = 0;
    }

    public int startTrade(List<Card> attemptedCards) {
        if (this.verifyValidCombo(attemptedCards)) {
            int numNewPieces = this.calculateNumNewPieces();
            updateSetsTradedIn();
            return numNewPieces;
        }
        return 0;
    }

    public boolean verifyValidCombo(List<Card> attemptedCards) {
        if (attemptedCards.size() == 3) {
            return hasWild(attemptedCards)
                    || (hasInfantry(attemptedCards) && hasCavalry(attemptedCards) && hasArtillery(attemptedCards))
                    || (hasInfantry(attemptedCards) && !hasCavalry(attemptedCards) && !hasArtillery(attemptedCards))
                    || (!hasInfantry(attemptedCards) && hasCavalry(attemptedCards) && !hasArtillery(attemptedCards))
                    || (!hasInfantry(attemptedCards) && !hasCavalry(attemptedCards) && hasArtillery(attemptedCards));
        }
        return false;
    }

    private boolean hasWild(List<Card> attemptedCards) {
        for (Card card : attemptedCards) {
            if (card.isWild()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInfantry(List<Card> attemptedCards) {
        for (Card card : attemptedCards) {
            if (card.matchesPieceType(PieceType.INFANTRY)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCavalry(List<Card> attemptedCards) {
        for (Card card : attemptedCards) {
            if (card.matchesPieceType(PieceType.CAVALRY)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasArtillery(List<Card> attemptedCards) {
        for (Card card : attemptedCards) {
            if (card.matchesPieceType(PieceType.ARTILLERY)) {
                return true;
            }
        }
        return false;
    }

    public int calculateNumNewPieces() {
        if (this.setsTradedInSoFar < 5) {
            return 2 * this.setsTradedInSoFar + 4;
        } else if (this.setsTradedInSoFar >= 14) { //all cards traded in
            return 0;
        }
        return (this.setsTradedInSoFar - 5) * 5 + 15;
    }

    public boolean updateSetsTradedIn() {
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
