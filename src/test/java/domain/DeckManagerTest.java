package domain;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

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



}
