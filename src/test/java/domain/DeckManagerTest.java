package domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DeckManagerTest {

    @Test
    public void test00_drawCard_withEmptyDeck_throwsException() {
        String expectedMessage = "Cannot draw card from an empty deck";
        Random random = EasyMock.createMock(Random.class);
        // Creating mocked random object to use test constructor (does not initDeck and shuffle)
        DeckManager deckManager = new DeckManager(random);

        Exception exception = assertThrows(NoSuchElementException.class, deckManager::drawCard);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test01_drawCard_withCompleteDeck_drawUntilEmpty() {
        int startingSize = 44;
        Random random = EasyMock.createMock(Random.class);
        DeckManager deckManager = new DeckManager(random);
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

    @Test
    public void test02_shuffle_withEmptyDeck_returnsFalse() {
        Random random = EasyMock.createMock(Random.class);
        DeckManager deckManager = new DeckManager(random);

        assertFalse(deckManager.shuffle());
    }

    @Test
    public void test03_shuffle_withOneCard_returnsFalse() {
        Random random = EasyMock.createMock(Random.class);
        DeckManager deckManager = new DeckManager(random);

        List<Card> mockedCards = new ArrayList<>();
        Card cardToAdd = EasyMock.createMock(Card.class);
        EasyMock.replay(cardToAdd);

        mockedCards.add(cardToAdd);
        deckManager.setDeck(mockedCards);

        assertFalse(deckManager.shuffle());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 43, 44})
    public void test04_shuffle_withTwoOrMoreCards_returnsTrue(int startingSize) {
        List<Card> mockedCards = new ArrayList<>();
        for (int i = 0; i < startingSize; i++) {
            Card cardToAdd = EasyMock.createMock(Card.class);
            EasyMock.replay(cardToAdd);
            mockedCards.add(cardToAdd);
        }

        Random random = EasyMock.createMock(Random.class);
        for (int i = startingSize; i > 1; i--) {
            EasyMock.expect(random.nextInt(i)).andReturn((i - 1) % startingSize);
        }
        EasyMock.replay(random);

        DeckManager deckManager = new DeckManager(random);
        deckManager.setDeck(mockedCards);
        assertTrue(deckManager.shuffle());
        EasyMock.verify(random);

        // Ensure at least one card is in a different location
        boolean isListDifferent = false;
        for (Card card : mockedCards) {
            Card drawnCard = deckManager.drawCard();
            if (!card.equals(drawnCard)) {
                isListDifferent = true;
            }
            EasyMock.verify(card);
        }
        assertTrue(isListDifferent);
    }

    @Test
    public void test05_initDeck_withEmptyDeck_returnsTrue() {
        int expectedTerritoryCards = 42;
        int actualTerritoryCards = 0;
        int expectedWildCards = 2;
        int actualWildCards = 0;

        Random random = EasyMock.createMock(Random.class);
        DeckManager deckManager = new DeckManager(random);
        assertTrue(deckManager.initDeck());

        for (int i = 0; i < expectedTerritoryCards + expectedWildCards; i++) {
            Card drawnCard = deckManager.drawCard();
            if (drawnCard.isWild()) {
                actualWildCards++;
            } else {
                actualTerritoryCards++;
            }
        }

        assertEquals(expectedTerritoryCards, actualTerritoryCards);
        assertEquals(expectedWildCards, actualWildCards);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 44})
    public void test06_initDeck_withNonEmptyDeck_throwsException(int size) {
        String expectedMessage = "Deck was previously initialized";
        Random random = EasyMock.createMock(Random.class);
        DeckManager deckManager = new DeckManager(random);

        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Card cardToAdd = EasyMock.createMock(Card.class);
            EasyMock.replay(cardToAdd);
            cards.add(cardToAdd);
        }

        deckManager.setDeck(cards);
        Exception exception = assertThrows(IllegalStateException.class, deckManager::initDeck);
        assertEquals(expectedMessage, exception.getMessage());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

}
