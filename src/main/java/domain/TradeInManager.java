package domain;

import java.util.Set;

public class TradeInManager {
    private int setsTradedIn = 0;

    public int startTrade(Set<Card> cards) {
        if (cards.size() != 3) {
            throw new IllegalArgumentException("Must trade in exactly three cards");
        }
        if (hasOneOfEachType(cards) || hasThreeOfSameType(cards)) {
            return 4 + 2 * setsTradedIn;
        }
        return 0;
    }

    private boolean hasOneOfEachType(Set<Card> cards) {
        return countPieceType(cards, PieceType.INFANTRY) == 1
                && countPieceType(cards, PieceType.CAVALRY) == 1
                && countPieceType(cards, PieceType.ARTILLERY) == 1;
    }

    private boolean hasThreeOfSameType(Set<Card> cards) {
        return countPieceType(cards, PieceType.INFANTRY) == 3
                || countPieceType(cards, PieceType.CAVALRY) == 3
                || countPieceType(cards, PieceType.ARTILLERY) == 3;
    }

    private long countPieceType(Set<Card> cards, PieceType type) {
        return cards.stream().filter(card -> card.matchesPieceType(type)).count();
    }

    void setSetsTradedIn(int setsTradedIn) {
        this.setsTradedIn = setsTradedIn;
    }
}
