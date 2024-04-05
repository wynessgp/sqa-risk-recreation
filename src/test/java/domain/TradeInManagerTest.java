package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TradeInManagerTest {

    private TradeInManager tradeMgrUnderTest;
    private Set<Card> cards;
    private Card wildCard;
    private Card wildCard2;
    private Card infantryCard;
    private Card infantryCard2;
    private Card infantryCard3;
    private Card cavalryCard;
    private Card cavalryCard2;
    private Card cavalryCard3;
    private Card artilleryCard;
    private Card artilleryCard2;
    private Card artilleryCard3;

    private Card createMockCard(boolean isWild, PieceType pieceType) {
        Card card = EasyMock.createMock(Card.class);
        EasyMock.expect(card.isWild()).andReturn(isWild).anyTimes();
        for (PieceType type : PieceType.values()) {
            if (type.equals(pieceType) && !isWild) {
                EasyMock.expect(card.matchesPieceType(type)).andReturn(true).anyTimes();
            } else {
                EasyMock.expect(card.matchesPieceType(type)).andReturn(false).anyTimes();
            }
        }
        EasyMock.replay(card);
        return card;
    }

    //mock setup for testing verifyValidCombo() and startTrade()
    @BeforeEach
    public void setUp() {
        tradeMgrUnderTest = new TradeInManager();

        cards = new HashSet<>();
        wildCard = createMockCard(true, PieceType.INFANTRY);
        wildCard2 = createMockCard(true, PieceType.INFANTRY);
        infantryCard = createMockCard(false, PieceType.INFANTRY);
        infantryCard2 = createMockCard(false, PieceType.INFANTRY);
        infantryCard3 = createMockCard(false, PieceType.INFANTRY);
        cavalryCard = createMockCard(false, PieceType.CAVALRY);
        cavalryCard2 = createMockCard(false, PieceType.CAVALRY);
        cavalryCard3 = createMockCard(false, PieceType.CAVALRY);
        artilleryCard = createMockCard(false, PieceType.ARTILLERY);
        artilleryCard2 = createMockCard(false, PieceType.ARTILLERY);
        artilleryCard3 = createMockCard(false, PieceType.ARTILLERY);
    }

    @AfterEach
    public void tearDown() {
        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }

    //test for verifyValidCombo()
    @Test
    public void test00_verifyValidCombo_twoWild_expectedException() {
        cards.add(wildCard);
        cards.add(wildCard2);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }

    @Test
    public void test01_verifyValidCombo_oneWildOneInfantry_expectedException() {
        cards.add(infantryCard);
        cards.add(wildCard);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }

    @Test
    public void test02_verifyValidCombo_twoInfantry_expectedException() {
        cards.add(infantryCard);
        cards.add(infantryCard2);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }

    @Test
    public void test03_verifyValidCombo_twoCavalry_expectedException() {
        cards.add(cavalryCard);
        cards.add(cavalryCard2);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }

    @Test
    public void test04_verifyValidCombo_twoArtillery_expectedException() {
        cards.add(artilleryCard);
        cards.add(artilleryCard2);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }

    @Test
    public void test05_verifyValidCombo_noInfantry_expectedFalse() {
        cards.add(cavalryCard);
        cards.add(artilleryCard);
        cards.add(artilleryCard2);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test06_verifyValidCombo_noCavalry_expectedFalse() {
        cards.add(artilleryCard);
        cards.add(infantryCard);
        cards.add(infantryCard2);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test07_verifyValidCombo_noArtillery_expectedFalse() {
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(cavalryCard2);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test08_verifyValidCombo_oneWildSetOf3_expectedTrue() {
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(infantryCard2);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test09_verifyValidCombo_twoWildSetOf3_expectedTrue() {
        cards.add(wildCard);
        cards.add(wildCard2);
        cards.add(infantryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test10_verifyValidCombo_oneOfEachSetOf3_expectedTrue() {
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test11_verifyValidCombo_setOf3Infantry_expectedTrue() {
        cards.add(infantryCard);
        cards.add(infantryCard2);
        cards.add(infantryCard3);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test12_verifyValidCombo_setOf3Cavalry_expectedTrue() {
        cards.add(cavalryCard);
        cards.add(cavalryCard2);
        cards.add(cavalryCard3);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test13_verifyValidCombo_setOf3Artillery_expectedTrue() {
        cards.add(artilleryCard);
        cards.add(artilleryCard2);
        cards.add(artilleryCard3);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test14_verifyValidCombo_twoWildSetOf4_expectedException() {
        cards.add(wildCard);
        cards.add(wildCard2);
        cards.add(infantryCard);
        cards.add(infantryCard2);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }

    @Test
    public void test15_verifyValidCombo_twoAndTwoSetOf4_expectedException() {
        cards.add(artilleryCard);
        cards.add(artilleryCard2);
        cards.add(infantryCard);
        cards.add(infantryCard2);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }

    @Test
    public void test16_verifyValidCombo_oneAndThreeSetOf4_expectedException() {
        cards.add(artilleryCard);
        cards.add(infantryCard2);
        cards.add(infantryCard3);
        cards.add(infantryCard);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }

    @Test
    public void test17_verifyValidCombo_oneOfEachWithWildSetOf4_expectedException() {
        cards.add(wildCard);
        cards.add(artilleryCard);
        cards.add(cavalryCard);
        cards.add(infantryCard);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.verifyValidCombo(cards);
        });
    }


    //test for calculateNumNewPieces()
    private static Stream<Object[]> calcPiecesProvider() {
        return Stream.of(
                new Object[]{0, 4},
                new Object[]{4, 12},
                new Object[]{5, 15},
                new Object[]{13, 55}
        );
    }

    @ParameterizedTest
    @MethodSource("calcPiecesProvider")
    public void test18_calculateNumNewPieces_withValidState_expectedInteger(int tradedIn, int expectedPieces) {
        for (int i = 0; i < tradedIn; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertEquals(expectedPieces, tradeMgrUnderTest.calculateNumNewPieces());
    }

    @Test
    public void test19_calculateNumNewPieces_allCardsTradedIn_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertThrows(IllegalStateException.class, () -> {
            tradeMgrUnderTest.calculateNumNewPieces();
        });
    }


    //test for updateSetsTradedIn()
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 13})
    public void test20_updateSetsTradeIn_withValidState_expectedTrue(int tradedIn) {
        for (int i = 0; i < tradedIn; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test21_updateSetsTradeIn_MaxIntTradedInSets_expectedFalse() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertFalse(tradeMgrUnderTest.updateSetsTradedIn());
    }


    //test for startTrade()
    @Test
    public void test22_startTrade_oneInfantryOneArtillery_expectedException() {
        //check number of pieces given to player
        cards.add(infantryCard);
        cards.add(artilleryCard);

        String expectedMessage = "Has to have exactly 3 cards to trade in";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test23_startTrade_oneCavalryOneWild_expectedException() {
        tradeMgrUnderTest.updateSetsTradedIn();
        cards.add(cavalryCard);
        cards.add(wildCard);

        String expectedMessage = "Has to have exactly 3 cards to trade in";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test24_startTrade_twoWildCardTwoInfantryCardCollection_expectedException() {
        cards.add(wildCard);
        cards.add(wildCard2);
        cards.add(infantryCard);
        cards.add(infantryCard2);

        String expectedMessage = "Has to have exactly 3 cards to trade in";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test25_startTrade_twoInfantryCardTwoCavalryCardCollection_expectedException() {
        for (int i = 0; i < 3; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(infantryCard2);
        cards.add(cavalryCard);
        cards.add(cavalryCard2);

        String expectedMessage = "Has to have exactly 3 cards to trade in";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test26_startTrade_oneArtilleryCardThreeInfantryCardCollection_expectedException() {
        for (int i = 0; i < 13; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(artilleryCard);
        cards.add(infantryCard);
        cards.add(infantryCard2);
        cards.add(infantryCard3);

        String expectedMessage = "Has to have exactly 3 cards to trade in";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test27_startTrade_oneOfEachCardSizeFourCollection_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        String expectedMessage = "Has to have exactly 3 cards to trade in";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Object[]> startTradeValidProvider() {
        return Stream.of(
                new Object[]{0, 4, 1},
                new Object[]{4, 12, 5},
                new Object[]{5, 15, 6},
                new Object[]{13, 55, 14}
        );
    }

    private static Stream<Object[]> startTradeInvalidProvider() {
        return Stream.of(
                new Object[]{0, 0, 0},
                new Object[]{4, 0, 4},
                new Object[]{5, 0, 5},
                new Object[]{13, 0, 13}
        );
    }

    @ParameterizedTest
    @MethodSource("startTradeInvalidProvider")
    public void test28_startTrade_oneInfantryTwoCavalryCardCollection_expected0(
            int tradedIns, int expectedPieces, int expectedSetsAfter) {
        for (int i = 0; i < tradedIns; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(cavalryCard2);

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @ParameterizedTest
    @MethodSource("startTradeValidProvider")
    public void test29_startTrade_oneWildCardTwoInfantryCardCollection_expectedCorrectIntValues(
            int tradedIns, int expectedPieces, int expectedSetsAfter) {
        for (int i = 0; i < tradedIns; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(infantryCard2);

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @ParameterizedTest
    @MethodSource("startTradeValidProvider")
    public void test30_startTrade_oneWildCardOneCavalryCardOneArtilleryCardCollection_expectedCorrectIntValues(
            int tradedIns, int expectedPieces, int expectedSetsAfter) {
        for (int i = 0; i < tradedIns; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @ParameterizedTest
    @MethodSource("startTradeValidProvider")
    public void test31_startTrade_twoWildCardOneInfantryCardCollection_expectedCorrectIntValues(
            int tradedIns, int expectedPieces, int expectedSetsAfter) {
        for (int i = 0; i < tradedIns; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(wildCard2);
        cards.add(infantryCard);

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @ParameterizedTest
    @MethodSource("startTradeValidProvider")
    public void test32_startTrade_oneInfantryCardOneCavalryCardOneArtilleryCardCollection_expectedCorrectIntValues(
            int tradedIns, int expectedPieces, int expectedSetsAfter) {
        for (int i = 0; i < 5; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @ParameterizedTest
    @MethodSource("startTradeValidProvider")
    public void test33_startTrade_threeInfantryCardCollection_expectedCorrectIntValues(
            int tradedIns, int expectedPieces, int expectedSetsAfter) {
        for (int i = 0; i < tradedIns; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(infantryCard2);
        cards.add(infantryCard3);

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @ParameterizedTest
    @MethodSource("startTradeValidProvider")
    public void test34_startTrade_threeCavalryCardCollection_expectedCorrectIntValues(
            int tradedIns, int expectedPieces, int expectedSetsAfter) {
        for (int i = 0; i < tradedIns; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(cavalryCard);
        cards.add(cavalryCard2);
        cards.add(cavalryCard3);

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @ParameterizedTest
    @MethodSource("startTradeValidProvider")
    public void test35_startTrade_threeArtilleryCardCollection_expectedCorrectIntValues(
            int tradedIns, int expectedPieces, int expectedSetsAfter) {
        for (int i = 0; i < tradedIns; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(artilleryCard);
        cards.add(artilleryCard2);
        cards.add(artilleryCard3);

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test36_startTrade_14TradedInOneInfantryTwoCavalry_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(cavalryCard2);

        String expectedMessage = "Should not have enough cards to be traded in more than 14 times";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test37_startTrade_14TradedInOneWildTwoInfantry_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(infantryCard2);

        String expectedMessage = "Should not have enough cards to be traded in more than 14 times";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test38_startTrade_14TradedInOneWildOneCavalryOneArtillery_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        String expectedMessage = "Should not have enough cards to be traded in more than 14 times";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test39_startTrade_14TradedInTwoWildOneInfantry_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(wildCard2);
        cards.add(infantryCard);

        String expectedMessage = "Should not have enough cards to be traded in more than 14 times";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test40_startTrade_14TradedInOneInfantryOneCavalryOneArtillery_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        String expectedMessage = "Should not have enough cards to be traded in more than 14 times";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test41_startTrade_14TradedInThreeInfantry_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(infantryCard2);
        cards.add(infantryCard3);

        String expectedMessage = "Should not have enough cards to be traded in more than 14 times";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test42_startTrade_14TradedInThreeCavalry_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(cavalryCard);
        cards.add(cavalryCard2);
        cards.add(cavalryCard3);

        String expectedMessage = "Should not have enough cards to be traded in more than 14 times";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test43_startTrade_14TradedInThreeArtillery_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(artilleryCard);
        cards.add(artilleryCard2);
        cards.add(artilleryCard3);

        String expectedMessage = "Should not have enough cards to be traded in more than 14 times";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

}
