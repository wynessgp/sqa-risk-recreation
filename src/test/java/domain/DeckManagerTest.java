package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckManagerTest {
    private DeckManager deckManager;
    List<Card> cards;
    private Card wildCard;
    private Card alaskaCard;
    private Card albertaCard;
    private Card centralAmericaCard;
    private Card easternUnitedStatesCard;
    private Card greenlandCard;
    private Card northwestTerritoryCard;
    private Card ontarioCard;
    private Card quebecCard;
    private Card westernUnitedStatesCard;
    private Card argentinaCard;
    private Card brazilCard;
    private Card peruCard;
    private Card venezuelaCard;
    private Card easternEuropeCard;
    private Card greatBritainCard;
    private Card icelandCard;
    private Card northernEuropeCard;
    private Card scandinaviaCard;
    private Card southernEuropeCard;
    private Card westernEuropeCard;
    private Card congoCard;
    private Card eastAfricaCard;
    private Card egyptCard;
    private Card madagascarCard;
    private Card northAfricaCard;
    private Card southAfricaCard;
    private Card afghanistanCard;
    private Card chinaCard;
    private Card indiaCard;
    private Card irkutskCard;
    private Card japanCard;
    private Card kamchatkaCard;
    private Card middleEastCard;
    private Card mongoliaCard;
    private Card siamCard;
    private Card siberiaCard;
    private Card uralCard;
    private Card yakutskCard;
    private Card easternAustraliaCard;
    private Card indonesiaCard;
    private Card newGuineaCard;
    private Card westernAustraliaCard;

    @BeforeEach
    public void setup() {
        deckManager = new DeckManager();

        cards = new ArrayList<>();

        //Mocks
        wildCard = EasyMock.createMock(Card.class);
        alaskaCard = EasyMock.createMock(Card.class);
        albertaCard = EasyMock.createMock(Card.class);
        centralAmericaCard = EasyMock.createMock(Card.class);
        easternUnitedStatesCard = EasyMock.createMock(Card.class);
        greenlandCard = EasyMock.createMock(Card.class);
        northwestTerritoryCard = EasyMock.createMock(Card.class);
        ontarioCard = EasyMock.createMock(Card.class);
        quebecCard = EasyMock.createMock(Card.class);
        westernUnitedStatesCard = EasyMock.createMock(Card.class);
        argentinaCard = EasyMock.createMock(Card.class);
        brazilCard = EasyMock.createMock(Card.class);
        peruCard = EasyMock.createMock(Card.class);
        venezuelaCard = EasyMock.createMock(Card.class);
        easternEuropeCard = EasyMock.createMock(Card.class);
        greatBritainCard = EasyMock.createMock(Card.class);
        icelandCard = EasyMock.createMock(Card.class);
        northernEuropeCard = EasyMock.createMock(Card.class);
        scandinaviaCard = EasyMock.createMock(Card.class);
        southernEuropeCard = EasyMock.createMock(Card.class);
        westernEuropeCard = EasyMock.createMock(Card.class);
        congoCard = EasyMock.createMock(Card.class);
        eastAfricaCard = EasyMock.createMock(Card.class);
        egyptCard = EasyMock.createMock(Card.class);
        madagascarCard = EasyMock.createMock(Card.class);
        northAfricaCard = EasyMock.createMock(Card.class);
        southAfricaCard = EasyMock.createMock(Card.class);
        afghanistanCard = EasyMock.createMock(Card.class);
        chinaCard = EasyMock.createMock(Card.class);
        indiaCard = EasyMock.createMock(Card.class);
        irkutskCard = EasyMock.createMock(Card.class);
        japanCard = EasyMock.createMock(Card.class);
        kamchatkaCard = EasyMock.createMock(Card.class);
        middleEastCard = EasyMock.createMock(Card.class);
        mongoliaCard = EasyMock.createMock(Card.class);
        siamCard = EasyMock.createMock(Card.class);
        siberiaCard = EasyMock.createMock(Card.class);
        uralCard = EasyMock.createMock(Card.class);
        yakutskCard = EasyMock.createMock(Card.class);
        easternAustraliaCard = EasyMock.createMock(Card.class);
        indonesiaCard = EasyMock.createMock(Card.class);
        newGuineaCard = EasyMock.createMock(Card.class);
        westernAustraliaCard = EasyMock.createMock(Card.class);

        EasyMock.expect(alaskaCard.matchesTerritory(TerritoryType.ALASKA)).andReturn(true).anyTimes();
        EasyMock.expect(albertaCard.matchesTerritory(TerritoryType.ALBERTA)).andReturn(true).anyTimes();
        EasyMock.expect(centralAmericaCard.matchesTerritory(TerritoryType.CENTRAL_AMERICA)).andReturn(true).anyTimes();
        EasyMock.expect(easternUnitedStatesCard.matchesTerritory(TerritoryType.EASTERN_UNITED_STATES)).andReturn(true).anyTimes();
        EasyMock.expect(greenlandCard.matchesTerritory(TerritoryType.GREENLAND)).andReturn(true).anyTimes();
        EasyMock.expect(northwestTerritoryCard.matchesTerritory(TerritoryType.NORTHWEST_TERRITORY)).andReturn(true).anyTimes();
        EasyMock.expect(ontarioCard.matchesTerritory(TerritoryType.ONTARIO)).andReturn(true).anyTimes();
        EasyMock.expect(quebecCard.matchesTerritory(TerritoryType.QUEBEC)).andReturn(true).anyTimes();
        EasyMock.expect(westernUnitedStatesCard.matchesTerritory(TerritoryType.WESTERN_UNITED_STATES)).andReturn(true).anyTimes();
        EasyMock.expect(argentinaCard.matchesTerritory(TerritoryType.ARGENTINA)).andReturn(true).anyTimes();
        EasyMock.expect(brazilCard.matchesTerritory(TerritoryType.BRAZIL)).andReturn(true).anyTimes();
        EasyMock.expect(peruCard.matchesTerritory(TerritoryType.PERU)).andReturn(true).anyTimes();
        EasyMock.expect(venezuelaCard.matchesTerritory(TerritoryType.VENEZUELA)).andReturn(true).anyTimes();
        EasyMock.expect(easternEuropeCard.matchesTerritory(TerritoryType.EASTERN_EUROPE)).andReturn(true).anyTimes();
        EasyMock.expect(greatBritainCard.matchesTerritory(TerritoryType.GREAT_BRITAIN)).andReturn(true).anyTimes();
        EasyMock.expect(icelandCard.matchesTerritory(TerritoryType.ICELAND)).andReturn(true).anyTimes();
        EasyMock.expect(northernEuropeCard.matchesTerritory(TerritoryType.NORTHERN_EUROPE)).andReturn(true).anyTimes();
        EasyMock.expect(scandinaviaCard.matchesTerritory(TerritoryType.SCANDINAVIA)).andReturn(true).anyTimes();
        EasyMock.expect(southernEuropeCard.matchesTerritory(TerritoryType.SOUTHERN_EUROPE)).andReturn(true).anyTimes();
        EasyMock.expect(westernEuropeCard.matchesTerritory(TerritoryType.WESTERN_EUROPE)).andReturn(true).anyTimes();
        EasyMock.expect(congoCard.matchesTerritory(TerritoryType.CONGO)).andReturn(true).anyTimes();
        EasyMock.expect(eastAfricaCard.matchesTerritory(TerritoryType.EAST_AFRICA)).andReturn(true).anyTimes();
        EasyMock.expect(egyptCard.matchesTerritory(TerritoryType.EGYPT)).andReturn(true).anyTimes();
        EasyMock.expect(madagascarCard.matchesTerritory(TerritoryType.MADAGASCAR)).andReturn(true).anyTimes();
        EasyMock.expect(northAfricaCard.matchesTerritory(TerritoryType.NORTH_AFRICA)).andReturn(true).anyTimes();
        EasyMock.expect(southAfricaCard.matchesTerritory(TerritoryType.SOUTH_AFRICA)).andReturn(true).anyTimes();
        EasyMock.expect(afghanistanCard.matchesTerritory(TerritoryType.AFGHANISTAN)).andReturn(true).anyTimes();
        EasyMock.expect(chinaCard.matchesTerritory(TerritoryType.CHINA)).andReturn(true).anyTimes();
        EasyMock.expect(indiaCard.matchesTerritory(TerritoryType.INDIA)).andReturn(true).anyTimes();
        EasyMock.expect(irkutskCard.matchesTerritory(TerritoryType.IRKUTSK)).andReturn(true).anyTimes();
        EasyMock.expect(japanCard.matchesTerritory(TerritoryType.JAPAN)).andReturn(true).anyTimes();
        EasyMock.expect(kamchatkaCard.matchesTerritory(TerritoryType.KAMCHATKA)).andReturn(true).anyTimes();
        EasyMock.expect(middleEastCard.matchesTerritory(TerritoryType.MIDDLE_EAST)).andReturn(true).anyTimes();
        EasyMock.expect(mongoliaCard.matchesTerritory(TerritoryType.MONGOLIA)).andReturn(true).anyTimes();
        EasyMock.expect(siamCard.matchesTerritory(TerritoryType.SIAM)).andReturn(true).anyTimes();
        EasyMock.expect(siberiaCard.matchesTerritory(TerritoryType.SIBERIA)).andReturn(true).anyTimes();
        EasyMock.expect(uralCard.matchesTerritory(TerritoryType.URAL)).andReturn(true).anyTimes();
        EasyMock.expect(yakutskCard.matchesTerritory(TerritoryType.YAKUTSK)).andReturn(true).anyTimes();
        EasyMock.expect(easternAustraliaCard.matchesTerritory(TerritoryType.EASTERN_AUSTRALIA)).andReturn(true).anyTimes();
        EasyMock.expect(indonesiaCard.matchesTerritory(TerritoryType.INDONESIA)).andReturn(true).anyTimes();
        EasyMock.expect(newGuineaCard.matchesTerritory(TerritoryType.NEW_GUINEA)).andReturn(true).anyTimes();
        EasyMock.expect(westernAustraliaCard.matchesTerritory(TerritoryType.WESTERN_AUSTRALIA)).andReturn(true).anyTimes();

        EasyMock.replay(
                alaskaCard,
                albertaCard,
                centralAmericaCard,
                easternUnitedStatesCard,
                greenlandCard,
                northwestTerritoryCard,
                ontarioCard,
                quebecCard,
                westernUnitedStatesCard,
                argentinaCard,
                brazilCard,
                peruCard,
                venezuelaCard,
                easternEuropeCard,
                greatBritainCard,
                icelandCard,
                northernEuropeCard,
                scandinaviaCard,
                southernEuropeCard,
                westernEuropeCard,
                congoCard,
                eastAfricaCard,
                egyptCard,
                madagascarCard,
                northAfricaCard,
                southAfricaCard,
                afghanistanCard,
                chinaCard,
                indiaCard,
                irkutskCard,
                japanCard,
                kamchatkaCard,
                middleEastCard,
                mongoliaCard,
                siamCard,
                siberiaCard,
                uralCard,
                yakutskCard,
                easternAustraliaCard,
                indonesiaCard,
                newGuineaCard,
                westernAustraliaCard
        );

        //Add these cards to the list
        cards.add(wildCard);
        cards.add(wildCard);
        cards.add(alaskaCard);
        cards.add(albertaCard);
        cards.add(centralAmericaCard);
        cards.add(easternUnitedStatesCard);
        cards.add(greenlandCard);
        cards.add(northwestTerritoryCard);
        cards.add(ontarioCard);
        cards.add(quebecCard);
        cards.add(westernUnitedStatesCard);
        cards.add(argentinaCard);
        cards.add(brazilCard);
        cards.add(peruCard);
        cards.add(venezuelaCard);
        cards.add(easternEuropeCard);
        cards.add(greatBritainCard);
        cards.add(icelandCard);
        cards.add(northernEuropeCard);
        cards.add(scandinaviaCard);
        cards.add(southernEuropeCard);
        cards.add(westernEuropeCard);
        cards.add(congoCard);
        cards.add(eastAfricaCard);
        cards.add(egyptCard);
        cards.add(madagascarCard);
        cards.add(northAfricaCard);
        cards.add(southAfricaCard);
        cards.add(afghanistanCard);
        cards.add(chinaCard);
        cards.add(indiaCard);
        cards.add(irkutskCard);
        cards.add(japanCard);
        cards.add(kamchatkaCard);
        cards.add(middleEastCard);
        cards.add(mongoliaCard);
        cards.add(siamCard);
        cards.add(siberiaCard);
        cards.add(uralCard);
        cards.add(yakutskCard);
        cards.add(easternAustraliaCard);
        cards.add(indonesiaCard);
        cards.add(newGuineaCard);
        cards.add(westernAustraliaCard);
    }

    @Test
    public void test01_initDeck_EmptyCollection_ExpectSuccess() {
        boolean result = deckManager.initDeck();
        assertTrue(result);

        assertEquals(44, deckManager.getDeckSize());
    }

    @Test
    public void test02_initDeck_OneElementCollection_ExpectException() {
        List<Card> nonEmptyDeck = new ArrayList<>();
        nonEmptyDeck.add(new TerritoryCard(TerritoryType.ALBERTA, PieceType.INFANTRY));
        deckManager = new DeckManager(nonEmptyDeck);
        assertThrows(IllegalStateException.class, () -> deckManager.initDeck());
    }

    @Test
    public void test03_shuffle_EmptyList_ExpectFalseAndEmptyList() {
        deckManager = new DeckManager(new ArrayList<>());
        boolean result = deckManager.shuffle();
        assertFalse(result);
        assertEquals(0, deckManager.getDeckSize());
    }

    @Test
    public void test04_shuffle_OneElementList_ExpectTrueAndSameList() {
        List<Card> singleCardDeck = new ArrayList<>();
        singleCardDeck.add(new TerritoryCard(TerritoryType.ALBERTA, PieceType.INFANTRY));
        deckManager = new DeckManager(singleCardDeck);
        boolean result = deckManager.shuffle();
        assertTrue(result);
        assertEquals(1, deckManager.getDeckSize());
    }

}
