package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    private static Stream<Arguments> pieceTypeGenerator() {
        return Stream.of(
                Arguments.of(PieceType.INFANTRY, 1, 6),
                Arguments.of(PieceType.CAVALRY, 2, 8),
                Arguments.of(PieceType.ARTILLERY, 3, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("pieceTypeGenerator")
    public void test02_startTrade_withPrevious_withThreeOfSameType_returnsNumberOfArmies(
            PieceType piece, int previousTrades, int expected) {
        Set<Card> cards = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            cards.add(createMockedCard(piece));
        }
        testSuccessfulTradeIn(previousTrades, cards, expected);
    }

    private Card createMockedWildCard() {
        Card card = EasyMock.createNiceMock(Card.class);
        EasyMock.expect(card.isWild()).andReturn(true).anyTimes();
        EasyMock.replay(card);
        return card;
    }

    @Test
    public void test03_startTrade_fourPrevious_withWildAndTwoInfantry_returnsTwelve() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedWildCard());
        cards.add(createMockedCard(PieceType.INFANTRY));
        cards.add(createMockedCard(PieceType.INFANTRY));
        testSuccessfulTradeIn(4, cards, 12);
    }
}
