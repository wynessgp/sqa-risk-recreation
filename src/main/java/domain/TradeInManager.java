package domain;

import java.util.Set;

public class TradeInManager {

    public void startTrade(Set<Card> cards) {
        throw new IllegalArgumentException("Must trade in exactly three cards");
    }
}
