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

public class TradeInParserTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 4})
    public void test00_startTrade_withWrongSizeCollection_throwsException(int size) {
        Set<Card> cards = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Card card = EasyMock.createMock(Card.class);
            cards.add(card);
            EasyMock.replay(card);
        }

        TradeInParser tradeIn = new TradeInParser();
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
        TradeInParser tradeIn = new TradeInParser();
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

        TradeInParser tradeIn = new TradeInParser();
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

        TradeInParser tradeIn = new TradeInParser();
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
                Arguments.of(PieceType.INFANTRY, PieceType.CAVALRY, PieceType.CAVALRY),
                Arguments.of(PieceType.INFANTRY, PieceType.ARTILLERY, PieceType.ARTILLERY),
                Arguments.of(PieceType.CAVALRY, PieceType.ARTILLERY, PieceType.ARTILLERY),
                Arguments.of(PieceType.CAVALRY, PieceType.INFANTRY, PieceType.INFANTRY),
                Arguments.of(PieceType.ARTILLERY, PieceType.INFANTRY, PieceType.INFANTRY),
                Arguments.of(PieceType.ARTILLERY, PieceType.CAVALRY, PieceType.CAVALRY)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTradeInGenerator")
    public void test06_startTrade_withInvalidCards_throwsException(PieceType first, PieceType second, PieceType third) {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedCard(first));
        cards.add(createMockedCard(second));
        cards.add(createMockedCard(third));

        TradeInParser tradeIn = new TradeInParser();
        String expected = "Invalid trade in set";
        Exception exception = assertThrows(IllegalStateException.class, () -> tradeIn.startTrade(cards));
        assertEquals(expected, exception.getMessage());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

    @Test
    public void test07_startTrade_withTwoWild_withTwoPrevious_returnsEight() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedWildCard());
        cards.add(createMockedWildCard());
        cards.add(createMockedCard(PieceType.INFANTRY));

        TradeInParser tradeIn = new TradeInParser();
        tradeIn.setSetsTradedIn(2);
        int expected = 8;
        int actual = tradeIn.startTrade(cards);
        assertEquals(expected, actual);

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 4})
    public void test08_getMatchedTerritories_withWrongSizeCollection_throwsException(int size) {
        Set<Card> cards = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Card card = EasyMock.createMock(Card.class);
            cards.add(card);
            EasyMock.replay(card);
        }

        Player player = EasyMock.createMock(Player.class);
        EasyMock.replay(player);

        TradeInParser tradeIn = new TradeInParser();
        String expected = "Invalid number of cards";
        Exception exception = assertThrows(IllegalStateException.class, () ->
                tradeIn.getMatchedTerritories(player, cards));
        assertEquals(expected, exception.getMessage());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    private Card createMockedTerritoryCard(TerritoryType type) {
        Card card = EasyMock.createNiceMock(Card.class);
        EasyMock.expect(card.matchesTerritory(type)).andReturn(true).anyTimes();
        EasyMock.replay(card);
        return card;
    }

    @Test
    public void test09_getMatchedTerritories_withOneWildTwoOthers_returnsEmptySet() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedWildCard());
        cards.add(createMockedTerritoryCard(TerritoryType.ALASKA));
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));

        Player player = EasyMock.createMock(Player.class);
        EasyMock.expect(player.ownsTerritory(TerritoryType.ALASKA)).andReturn(false).anyTimes();
        EasyMock.expect(player.ownsTerritory(TerritoryType.ALBERTA)).andReturn(false).anyTimes();
        EasyMock.replay(player);

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(0, actual.size());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    private Player createMockedPlayer(Set<TerritoryType> territories) {
        Player player = EasyMock.createMock(Player.class);
        for (TerritoryType territory : territories) {
            EasyMock.expect(player.ownsTerritory(territory)).andReturn(true).anyTimes();
        }
        for (TerritoryType territory : Set.of(TerritoryType.values())) {
            if (!territories.contains(territory)) {
                EasyMock.expect(player.ownsTerritory(territory)).andReturn(false).anyTimes();
            }
        }
        EasyMock.replay(player);
        return player;
    }

    @Test
    public void test10_getMatchedTerritories_withOneWildOneMatchOneOther_returnsMatchedCard() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedWildCard());
        cards.add(createMockedTerritoryCard(TerritoryType.ALASKA));
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));

        Player player = createMockedPlayer(Set.of(TerritoryType.ALASKA));

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(1, actual.size());
        assertEquals(TerritoryType.ALASKA, actual.iterator().next());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test11_getMatchedTerritories_withThreeDifferentCardsNoMatches_returnsEmptySet() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));
        cards.add(createMockedTerritoryCard(TerritoryType.CENTRAL_AMERICA));
        cards.add(createMockedTerritoryCard(TerritoryType.EASTERN_UNITED_STATES));

        Player player = createMockedPlayer(Set.of(TerritoryType.ALASKA));

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(0, actual.size());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test12_getMatchedTerritories_withOneWildTwoMatches_returnsMatchedCards() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedWildCard());
        cards.add(createMockedTerritoryCard(TerritoryType.ALASKA));
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));

        Set<TerritoryType> territories = Set.of(TerritoryType.ALASKA, TerritoryType.ALBERTA);
        Player player = createMockedPlayer(territories);

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(territories.size(), actual.size());
        assertEquals(territories, actual);

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test13_getMatchedTerritories_withThreeMatches_returnsMatchedCards() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));
        cards.add(createMockedTerritoryCard(TerritoryType.CENTRAL_AMERICA));
        cards.add(createMockedTerritoryCard(TerritoryType.EASTERN_UNITED_STATES));

        Set<TerritoryType> territories = Set.of(TerritoryType.ALBERTA, TerritoryType.CENTRAL_AMERICA,
                TerritoryType.EASTERN_UNITED_STATES);
        Player player = createMockedPlayer(territories);

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(territories.size(), actual.size());
        assertEquals(territories, actual);

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test14_getMatchedTerritories_withTwoMatchesOneOther_returnsMatchedCards() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedTerritoryCard(TerritoryType.ALASKA));
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));
        cards.add(createMockedTerritoryCard(TerritoryType.CENTRAL_AMERICA));

        Set<TerritoryType> territories = Set.of(TerritoryType.ALBERTA, TerritoryType.CENTRAL_AMERICA);
        Player player = createMockedPlayer(territories);

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(territories.size(), actual.size());
        assertEquals(territories, actual);

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test15_getMatchedTerritories_withOneMatchTwoOthers_returnsMatchedCard() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedTerritoryCard(TerritoryType.ALASKA));
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));
        cards.add(createMockedTerritoryCard(TerritoryType.CENTRAL_AMERICA));

        Set<TerritoryType> territories = Set.of(TerritoryType.ALASKA);
        Player player = createMockedPlayer(territories);

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(territories.size(), actual.size());
        assertEquals(territories, actual);

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test16_getMatchedTerritories_withThreeOthers_returnsEmptySet() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedTerritoryCard(TerritoryType.ALASKA));
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));
        cards.add(createMockedTerritoryCard(TerritoryType.CENTRAL_AMERICA));

        Player player = createMockedPlayer(new HashSet<>());

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(0, actual.size());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test17_getMatchedTerritories_withOneWildTwoOthers_noPlayerMatches_returnsEmptySet() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedWildCard());
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));
        cards.add(createMockedTerritoryCard(TerritoryType.CENTRAL_AMERICA));

        Player player = createMockedPlayer(Set.of(TerritoryType.ALASKA));

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(0, actual.size());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test18_getMatchedTerritories_playerOccupiesAllTerritories_withOneWildTwoOthers_returnsMatchedCards() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedWildCard());
        cards.add(createMockedTerritoryCard(TerritoryType.ALASKA));
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));

        Player player = createMockedPlayer(Set.of(TerritoryType.values()));

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(2, actual.size());
        assertEquals(Set.of(TerritoryType.ALASKA, TerritoryType.ALBERTA), actual);

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }

    @Test
    public void test19_getMatchedTerritories_playerOccupiesAllTerritories_withThreeOthers_returnsMatchedCards() {
        Set<Card> cards = new HashSet<>();
        cards.add(createMockedTerritoryCard(TerritoryType.ALASKA));
        cards.add(createMockedTerritoryCard(TerritoryType.ALBERTA));
        cards.add(createMockedTerritoryCard(TerritoryType.CENTRAL_AMERICA));

        Player player = createMockedPlayer(Set.of(TerritoryType.values()));

        TradeInParser tradeIn = new TradeInParser();
        Set<TerritoryType> actual = tradeIn.getMatchedTerritories(player, cards);
        assertEquals(3, actual.size());
        assertEquals(Set.of(TerritoryType.ALASKA, TerritoryType.ALBERTA, TerritoryType.CENTRAL_AMERICA), actual);

        for (Card card : cards) {
            EasyMock.verify(card);
        }
        EasyMock.verify(player);
    }
}
