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
        Exception exception = assertThrows(IllegalStateException.class, () -> tradeIn.startTrade(cards));
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

    private static Stream<Arguments> wildCardTestGenerator() {
        return Stream.of(
                Arguments.of(PieceType.INFANTRY, PieceType.INFANTRY, 4, 12),
                Arguments.of(PieceType.CAVALRY, PieceType.CAVALRY, 5, 15),
                Arguments.of(PieceType.ARTILLERY, PieceType.ARTILLERY, 6, 20),
                Arguments.of(PieceType.INFANTRY, PieceType.CAVALRY, 7, 25),
                Arguments.of(PieceType.INFANTRY, PieceType.ARTILLERY, 8, 30),
                Arguments.of(PieceType.CAVALRY, PieceType.ARTILLERY, 9, 35)
        );
    }

    private Card createMockedWildCard() {
        Card card = EasyMock.createNiceMock(Card.class);
        EasyMock.expect(card.isWild()).andReturn(true).anyTimes();
        EasyMock.replay(card);
        return card;
    }

    @ParameterizedTest
    @MethodSource("wildCardTestGenerator")
    public void test03_startTrade_withPrevious_withWildAndTwoOthers_returnsNumberOfArmies(
            PieceType secondCard, PieceType thirdCard, int previousTrades, int expected) {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedWildCard());
        cards.add(createMockedCard(secondCard));
        cards.add(createMockedCard(thirdCard));
        testSuccessfulTradeIn(previousTrades, cards, expected);
    }

    @Test
    public void test04_startTrade_withTenPrevious_tradeFourTimes_returnsFiftyFive() {
        Set<Card> cards = new HashSet<>();
        for (PieceType type : PieceType.values()) {
            cards.add(createMockedCard(type));
        }

        TradeInManager tradeIn = new TradeInManager();
        tradeIn.setSetsTradedIn(10);
        int expected = 40;
        for (int i = 0; i < 4; i++) {
            int actual = tradeIn.startTrade(cards);
            assertEquals(expected, actual);
            expected += 5;
        }

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

    @Test
    public void test05_startTrade_withFourteenPrevious_throwsException() {
        Set<Card> cards = new HashSet<>();
        for (PieceType type : PieceType.values()) {
            cards.add(createMockedCard(type));
        }

        TradeInManager tradeIn = new TradeInManager();
        tradeIn.setSetsTradedIn(14);
        String expected = "No more cards to trade in";
        Exception exception = assertThrows(IllegalStateException.class, () -> tradeIn.startTrade(cards));
        assertEquals(expected, exception.getMessage());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

    private static Stream<Arguments> invalidTradeInGenerator() {
        return Stream.of(
                Arguments.of(PieceType.INFANTRY, PieceType.CAVALRY, PieceType.CAVALRY)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTradeInGenerator")
    public void test06_startTrade_withInvalidCards_throwsException(PieceType first, PieceType second, PieceType third) {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedCard(first));
        cards.add(createMockedCard(second));
        cards.add(createMockedCard(third));

        TradeInManager tradeIn = new TradeInManager();
        String expected = "Invalid trade in set";
        Exception exception = assertThrows(IllegalStateException.class, () -> tradeIn.startTrade(cards));
        assertEquals(expected, exception.getMessage());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }
}
