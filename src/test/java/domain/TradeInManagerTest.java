package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

//import java.util.ArrayList;
//import java.util.List;
import java.util.HashSet;
import java.util.Set;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    @Test
    public void test18_calculateNumNewPieces_0TradedInSets_expected4() {
        int expected = 4;
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test19_calculateNumNewPieces_1TradedInSets_expected6() {
        int expected = 6;
        tradeMgrUnderTest.updateSetsTradedIn();
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test20_calculateNumNewPieces_2TradedInSets_expected8() {
        int expected = 8;
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test21_calculateNumNewPieces_3TradedInSets_expected10() {
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();

        int expected = 10;
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test22_calculateNumNewPieces_4TradedInSets_expected12() {
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        int expected = 12;
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test23_calculateNumNewPieces_5TradedInSets_expected15() {
        int expected = 15;
        for (int i = 0; i < 5; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test24_calculateNumNewPieces_6TradedInSets_expected20() {
        int expected = 20;
        for (int i = 0; i < 6; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test25_calculateNumNewPieces_13TradedInSets_expected60() {
        int expected = 55;
        for (int i = 0; i < 13; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test26_calculateNumNewPieces_allCardsTradedIn_expectedException() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertThrows(IllegalStateException.class, () -> {
            tradeMgrUnderTest.calculateNumNewPieces();
        });
    }


    //test for updateSetsTradedIn()
    @Test
    public void test27_updateSetsTradeIn_0TradedInSets_expectedTrue() {
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test28_updateSetsTradeIn_1TradedInSets_expectedTrue() {
        tradeMgrUnderTest.updateSetsTradedIn();
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test29_updateSetsTradeIn_10TradedInSets_expectedTrue() {
        for (int i = 0; i < 10; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test30_updateSetsTradeIn_MaxIntMinusOneTradedInSets_expectedTrue() {
        for (int i = 0; i < 13; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test31_updateSetsTradeIn_MaxIntTradedInSets_expectedFalse() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertFalse(tradeMgrUnderTest.updateSetsTradedIn());
    }


    //test for startTrade()
    @Test
    public void test32_startTrade_0setOneInfantryOneArtillery_expectedException() {
        //check number of pieces given to player
        cards.add(infantryCard);
        cards.add(artilleryCard);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
    }

    @Test
    public void test33_startTrade_1setOneCavalryOneWild_expectedException() {
        tradeMgrUnderTest.updateSetsTradedIn();
        cards.add(cavalryCard);
        cards.add(wildCard);
        assertThrows(IllegalArgumentException.class, () -> {
            tradeMgrUnderTest.startTrade(cards);
        });
    }

    @Test
    public void test34_startTrade_5setOneInfantryCardCollection_expected0AndFalse() {
        for (int i = 0; i < 5; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 5;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test35_startTrade_13setOneCavalryCardCollection_expected0AndFalse() {
        for (int i = 0; i < 13; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(cavalryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 13;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test36_startTrade_14setOneArtilleryCardCollection_expected0AndFalse() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(artilleryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 14;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test37_startTrade_2setOneInfantryTwoCavalryCardCollection_expected0AndFalse() {
        for (int i = 0; i < 2; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(cavalryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 2;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test38_startTrade_0setOneWildCardTwoInfantryCardCollection_expected4AndTrue() {
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(infantryCard);

        int expectedPieces = 4;
        int expectedSetsAfter = 1;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test39_startTrade_13setOneWildCardOneCavalryCardOneArtilleryCardCollection_expected55AndTrue() {
        for (int i = 0; i < 13; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        int expectedPieces = 55;
        int expectedSetsAfter = 14;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test40_startTrade_14setOneWildCardOneCavalryCardOneArtilleryCardCollection_expected0AndFalse() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 14;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test41_startTrade_3setTwoWildCardOneInfantryCardCollection_expected10AndTrue() {
        for (int i = 0; i < 3; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(wildCard);
        cards.add(infantryCard);

        int expectedPieces = 10;
        int expectedSetsAfter = 4;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test42_startTrade_5setOneInfantryCardOneCavalryCardOneArtilleryCardCollection_expected15AndTrue() {
        for (int i = 0; i < 5; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        int expectedPieces = 15;
        int expectedSetsAfter = 6;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test43_startTrade_6setThreeInfantryCardCollection_expected20AndTrue() {
        for (int i = 0; i < 6; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(infantryCard);
        cards.add(infantryCard);

        int expectedPieces = 20;
        int expectedSetsAfter = 7;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test44_startTrade_13setThreeCavalryCardCollection_expected55AndTrue() {
        for (int i = 0; i < 13; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(cavalryCard);
        cards.add(cavalryCard);
        cards.add(cavalryCard);

        int expectedPieces = 55;
        int expectedSetsAfter = 14;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test45_startTrade_14setThreeArtilleryCardCollection_expected0AndFalse() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(artilleryCard);
        cards.add(artilleryCard);
        cards.add(artilleryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 14;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test46_startTrade_0setTwoWildCardTwoInfantryCardCollection_expected0AndFalse() {
        cards.add(wildCard);
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(infantryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 0;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test47_startTrade_3setTwoInfantryCardTwoCavalryCardCollection_expected0AndFalse() {
        for (int i = 0; i < 3; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(infantryCard);
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(cavalryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 3;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test48_startTrade_13setOneArtilleryCardThreeInfantryCardCollection_expected0AndFalse() {
        for (int i = 0; i < 13; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(artilleryCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        cards.add(infantryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 13;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }

    @Test
    public void test49_startTrade_14setOneOfEachCardSizeFourCollection_expected0AndFalse() {
        for (int i = 0; i < 14; i++) {
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);

        int expectedPieces = 0;
        int expectedSetsAfter = 14;

        int actualPieces = tradeMgrUnderTest.startTrade(cards);
        int actualSetsAfter = tradeMgrUnderTest.getSetsTradedInSoFar();
        assertEquals(expectedPieces, actualPieces);
        assertEquals(expectedSetsAfter, actualSetsAfter);
    }


}
