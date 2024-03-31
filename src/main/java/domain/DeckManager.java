package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class DeckManager {
    private List<Card> deckOfCards;
    private Random random;

    public DeckManager() {
        this.deckOfCards = new ArrayList<>();
        this.random = new Random();
    }

    public Card drawCard() {
        if (isDeckEmpty()) {
            throw new NoSuchElementException("Cannot draw card from an empty deck");
        }
        return deckOfCards.remove(deckOfCards.size() - 1);
    }

    public boolean shuffle() {
        if (this.deckOfCards.size() < 2) {
            return false;
        }
        Collections.shuffle(deckOfCards, random);
        return true;
    }

    void setRandom(Random random) {
        this.random = random;
    }

    void setDeck(List<Card> deck) {
        this.deckOfCards = new ArrayList<>(deck);
    }

    boolean isDeckEmpty() {
        return deckOfCards.isEmpty();
    }
}
