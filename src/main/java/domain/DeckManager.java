package domain;

import java.util.NoSuchElementException;

public class DeckManager {

    public Card drawCard() {
        throw new NoSuchElementException("Cannot draw card from an empty deck");
    }
}
