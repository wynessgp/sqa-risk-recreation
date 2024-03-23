package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class TradeInManagerTest {

    private TradeInManager tradeMgrUnderTest;
    private List<Card> cards;
    private Card wildCard, infantryCard, cavalryCard, artilleryCard;

    //mock setup for testing verifyValidCombo() and startTrade()
    @BeforeEach
    public void setUp() {
        tradeMgrUnderTest = new TradeInManager();

        cards = new ArrayList<>();
        wildCard = EasyMock.createMock(Card.class);
        infantryCard = EasyMock.createMock(Card.class);
        cavalryCard = EasyMock.createMock(Card.class);
        artilleryCard = EasyMock.createMock(Card.class);

        //wild card
        EasyMock.expect(wildCard.isWild()).andReturn(true).anyTimes();
        EasyMock.expect(wildCard.matchesPieceType(PieceType.INFANTRY)).andReturn(false).anyTimes();
        EasyMock.expect(wildCard.matchesPieceType(PieceType.CAVALRY)).andReturn(false).anyTimes();
        EasyMock.expect(wildCard.matchesPieceType(PieceType.ARTILLERY)).andReturn(false).anyTimes();

        //infantry card
        EasyMock.expect(infantryCard.isWild()).andReturn(false).anyTimes();
        EasyMock.expect(infantryCard.matchesPieceType(PieceType.INFANTRY)).andReturn(true).anyTimes();
        EasyMock.expect(infantryCard.matchesPieceType(PieceType.CAVALRY)).andReturn(false).anyTimes();
        EasyMock.expect(infantryCard.matchesPieceType(PieceType.ARTILLERY)).andReturn(false).anyTimes();

        //calvary card
        EasyMock.expect(cavalryCard.isWild()).andReturn(false).anyTimes();
        EasyMock.expect(cavalryCard.matchesPieceType(PieceType.INFANTRY)).andReturn(false).anyTimes();
        EasyMock.expect(cavalryCard.matchesPieceType(PieceType.CAVALRY)).andReturn(true).anyTimes();
        EasyMock.expect(cavalryCard.matchesPieceType(PieceType.ARTILLERY)).andReturn(false).anyTimes();

        //artillery card
        EasyMock.expect(artilleryCard.isWild()).andReturn(false).anyTimes();
        EasyMock.expect(artilleryCard.matchesPieceType(PieceType.INFANTRY)).andReturn(false).anyTimes();
        EasyMock.expect(artilleryCard.matchesPieceType(PieceType.CAVALRY)).andReturn(false).anyTimes();
        EasyMock.expect(artilleryCard.matchesPieceType(PieceType.ARTILLERY)).andReturn(true).anyTimes();

        EasyMock.replay(wildCard, infantryCard, cavalryCard, artilleryCard);
    }

    @AfterEach
    public void tearDown(){
        EasyMock.verify(wildCard);
        EasyMock.verify(infantryCard);
        EasyMock.verify(cavalryCard);
        EasyMock.verify(artilleryCard);
    }


    //test for verifyValidCombo()
    @Test
    public void test00_verifyValidCombo_emptySet_expectedFalse(){
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test01_verifyValidCombo_oneInfantry_expectedFalse(){
        cards.add(infantryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test02_verifyValidCombo_oneCalvary_expectedFalse(){
        cards.add(cavalryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test03_verifyValidCombo_oneArtillery_expectedFalse(){
        cards.add(artilleryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test04_verifyValidCombo_oneWild_expectedFalse(){
        cards.add(wildCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test05_verifyValidCombo_noInfantry_expectedFalse(){
        cards.add(cavalryCard);
        cards.add(artilleryCard);
        cards.add(artilleryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test06_verifyValidCombo_noCavalry_expectedFalse(){
        cards.add(artilleryCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test07_verifyValidCombo_noArtillery_expectedFalse(){
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(cavalryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test08_verifyValidCombo_oneWildSetOf3_expectedTrue(){
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test09_verifyValidCombo_twoWildSetOf3_expectedTrue(){
        cards.add(wildCard);
        cards.add(wildCard);
        cards.add(infantryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test10_verifyValidCombo_oneOfEachSetOf3_expectedTrue(){
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test11_verifyValidCombo_setOf3Infantry_expectedTrue(){
        cards.add(infantryCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test12_verifyValidCombo_setOf3Cavalry_expectedTrue(){
        cards.add(cavalryCard);
        cards.add(cavalryCard);
        cards.add(cavalryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test13_verifyValidCombo_setOf3Artillery_expectedTrue(){
        cards.add(artilleryCard);
        cards.add(artilleryCard);
        cards.add(artilleryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test14_verifyValidCombo_twoWildSetOf4_expectedFalse(){
        cards.add(wildCard);
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test15_verifyValidCombo_twoAndTwoSetOf4_expectedFalse(){
        cards.add(artilleryCard);
        cards.add(artilleryCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test16_verifyValidCombo_oneAndThreeSetOf4_expectedFalse(){
        cards.add(artilleryCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }

    @Test
    public void test17_verifyValidCombo_oneOfEachWithWildSetOf4_expectedFalse(){
        cards.add(wildCard);
        cards.add(artilleryCard);
        cards.add(cavalryCard);
        cards.add(infantryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }


    //test for calculateNumNewPieces()
    @Test
    public void test18_calculateNumNewPieces_0TradedInSets_expected4(){
        int expected = 4;
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test19_calculateNumNewPieces_1TradedInSets_expected6(){
        int expected = 6;
        tradeMgrUnderTest.updateSetsTradedIn();
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test20_calculateNumNewPieces_2TradedInSets_expected8(){
        int expected = 8;
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test21_calculateNumNewPieces_3TradedInSets_expected10(){
        int expected = 10;
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test22_calculateNumNewPieces_4TradedInSets_expected12(){
        int expected = 12;
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        tradeMgrUnderTest.updateSetsTradedIn();
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test23_calculateNumNewPieces_5TradedInSets_expected15(){
        int expected = 15;
        for(int i = 0; i < 5; i++){
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test24_calculateNumNewPieces_6TradedInSets_expected20(){
        int expected = 20;
        for(int i = 0; i < 6; i++){
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test25_calculateNumNewPieces_13TradedInSets_expected60(){
        int expected = 55;
        for(int i = 0; i < 13; i++){
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }

    @Test
    public void test26_calculateNumNewPieces_allCardsTradedIn_expected0(){
        int expected = 0;
        for(int i = 0; i < 14; i++){
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        int actual = tradeMgrUnderTest.calculateNumNewPieces();
        assertEquals(expected, actual);
    }


    //test for updateSetsTradedIn()
    @Test
    public void test27_updateSetsTradeIn_0TradedInSets_expectedTrue(){
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test28_updateSetsTradeIn_1TradedInSets_expectedTrue(){
        tradeMgrUnderTest.updateSetsTradedIn();
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test29_updateSetsTradeIn_10TradedInSets_expectedTrue(){
        for(int i = 0; i < 10; i++){
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test30_updateSetsTradeIn_MaxIntMinusOneTradedInSets_expectedTrue(){
        for(int i = 0; i < 13; i++){
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertTrue(tradeMgrUnderTest.updateSetsTradedIn());
    }

    @Test
    public void test31_updateSetsTradeIn_MaxIntTradedInSets_expectedFalse(){
        for(int i = 0; i < 14; i++){
            tradeMgrUnderTest.updateSetsTradedIn();
        }
        assertFalse(tradeMgrUnderTest.updateSetsTradedIn());
    }


    //test for startTrade()


}
