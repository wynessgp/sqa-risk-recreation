package domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class DeckManagerTest {

    @Test
    public void test00_drawCard_withEmptyDeck_throwsException() {
        String expectedMessage = "Cannot draw card from an empty deck";
        DeckManager deckManager = new DeckManager();

        Exception exception = assertThrows(NoSuchElementException.class, deckManager::drawCard);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test01_drawCard_withCompleteDeck_drawUntilEmpty() {
        DeckManager deckManager = new DeckManager();
        int startingSize = 44;
        List<Card> mockedCards = new ArrayList<>();

        for (int i = 0; i < startingSize; i++) {
            Card cardToAdd = EasyMock.createMock(Card.class);
            EasyMock.replay(cardToAdd);
            mockedCards.add(cardToAdd);
        }
        deckManager.setDeck(mockedCards);

        for (int i = startingSize - 1; i >= 0; i--) {
            Card drawnCard = assertDoesNotThrow(deckManager::drawCard);
            assertEquals(mockedCards.get(i), drawnCard);
            EasyMock.verify(drawnCard);
        }
        assertTrue(deckManager.isDeckEmpty());
    }
}
