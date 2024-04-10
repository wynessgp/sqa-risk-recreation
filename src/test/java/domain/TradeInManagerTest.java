package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TradeInManagerTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 4})
    public void test00_startTrade_withWrongSizeCollection_throwsException(int size) {
        Set<Card> cards = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Card card = EasyMock.createMock(Card.class);
            cards.add(card);
            EasyMock.replay(card);
        }

        TradeInManager tradeIn = new TradeInManager();
        String expected = "Must trade in exactly three cards";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> tradeIn.startTrade(cards));
        assertEquals(expected, exception.getMessage());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

    private Card createMockedCard(PieceType type) {
        Card card = EasyMock.createNiceMock(Card.class);
        EasyMock.expect(card.matchesPieceType(type)).andReturn(true).anyTimes();
        EasyMock.replay(card);
        return card;
    }

    private void testSuccessfulTradeIn(int previousTrades, Set<Card> cards, int expected) {
        TradeInManager tradeIn = new TradeInManager();
        tradeIn.setSetsTradedIn(previousTrades);
        int actual = tradeIn.startTrade(cards);
        assertEquals(expected, actual);

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

    @Test
    public void test01_startTrade_noPrevious_withOneOfEach_returnsFour() {
        Set<Card> cards = new HashSet<>();
        for (PieceType type : PieceType.values()) {
            cards.add(createMockedCard(type));
        }
        testSuccessfulTradeIn(0, cards, 4);
    }

    @Test
    public void test02_startTrade_onePrevious_withThreeInfantry_returnsSix() {
        Set<Card> cards = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            cards.add(createMockedCard(PieceType.INFANTRY));
        }
        testSuccessfulTradeIn(1, cards, 6);
    }

    @Test
    public void test03_startTrade_twoPrevious_withThreeCavalry_returnsEight() {
        Set<Card> cards = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            cards.add(createMockedCard(PieceType.CAVALRY));
        }
        testSuccessfulTradeIn(2, cards, 8);
    }
}
