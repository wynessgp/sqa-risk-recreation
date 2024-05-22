package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

class RiskCardDeck {
    private static final int NUMBER_OF_WILD_CARDS = 2;
    private static final int CARDS_PER_PIECE_TYPE = 14;

    private List<Card> deckOfCards = new ArrayList<>();
    private final Random random;

    RiskCardDeck() {
        this.random = new Random();
        if (!initDeck()) {
            return;
        }
        shuffle();
    }

    RiskCardDeck(Random random) {
        this.random = random;
    }

    boolean initDeck() {
        if (!isDeckEmpty()) {
            return false;
        }
        addTerritoryCards();
        addWildCards();
        return true;
    }

    private void addTerritoryCards() {
        int pieceTypeCount = 0;
        for (TerritoryType territoryType : TerritoryType.values()) {
            PieceType currentPiece = PieceType.values()[pieceTypeCount / CARDS_PER_PIECE_TYPE];
            deckOfCards.add(new TerritoryCard(territoryType, currentPiece));
            pieceTypeCount++;
        }
    }

    private void addWildCards() {
        for (int i = 0; i < NUMBER_OF_WILD_CARDS; i++) {
            deckOfCards.add(new WildCard());
        }
    }

    Card drawCard() {
        if (isDeckEmpty()) {
            throw new NoSuchElementException("Cannot draw card from an empty deck");
        }
        return deckOfCards.remove(deckOfCards.size() - 1);
    }

    boolean shuffle() {
        if (this.deckOfCards.size() <= 1) {
            return false;
        }
        Collections.shuffle(deckOfCards, random);
        return true;
    }

    void setDeck(List<Card> deck) {
        this.deckOfCards = new ArrayList<>(deck);
    }

    boolean isDeckEmpty() {
        return deckOfCards.isEmpty();
    }
}
