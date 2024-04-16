package domain;

import java.util.Set;
import java.util.stream.Collectors;

public class TradeInParser {
    private static final int TRADE_IN_SET_LENGTH = 3;
    private static final int MAXIMUM_NUMBER_OF_TRADES = 14;
    private static final int ARMY_CALCULATION_OFFSET = 6;
    private static final int STARTING_NUMBER_OF_ARMIES = 4;
    private static final int ADDITIONAL_NUMBER_OF_ARMIES = 2;
    private static final int STARTING_NUMBER_OF_ARMIES_AFTER_THRESHOLD = 15;
    private static final int ADDITIONAL_NUMBER_OF_ARMIES_AFTER_THRESHOLD = 5;

    private int setsTradedIn = 0;

    public int startTrade(Set<Card> cards) {
        checkTradeInState(cards);
        if (hasOneOfEachType(cards) || hasThreeOfSameType(cards) || hasWild(cards)) {
            setsTradedIn++;
            return setsTradedIn < ARMY_CALCULATION_OFFSET ? STARTING_NUMBER_OF_ARMIES + ADDITIONAL_NUMBER_OF_ARMIES
                    * (setsTradedIn - 1) : STARTING_NUMBER_OF_ARMIES_AFTER_THRESHOLD
                    + ADDITIONAL_NUMBER_OF_ARMIES_AFTER_THRESHOLD * (setsTradedIn - ARMY_CALCULATION_OFFSET);
        }
        throw new IllegalStateException("Invalid trade in set");
    }

    public Set<TerritoryType> getMatchedTerritories(Player player, Set<Card> cards) {
        if (cards.size() != TRADE_IN_SET_LENGTH) {
            throw new IllegalStateException("Invalid number of cards");
        }
        return player.getTerritories().stream().filter(territory -> cards.stream().anyMatch(card ->
                card.matchesTerritory(territory))).collect(Collectors.toSet());
    }

    private void checkTradeInState(Set<Card> cards) {
        if (cards.size() != TRADE_IN_SET_LENGTH) {
            throw new IllegalStateException("Must trade in exactly three cards");
        }
        if (setsTradedIn >= MAXIMUM_NUMBER_OF_TRADES) {
            throw new IllegalStateException("No more cards to trade in");
        }
    }

    private boolean hasOneOfEachType(Set<Card> cards) {
        return countPieceType(cards, PieceType.INFANTRY) == 1
                && countPieceType(cards, PieceType.CAVALRY) == 1
                && countPieceType(cards, PieceType.ARTILLERY) == 1;
    }

    private boolean hasThreeOfSameType(Set<Card> cards) {
        return countPieceType(cards, PieceType.INFANTRY) == TRADE_IN_SET_LENGTH
                || countPieceType(cards, PieceType.CAVALRY) == TRADE_IN_SET_LENGTH
                || countPieceType(cards, PieceType.ARTILLERY) == TRADE_IN_SET_LENGTH;
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
