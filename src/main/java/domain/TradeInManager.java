package domain;

import java.util.Set;

public class TradeInManager {
    private int setsTradedIn = 0;

    public int startTrade(Set<Card> cards) {
        checkTradeInState(cards);
        if (hasOneOfEachType(cards) || hasThreeOfSameType(cards) || hasWild(cards)) {
            setsTradedIn++;
            return setsTradedIn < 6 ? 4 + 2 * (setsTradedIn - 1) : 15 + 5 * (setsTradedIn - 6);
        }
        throw new IllegalStateException("Invalid trade in set");
    }

    private void checkTradeInState(Set<Card> cards) {
        if (cards.size() != 3) {
            throw new IllegalStateException("Must trade in exactly three cards");
        }
        if (setsTradedIn > 13) {
            throw new IllegalStateException("No more cards to trade in");
        }
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

    private boolean hasWild(Set<Card> cards) {
        return cards.stream().filter(Card::isWild).count() == 1;
    }

    void setSetsTradedIn(int setsTradedIn) {
        this.setsTradedIn = setsTradedIn;
    }
}
