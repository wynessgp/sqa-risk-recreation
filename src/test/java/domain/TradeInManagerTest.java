package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TradeInManagerTest {

    private TradeInManager tradeMgrUnderTest;
    private Set<Card> cards;
    private Card wildCard, infantryCard, cavalryCard, artilleryCard;

    //mock setup
    @BeforeEach
    public void setUp() {
        tradeMgrUnderTest = new TradeInManager();

        cards = new HashSet<>();
        wildCard = EasyMock.createMock(Card.class);
        infantryCard = EasyMock.createMock(Card.class);
        cavalryCard = EasyMock.createMock(Card.class);
        artilleryCard = EasyMock.createMock(Card.class);

        EasyMock.expect(wildCard.isWild()).andReturn(true).anyTimes();
        EasyMock.expect(infantryCard.matchesPieceType(PieceType.INFANTRY)).andReturn(true).anyTimes();
        EasyMock.expect(cavalryCard.matchesPieceType(PieceType.CAVALRY)).andReturn(true).anyTimes();
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

}
