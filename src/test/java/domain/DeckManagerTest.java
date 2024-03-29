package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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
        assertEquals(44, deckManager.getDeckSize());
    }

    @Test
    public void test02_initDeck_OneElementCollection_ExpectException() {
        List<Card> nonEmptyDeck = new ArrayList<>();
        nonEmptyDeck.add(new TerritoryCard(TerritoryType.ALBERTA, PieceType.INFANTRY));
        deckManager = new DeckManager(nonEmptyDeck);
        assertThrows(IllegalStateException.class, () -> deckManager.initDeck());
    }

    @Test
    public void test03_shuffle_EmptyList_ExpectFalseAndEmptyList() {
        deckManager = new DeckManager(new ArrayList<>());
        boolean result = deckManager.shuffle();
        assertFalse(result);
        assertEquals(0, deckManager.getDeckSize());
    }

    @Test
    public void test04_shuffle_OneElementList_ExpectTrueAndSameList() {
        List<Card> singleCardDeck = new ArrayList<>();
        singleCardDeck.add(new TerritoryCard(TerritoryType.ALBERTA, PieceType.INFANTRY));
        deckManager = new DeckManager(singleCardDeck);
        boolean result = deckManager.shuffle();
        assertTrue(result);
        assertEquals(1, deckManager.getDeckSize());
    }

}
