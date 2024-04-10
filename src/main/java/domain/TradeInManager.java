package domain;

import java.util.Set;

public class TradeInManager {

    public int startTrade(Set<Card> cards) {
        if (cards.size() != 3) {
            throw new IllegalArgumentException("Must trade in exactly three cards");
        }
        long infantry = cards.stream().filter(c -> c.matchesPieceType(PieceType.INFANTRY)).count();
        long cavalry = cards.stream().filter(c -> c.matchesPieceType(PieceType.CAVALRY)).count();
        long artillery = cards.stream().filter(c -> c.matchesPieceType(PieceType.ARTILLERY)).count();
        return (infantry == 1 && cavalry == 1 && artillery == 1) ? 4 : 6;
    }
}
