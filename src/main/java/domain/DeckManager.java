package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DeckManager {
    List<Card> deckOfCards;

    public DeckManager() {
        this.deckOfCards = new ArrayList<>();
    }

    public Card drawCard() {
        if (isDeckEmpty()) {
            throw new NoSuchElementException("Cannot draw card from an empty deck");
        }
        return deckOfCards.remove(deckOfCards.size() - 1);
    }

    void setDeck(List<Card> deck) {
        this.deckOfCards = new ArrayList<>(deck);
    }

    boolean isDeckEmpty() {
        return deckOfCards.isEmpty();
    }
}
