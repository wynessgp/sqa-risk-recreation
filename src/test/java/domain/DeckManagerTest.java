package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class DeckManagerTest {

    @Test
    public void test00_drawCard_withEmptyDeck_throwsException() {
        String expectedMessage = "Cannot draw card from an empty deck";
        DeckManager deckManager = new DeckManager();

        Exception exception = assertThrows(NoSuchElementException.class, deckManager::drawCard);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
