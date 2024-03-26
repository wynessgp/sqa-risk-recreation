package domain;

import java.util.ArrayList;
import java.util.List;


public class DeckManager {
    private List<Card> deckOfCards;

    public DeckManager() {
        this.deckOfCards = new ArrayList<>();
    }

    public boolean initDeck() {

        if (!this.deckOfCards.isEmpty()) {
            return false;
        }

        for (TerritoryType territory : TerritoryType.values()) {
            this.deckOfCards.add(new TerritoryCard(territory, PieceType.INFANTRY));
        }

        this.deckOfCards.add(new WildCard());
        this.deckOfCards.add(new WildCard());


        return true;
    }

    public List<Card> getDeckOfCards() {
        return this.deckOfCards;
    }

    
}
