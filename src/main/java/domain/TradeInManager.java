package domain;

import java.util.Set;

public class TradeInManager {

    public int startTrade(Set<Card> cards) {
        if (cards.size() == 3 && cards.stream().anyMatch(card -> card.matchesPieceType(PieceType.INFANTRY))
                && cards.stream().anyMatch(card -> card.matchesPieceType(PieceType.CAVALRY))
                && cards.stream().anyMatch(card -> card.matchesPieceType(PieceType.ARTILLERY))) {
            return 4;
        }
        throw new IllegalArgumentException("Must trade in exactly three cards");
    }
}
