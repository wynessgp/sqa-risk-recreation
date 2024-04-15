package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public final class RiskCardDeck {
    private List<Card> deckOfCards = new ArrayList<>();
    private final Random random;

    public RiskCardDeck() {
        this.random = new Random();
        initDeck();
        shuffle();
    }

    RiskCardDeck(Random random) {
        this.random = random;
    }

    boolean initDeck() {
        if (!isDeckEmpty()) {
            throw new IllegalStateException("Deck was previously initialized");
        }
        addTerritoryCards();
        addWildCards();
        return true;
    }

    private void addTerritoryCards() {
        int pieceTypeCount = 0;
        for (TerritoryType territoryType : TerritoryType.values()) {
            PieceType currentPiece = PieceType.values()[pieceTypeCount / 14];
            deckOfCards.add(new TerritoryCard(territoryType, currentPiece));
            pieceTypeCount++;
        }
    }

    private void addWildCards() {
        for (int i = 0; i < 2; i++) {
            deckOfCards.add(new WildCard());
        }
    }

    public Card drawCard() {
        if (isDeckEmpty()) {
            throw new NoSuchElementException("Cannot draw card from an empty deck");
        }
        return deckOfCards.remove(deckOfCards.size() - 1);
    }

    boolean shuffle() {
        if (this.deckOfCards.size() < 2) {
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
