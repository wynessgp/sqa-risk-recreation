package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TradeInManagerTest {

    private TradeInManager tradeMgrUnderTest;
    private List<Card> cards;
    private Card wildCard, infantryCard, cavalryCard, artilleryCard;

    //mock setup
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
    public void test010_verifyValidCombo_oneOfEachSetOf3_expectedTrue(){
        cards.add(infantryCard);
        cards.add(cavalryCard);
        cards.add(artilleryCard);
        assertTrue(tradeMgrUnderTest.verifyValidCombo(cards));
    }


    @Test
    public void test011_verifyValidCombo_twoWildSetOf4_expectedFalse(){
        cards.add(wildCard);
        cards.add(wildCard);
        cards.add(infantryCard);
        cards.add(infantryCard);
        assertFalse(tradeMgrUnderTest.verifyValidCombo(cards));
    }
}
