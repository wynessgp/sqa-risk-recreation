package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckManagerTest {

    private DeckManager deckManager;
    @BeforeEach
    public void setup() {
        deckManager = new DeckManager();
    }

    @Test
    public void test01_initDeck_EmptyCollection_ExpectSuccess() {
        boolean result = deckManager.initDeck();

        assertTrue(result);

        List<Card> deck = deckManager.getDeckOfCards();
        assertEquals(44, deck.size());
        //no checking for card type yet

    }
    @Test
    public void test02_initDeck_OneElementCollection_ExpectException() {
        deckManager.getDeckOfCards().add(new TerritoryCard(TerritoryType.ALBERTA, PieceType.INFANTRY));

        assertThrows(IllegalStateException.class, () -> deckManager.initDeck());
    }
    @Test
    public void test03_shuffle_EmptyList_ExpectFalseAndEmptyList() {
        deckManager.getDeckOfCards().clear();

        boolean result = deckManager.shuffle();

        assertFalse(result);

        assertTrue(deckManager.getDeckOfCards().isEmpty());
    }

    @Test
    public void test04_shuffle_OneElementList_ExpectTrueAndSameList() {
        deckManager.getDeckOfCards().add(new TerritoryCard(TerritoryType.ALBERTA, PieceType.INFANTRY));

        boolean result = deckManager.shuffle();

        assertTrue(result);

        List<Card> deck = deckManager.getDeckOfCards();
        assertEquals(1, deck.size());
//        assertEquals(TerritoryType.ALBERTA, ((TerritoryCard) deck.get(0)).getTerritory()); //getters are not implemented yet
//        assertEquals(PieceType.INFANTRY, ((TerritoryCard) deck.get(0)).getPiece());
    }



}
