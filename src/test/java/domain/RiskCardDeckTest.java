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

public class RiskCardDeckTest {

    @Test
    public void test00_drawCard_withEmptyDeck_throwsException() {
        String expectedMessage = "Cannot draw card from an empty deck";
        Random random = EasyMock.createMock(Random.class);
        // Creating mocked random object to use test constructor (does not initDeck and shuffle)
        RiskCardDeck riskCardDeck = new RiskCardDeck(random);

        Exception exception = assertThrows(NoSuchElementException.class, riskCardDeck::drawCard);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test01_drawCard_withCompleteDeck_drawUntilEmpty() {
        int startingSize = 44;
        Random random = EasyMock.createMock(Random.class);
        RiskCardDeck riskCardDeck = new RiskCardDeck(random);
        List<Card> mockedCards = new ArrayList<>();

        for (int i = 0; i < startingSize; i++) {
            Card cardToAdd = EasyMock.createMock(Card.class);
            EasyMock.replay(cardToAdd);
            mockedCards.add(cardToAdd);
        }
        riskCardDeck.setDeck(mockedCards);

        for (int i = startingSize - 1; i >= 0; i--) {
            Card drawnCard = assertDoesNotThrow(riskCardDeck::drawCard);
            assertEquals(mockedCards.get(i), drawnCard);
            EasyMock.verify(drawnCard);
        }
        assertTrue(riskCardDeck.isDeckEmpty());
    }

    @Test
    public void test02_shuffle_withEmptyDeck_returnsFalse() {
        Random random = EasyMock.createMock(Random.class);
        RiskCardDeck riskCardDeck = new RiskCardDeck(random);

        assertFalse(riskCardDeck.shuffle());
    }

    @Test
    public void test03_shuffle_withOneCard_returnsFalse() {
        Random random = EasyMock.createMock(Random.class);
        RiskCardDeck riskCardDeck = new RiskCardDeck(random);

        List<Card> mockedCards = new ArrayList<>();
        Card cardToAdd = EasyMock.createMock(Card.class);
        EasyMock.replay(cardToAdd);

        mockedCards.add(cardToAdd);
        riskCardDeck.setDeck(mockedCards);

        assertFalse(riskCardDeck.shuffle());
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

        RiskCardDeck riskCardDeck = new RiskCardDeck(random);
        riskCardDeck.setDeck(mockedCards);
        assertTrue(riskCardDeck.shuffle());
        EasyMock.verify(random);

        // Ensure at least one card is in a different location
        boolean isListDifferent = false;
        for (Card card : mockedCards) {
            Card drawnCard = riskCardDeck.drawCard();
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
        RiskCardDeck riskCardDeck = new RiskCardDeck(random);
        assertTrue(riskCardDeck.initDeck());

        for (int i = 0; i < expectedTerritoryCards + expectedWildCards; i++) {
            Card drawnCard = riskCardDeck.drawCard();
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
        RiskCardDeck riskCardDeck = new RiskCardDeck(random);

        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Card cardToAdd = EasyMock.createMock(Card.class);
            EasyMock.replay(cardToAdd);
            cards.add(cardToAdd);
        }

        riskCardDeck.setDeck(cards);
        Exception exception = assertThrows(IllegalStateException.class, riskCardDeck::initDeck);
        assertEquals(expectedMessage, exception.getMessage());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

    @Test
    public void test07_defaultConstructor_createsShuffledDeck() {
        int expectedTerritoryCards = 42;
        int actualTerritoryCards = 0;
        int expectedWildCards = 2;
        int actualWildCards = 0;

        RiskCardDeck riskCardDeck = new RiskCardDeck();

        for (int i = 0; i < expectedTerritoryCards + expectedWildCards; i++) {
            Card drawnCard = riskCardDeck.drawCard();
            if (drawnCard.isWild()) {
                actualWildCards++;
            } else {
                actualTerritoryCards++;
            }
        }

        assertEquals(expectedTerritoryCards, actualTerritoryCards);
        assertEquals(expectedWildCards, actualWildCards);
    }
}
