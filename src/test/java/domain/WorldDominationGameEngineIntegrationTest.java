package domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class WorldDominationGameEngineIntegrationTest {

    private static final int DEFAULT_NUM_ARMIES_WITH_NO_CONTINENTS = 3;

    private static Stream<Arguments> generateValidPlayerListsSizesThreeThroughSix() {
        Set<Arguments> arguments = new HashSet<>();
        Set<PlayerColor> validPlayers = new HashSet<>(Arrays.asList(PlayerColor.values()));
        validPlayers.remove(PlayerColor.SETUP);

        int size = validPlayers.size();
        for (PlayerColor player : new HashSet<>(validPlayers)) {
            if (size < 3) {
                continue;
            }
            List<PlayerColor> playerListVariant = new ArrayList<>(validPlayers);
            arguments.add(Arguments.of(playerListVariant));
            validPlayers.remove(player);
            size--;
        }
        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("generateValidPlayerListsSizesThreeThroughSix")
    public void test00_assignSetupArmiesToPlayers_playerListInputVaries_expectTrueAndArmiesAssignedCorrectly(
            List<PlayerColor> playerListVariant) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playerListVariant);

        int expectedNumArmies = 35 - ((playerListVariant.size() - 3) * 5);
        for (PlayerColor player : playerListVariant) {
            assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(player));
        }
    }

    @Test
    public void test01_checkIfPlayerOwnsTerritory_doesSetupOwnAllTerritoriesAtInitialization_expectTrue() {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        // don't need to initialize anything else, just make sure the Graph is ready for us to play on.
        for (TerritoryType territory : TerritoryType.values()) {
            assertTrue(unitUnderTest.checkIfPlayerOwnsTerritory(territory, PlayerColor.SETUP));
        }
    }

    private DieRollParser generateMockedParser(List<PlayerColor> players) {
        DieRollParser parser = EasyMock.createMock(DieRollParser.class);
        List<Integer> dieRolls = new ArrayList<>();
        for (int i = players.size(); i > 0; i--) {
            dieRolls.add(i);
        }
        EasyMock.expect(parser.rollDiceToDeterminePlayerOrder(players.size())).andReturn(dieRolls);
        EasyMock.replay(parser);
        return parser;
    }

    @Test
    public void test02_placeNewArmiesInTerritoryScramblePhase_doubleClaimingTerritory_expectException() {
        List<PlayerColor> playerColors = List.of(PlayerColor.RED, PlayerColor.PURPLE, PlayerColor.BLUE);
        // we want to ensure our player colors list ordering stays the same after shuffling, so we need a mock.
        DieRollParser parser = generateMockedParser(playerColors);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playerColors, parser);
        TerritoryType targetTerritory = TerritoryType.ALASKA;
        int numArmiesToPlace = 1;

        // since it is an expectation that the WorldDominationGameEngine handles whose turn it is FOR us,
        // we will try placing an army as RED, then the current player should be PURPLE.
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(targetTerritory, numArmiesToPlace));

        assertEquals(PlayerColor.PURPLE, unitUnderTest.getCurrentPlayer());

        // now we'll try to place an army in the same place RED just did.
        String expectedMessage = "Cannot place armies in a claimed territory until the scramble phase is over";
        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(targetTerritory, numArmiesToPlace));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        EasyMock.verify(parser);
    }

    @ParameterizedTest
    @MethodSource("generateValidPlayerListsSizesThreeThroughSix")
    public void test03_placeNewArmiesInTerritoryScramblePhase_listSizeVaries_expectFullCycleOfPlayersIsRepresented(
            List<PlayerColor> players) {
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);
        List<TerritoryType> territories = List.of(TerritoryType.values());
        int numArmiesToPlace = 1;

        // let everyone go once, with a valid territory. Check that it wraps back around as well.
        for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
            assertEquals(players.get(playerIndex), unitUnderTest.getCurrentPlayer());
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(territories.get(playerIndex), numArmiesToPlace));
        }

        assertEquals(players.get(0), unitUnderTest.getCurrentPlayer());
        EasyMock.verify(parser);
    }

    @ParameterizedTest
    @MethodSource("generateValidPlayerListsSizesThreeThroughSix")
    public void test04_placeNewArmiesInTerritoryScramblePhase_validInput_addToPlayerSetsWhenClaimingAndTerritoryUpdates(
            List<PlayerColor> players) {
        DieRollParser parser = generateMockedParser(players);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);
        List<TerritoryType> territories = List.of(TerritoryType.values());
        int numArmiesToPlace = 1;

        // here's the tricky bit about testing this: placeArmiesInTerritory automatically advances the
        // player going. So we'll need to ask for a particular player.
        int playerIndex = 0;
        for (; playerIndex < players.size(); playerIndex++) {
            TerritoryType currentTerritory = territories.get(playerIndex);
            PlayerColor currentPlayer = players.get(playerIndex);

            assertTrue(unitUnderTest.placeNewArmiesInTerritory(currentTerritory, numArmiesToPlace));
            assertTrue(unitUnderTest.checkIfPlayerOwnsTerritory(currentTerritory, currentPlayer));

            assertEquals(Set.of(currentTerritory), unitUnderTest.getClaimedTerritoriesForPlayer(currentPlayer));
        }

        // go forward another player's list worth of indices, just to ensure the set can actually grow.
        for (; playerIndex < players.size() * 2; playerIndex++) {
            int previousIterationIndex = playerIndex - players.size();

            TerritoryType previousIterationTerritory = territories.get(previousIterationIndex);
            PlayerColor currentPlayer = players.get(previousIterationIndex);
            TerritoryType currentTerritory = territories.get(playerIndex);

            assertTrue(unitUnderTest.placeNewArmiesInTerritory(currentTerritory, numArmiesToPlace));
            assertTrue(unitUnderTest.checkIfPlayerOwnsTerritory(currentTerritory, currentPlayer));

            assertEquals(Set.of(currentTerritory, previousIterationTerritory),
                    unitUnderTest.getClaimedTerritoriesForPlayer(players.get(previousIterationIndex)));
        }
        EasyMock.verify(parser);
    }

    @ParameterizedTest
    @MethodSource("generateValidPlayerListsSizesThreeThroughSix")
    public void test05_placeNewArmiesInTerritoryScramblePhase_allTerritoriesClaimedAdvancePhaseToSetup(
            List<PlayerColor> players) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players);
        int numArmiesToPlace = 1;

        for (TerritoryType territory : TerritoryType.values()) {
            assertEquals(GamePhase.SCRAMBLE, unitUnderTest.getCurrentGamePhase());
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(territory, numArmiesToPlace));
        }

        assertEquals(GamePhase.SETUP, unitUnderTest.getCurrentGamePhase());
    }

    @Test
    public void test06_placeNewArmiesInTerritorySetupPhase_placingArmiesInTerritoryPlayerDoesNotOwn_expectException() {
        List<PlayerColor> playerColors = List.of(PlayerColor.RED, PlayerColor.PURPLE, PlayerColor.BLUE);
        // we want to ensure our player colors list ordering stays the same after shuffling, so we need a mock.
        DieRollParser parser = generateMockedParser(playerColors);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playerColors, parser);
        TerritoryType targetTerritory = TerritoryType.ALASKA;
        int numArmiesToPlace = 1;

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(targetTerritory, numArmiesToPlace));

        assertEquals(PlayerColor.PURPLE, unitUnderTest.getCurrentPlayer());

        // flip what phase we're in really quickly, to speed up testing. We've already claimed the territory
        // through legitimate playing order, so this should be OK.
        unitUnderTest.setGamePhase(GamePhase.SETUP);

        // now we'll try to place an army in the same place RED just did.
        String expectedMessage = "Cannot place armies on a territory you do not own";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(targetTerritory, numArmiesToPlace));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        EasyMock.verify(parser);
    }

    @ParameterizedTest
    @MethodSource("generateValidPlayerListsSizesThreeThroughSix")
    public void test07_placeNewArmiesInTerritorySetupPhase_listSizeVaries_expectFullCycleOfPlayersIsRepresented(
            List<PlayerColor> players) {
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);
        List<TerritoryType> territories = List.of(TerritoryType.values());
        int numArmiesToPlace = 1;

        // let everyone go once, with a valid territory, just to claim one for each player.
        for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(territories.get(playerIndex), numArmiesToPlace));
        }

        // now change the game's phase to SETUP, and re-iterate over those same territories to check for correctness.
        unitUnderTest.setGamePhase(GamePhase.SETUP);
        for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
            assertEquals(players.get(playerIndex), unitUnderTest.getCurrentPlayer());
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(territories.get(playerIndex), numArmiesToPlace));
        }
        assertEquals(players.get(0), unitUnderTest.getCurrentPlayer());

        EasyMock.verify(parser);
    }

    @ParameterizedTest
    @MethodSource("generateValidPlayerListsSizesThreeThroughSix")
    public void test08_placeNewArmiesInTerritoryMultiPhaseAdvancement_validInput_goesThroughScrambleAndSetupPhases(
            List<PlayerColor> players) {
        DieRollParser parser = generateMockedParser(players);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        // claim all the territories, ensure we enter the setup phase.
        int numArmiesToPlace = 1;
        for (TerritoryType territory : TerritoryType.values()) {
            assertEquals(GamePhase.SCRAMBLE, unitUnderTest.getCurrentGamePhase());
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(territory, numArmiesToPlace));
        }

        assertEquals(GamePhase.SETUP, unitUnderTest.getCurrentGamePhase());

        // in order to enter the placement phase in RISK, we need to exhaust each and every player's armies
        // that they acquire during the game's setup. Let's "peek" into the WorldDominationGameEngine a bit and figure
        // out when that is.
        List<Integer> numArmiesByPlayer = new ArrayList<>();
        for (PlayerColor player : players) {
            numArmiesByPlayer.add(unitUnderTest.getNumArmiesByPlayerColor(player));
        }

        List<TerritoryType> territories = List.of(TerritoryType.values());
        int numArmiesLeftToPlace = numArmiesByPlayer.stream().mapToInt(Integer::intValue).sum();
        while (numArmiesLeftToPlace != 0) {
            assertEquals(GamePhase.SETUP, unitUnderTest.getCurrentGamePhase());

            // determine a "safe" territory for the current player who's going
            PlayerColor currentPlayerGoing = unitUnderTest.getCurrentPlayer();
            TerritoryType safeTerritory = territories.get(players.indexOf(currentPlayerGoing));
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(safeTerritory, numArmiesToPlace));

            numArmiesLeftToPlace--;
            int numExpectedArmiesPlayerHasLeft = numArmiesLeftToPlace / players.size();
            assertEquals(numExpectedArmiesPlayerHasLeft, unitUnderTest.getNumArmiesByPlayerColor(currentPlayerGoing));
        }

        assertEquals(GamePhase.PLACEMENT, unitUnderTest.getCurrentGamePhase());
        EasyMock.verify(parser);
    }

    @Test
    public void test09_placeNewArmiesInTerritoryPlacementPhase_placingArmiesInOtherPlayersTerritory_expectException() {
        // this will look incredibly similar to test06. We want to ensure an exception happens for both
        // phases, because this exception can happen as a result of normal play!
        List<PlayerColor> playerColors = List.of(PlayerColor.RED, PlayerColor.PURPLE, PlayerColor.BLUE);
        // we want to ensure our player colors list ordering stays the same after shuffling, so we need a mock.
        DieRollParser parser = generateMockedParser(playerColors);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playerColors, parser);
        TerritoryType targetTerritory = TerritoryType.ALASKA;
        int numArmiesToPlace = 1;

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(targetTerritory, numArmiesToPlace));
        assertEquals(PlayerColor.PURPLE, unitUnderTest.getCurrentPlayer());

        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        // now we'll try to place an army in the same place RED just did.
        String expectedMessage = "Cannot place armies on a territory you do not own";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(targetTerritory, numArmiesToPlace));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        EasyMock.verify(parser);
    }

    @Test
    public void test10_placeNewArmiesInTerritoryPlacementPhase_validInput_checkForBonusArmiesAtStartOfPlacement() {
        List<PlayerColor> players = List.of(PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.RED);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);
        // 42 territories split among 4 players means ~10 territories each. Nobody will own the entirety of a
        // continent as a result of looping through them for this test, so... 3 armies each.

        for (TerritoryType territory : TerritoryType.values()) { // claim every territory.
            unitUnderTest.placeNewArmiesInTerritory(territory, 1);
        }

        // get the number of armies left to place, so we can check that the bonus is calculated PRECISELY
        // when the game phase changes.
        List<Integer> numArmiesByPlayer = new ArrayList<>();
        for (PlayerColor player : players) {
            numArmiesByPlayer.add(unitUnderTest.getNumArmiesByPlayerColor(player));
        }
        int numArmiesLeftToPlace = numArmiesByPlayer.stream().mapToInt(Integer::intValue).sum();

        int safeTerritoryIndex = TerritoryType.values().length % 4;
        while (numArmiesLeftToPlace != 1) {
            unitUnderTest.placeNewArmiesInTerritory(TerritoryType.values()[safeTerritoryIndex], 1);
            safeTerritoryIndex = (safeTerritoryIndex + 1) % players.size();
            numArmiesLeftToPlace--;
        }
        assertEquals(GamePhase.SETUP, unitUnderTest.getCurrentGamePhase());

        // place the last army.
        unitUnderTest.placeNewArmiesInTerritory(TerritoryType.values()[safeTerritoryIndex], 1);

        assertEquals(GamePhase.PLACEMENT, unitUnderTest.getCurrentGamePhase());
        assertEquals(PlayerColor.YELLOW, unitUnderTest.getCurrentPlayer()); // need to be back on the first player.
        assertEquals(DEFAULT_NUM_ARMIES_WITH_NO_CONTINENTS, unitUnderTest.getNumArmiesByPlayerColor(players.get(0)));
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.values()[0], 1));

        EasyMock.verify(parser);
    }

    @Test
    public void test11_placeNewArmiesInTerritoryPlacementPhase_validInput_placingLastArmyMovesGameToAttackPhase() {
        List<PlayerColor> validPlayersList = List.of(PlayerColor.RED, PlayerColor.PURPLE, PlayerColor.BLUE);
        DieRollParser parser = generateMockedParser(validPlayersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(validPlayersList, parser);

        for (TerritoryType territory : TerritoryType.values()) { // claim every territory.
            unitUnderTest.placeNewArmiesInTerritory(territory, 1);
        }

        List<Integer> numArmiesByPlayer = new ArrayList<>();
        for (PlayerColor player : validPlayersList) {
            numArmiesByPlayer.add(unitUnderTest.getNumArmiesByPlayerColor(player));
        }
        int numArmiesLeftToPlace = numArmiesByPlayer.stream().mapToInt(Integer::intValue).sum();

        int safeTerritoryIndex = TerritoryType.values().length % validPlayersList.size();
        while (numArmiesLeftToPlace != 1) {
            unitUnderTest.placeNewArmiesInTerritory(TerritoryType.values()[safeTerritoryIndex], 1);
            safeTerritoryIndex = (safeTerritoryIndex + 1) % validPlayersList.size();
            numArmiesLeftToPlace--;
        }

        // place the last army.
        unitUnderTest.placeNewArmiesInTerritory(TerritoryType.values()[safeTerritoryIndex], 1);

        // each player will get 4 armies as their bonus here (42 / 3) = 14, integer division by 3 => 4
        // so have them place 3 armies, THEN place the last one and check that we are in the attack phase.
        assertEquals(GamePhase.PLACEMENT, unitUnderTest.getCurrentGamePhase());
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.values()[0],
                DEFAULT_NUM_ARMIES_WITH_NO_CONTINENTS));
        assertEquals(GamePhase.PLACEMENT, unitUnderTest.getCurrentGamePhase());
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.values()[0], 1));
        assertEquals(GamePhase.ATTACK, unitUnderTest.getCurrentGamePhase());

        EasyMock.verify(parser);
    }

    @Test
    public void test12_tradeInCardsPlacementPhase_invalidTradeInSize_expectException() {
        List<PlayerColor> players = List.of(PlayerColor.YELLOW, PlayerColor.BLACK, PlayerColor.GREEN);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        Set<Card> attemptToTradeIn = Set.of(new WildCard(),
                new TerritoryCard(TerritoryType.CENTRAL_AMERICA, PieceType.INFANTRY));

        unitUnderTest.setCardsForPlayer(players.get(0), attemptToTradeIn);
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.tradeInCards(attemptToTradeIn));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Could not trade in cards: Must trade in exactly three cards";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test32_tradeInCardsAttackPhase_tradeInIsNotForced_expectException() {
        List<PlayerColor> players = List.of(PlayerColor.YELLOW, PlayerColor.BLACK, PlayerColor.GREEN);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        Set<Card> attemptToTradeIn = Set.of(new WildCard(),
                new TerritoryCard(TerritoryType.CENTRAL_AMERICA, PieceType.INFANTRY),
                new TerritoryCard(TerritoryType.IRKUTSK, PieceType.ARTILLERY));

        Set<Card> ownedPlayerCards = new HashSet<>(attemptToTradeIn);
        ownedPlayerCards.addAll(Set.of(new WildCard(), new TerritoryCard(TerritoryType.YAKUTSK, PieceType.INFANTRY)));

        unitUnderTest.setCardsForPlayer(players.get(0), ownedPlayerCards);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.tradeInCards(attemptToTradeIn));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Cannot trade in cards in the ATTACK phase unless you have > 5 held!";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test13_tradeInCardsPlacementPhase_playerDoesNotOwnCards_expectException() {
        List<PlayerColor> players = List.of(PlayerColor.YELLOW, PlayerColor.BLACK, PlayerColor.GREEN);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        Set<Card> attemptToTradeIn = Set.of(new WildCard(),
                new TerritoryCard(TerritoryType.CENTRAL_AMERICA, PieceType.INFANTRY),
                new TerritoryCard(TerritoryType.MADAGASCAR, PieceType.ARTILLERY));

        unitUnderTest.setCardsForPlayer(players.get(0), Set.of());
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.tradeInCards(attemptToTradeIn));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Player doesn't own all the selected cards!";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test14_tradeInCardsPlacementPhase_playerOwnsCards_invalidTradeInSet_expectException() {
        List<PlayerColor> players = List.of(PlayerColor.YELLOW, PlayerColor.BLACK, PlayerColor.GREEN);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        Set<Card> attemptToTradeIn = Set.of(new TerritoryCard(TerritoryType.PERU, PieceType.INFANTRY),
                new TerritoryCard(TerritoryType.CENTRAL_AMERICA, PieceType.INFANTRY),
                new TerritoryCard(TerritoryType.MADAGASCAR, PieceType.ARTILLERY));

        unitUnderTest.setCardsForPlayer(players.get(0), attemptToTradeIn);
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.tradeInCards(attemptToTradeIn));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Could not trade in cards: Invalid trade in set";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generatePlayerHeldCardsAndSetsToTradeIn() {
        Set<Arguments> toStream = new HashSet<>();

        WildCard wildCard = new WildCard();
        TerritoryCard congoCard = new TerritoryCard(TerritoryType.CONGO, PieceType.INFANTRY);
        TerritoryCard japanCard = new TerritoryCard(TerritoryType.JAPAN, PieceType.ARTILLERY);
        TerritoryCard ukraineCard = new TerritoryCard(TerritoryType.UKRAINE, PieceType.CAVALRY);
        TerritoryCard brazilCard = new TerritoryCard(TerritoryType.BRAZIL, PieceType.ARTILLERY);
        TerritoryCard greenlandCard = new TerritoryCard(TerritoryType.GREENLAND, PieceType.INFANTRY);
        TerritoryCard newGuineaCard = new TerritoryCard(TerritoryType.NEW_GUINEA, PieceType.CAVALRY);

        // make some sets with differing sizes to ensure we correctly remove 3 cards
        toStream.add(Arguments.of(Set.of(wildCard, brazilCard, greenlandCard),
                Set.of(wildCard, brazilCard, greenlandCard)));
        toStream.add(Arguments.of(Set.of(brazilCard, greenlandCard, newGuineaCard, ukraineCard, congoCard),
                Set.of(greenlandCard, newGuineaCard, brazilCard)));
        toStream.add(Arguments.of(Set.of(wildCard, brazilCard, ukraineCard, newGuineaCard),
                Set.of(brazilCard, newGuineaCard, wildCard)));
        toStream.add(Arguments.of(
                Set.of(newGuineaCard, greenlandCard, brazilCard, ukraineCard, japanCard, congoCard, wildCard),
                Set.of(greenlandCard, ukraineCard, japanCard)));
        toStream.add(Arguments.of(Set.of(greenlandCard, brazilCard, ukraineCard, japanCard, congoCard),
                Set.of(brazilCard, ukraineCard, greenlandCard)));
        toStream.add(Arguments.of(Set.of(wildCard, ukraineCard, brazilCard, japanCard, congoCard, newGuineaCard),
                Set.of(newGuineaCard, wildCard, brazilCard)));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generatePlayerHeldCardsAndSetsToTradeIn")
    public void test15_tradeInCardsPlacementPhase_playerOwnsCards_validTradeIn_ensureCardsGetRemoved(
            Set<Card> playerCards, Set<Card> toTradeIn) {
        List<PlayerColor> players = List.of(PlayerColor.YELLOW, PlayerColor.BLACK, PlayerColor.GREEN);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        unitUnderTest.setCardsForPlayer(players.get(0), playerCards);
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        unitUnderTest.tradeInCards(toTradeIn);

        // check that the player has 3 fewer cards.
        int expectedNumCards = playerCards.size() - 3;
        assertEquals(expectedNumCards, unitUnderTest.getNumCardsForPlayer(players.get(0)));
    }

    @Test
    public void test16_tradeInCardsAttackPhase_validTradeIn_ensureGameIsPutInPlacementPhase() {
        List<PlayerColor> players = List.of(PlayerColor.GREEN, PlayerColor.PURPLE, PlayerColor.BLUE);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        // claim a territory just to ensure we can still place after being booted back to placement phase.
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 1));

        Card brazilCard = new TerritoryCard(TerritoryType.BRAZIL, PieceType.INFANTRY);
        Card irkutskCard = new TerritoryCard(TerritoryType.IRKUTSK, PieceType.ARTILLERY);
        Card wildCard = new WildCard();

        Set<Card> playerCards = Set.of(wildCard, new WildCard(),
                brazilCard,
                new TerritoryCard(TerritoryType.CENTRAL_AMERICA, PieceType.INFANTRY),
                irkutskCard,
                new TerritoryCard(TerritoryType.INDONESIA, PieceType.CAVALRY)); // force a trade in.
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN); // go back to green.
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.setCardsForPlayer(players.get(0), playerCards);

        unitUnderTest.tradeInCards(Set.of(wildCard, brazilCard, irkutskCard));

        assertEquals(GamePhase.PLACEMENT, unitUnderTest.getCurrentGamePhase());
        // should gain 4 armies for the first trade-in, and with 3 users, you start with 35 armies.
        // since we placed one, they should have 38 armies.
        int expectedNumArmies = 38;
        assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(PlayerColor.GREEN));

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 2));
    }

    @Test
    public void test17_tradeInCardsPlacementPhase_playerHasTooManyCards_forceTradeIn_ensurePlayerCanPlaceAfterTrade() {
        List<PlayerColor> players = List.of(PlayerColor.GREEN, PlayerColor.PURPLE, PlayerColor.BLUE);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        // claim a territory just to ensure we can still place after being booted back to placement phase.
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 1));

        WildCard wildCardOne = new WildCard();
        TerritoryCard argentinaCard = new TerritoryCard(TerritoryType.ARGENTINA, PieceType.CAVALRY);
        TerritoryCard uralCard = new TerritoryCard(TerritoryType.URAL, PieceType.ARTILLERY);
        Set<Card> playerCards = Set.of(wildCardOne, new WildCard(),
                new TerritoryCard(TerritoryType.BRAZIL, PieceType.INFANTRY), argentinaCard, uralCard);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN); // go back to green.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        unitUnderTest.setCardsForPlayer(players.get(0), playerCards);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Player cannot place armies while they are holding more than 5 cards!";
        assertEquals(expectedMessage, actualMessage);

        // now go to trade in cards
        Set<Card> toTradeIn = Set.of(wildCardOne, argentinaCard, uralCard);
        unitUnderTest.tradeInCards(toTradeIn);

        // they had 34 armies, trade in should bring them to 38.
        int expectedNumArmies = 38;
        assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(PlayerColor.GREEN));

        // make sure they can place armies in the territory now.
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 2));
        // roll back a player color
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN); // go back to green.
        assertEquals(expectedNumArmies - 2, unitUnderTest.getCurrentPlayerArmiesToPlace());
        // should be three armies in the territory now.
        assertEquals(3, unitUnderTest.getNumberOfArmies(TerritoryType.ALASKA));
    }

    @Test
    public void test18_placeNewArmiesInTerritory_scramblePhase_validInput_playerLosesAnArmy() {
        List<PlayerColor> players = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 1));

        // see how many armies blue has left to place. By default, you get 35 in 3-player risk, so we should
        // have 34 left.
        int expectedNumArmies = 34;
        assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(PlayerColor.BLUE));
    }

    @Test
    public void test19_placeNewArmiesInTerritory_setupPhase_validInput_playerLosesAnArmy() {
        List<PlayerColor> players = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED);
        DieRollParser parser = generateMockedParser(players);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(players, parser);

        // claim this territory; so we should have 34 armies at this point
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 1));

        unitUnderTest.setGamePhase(GamePhase.SETUP);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        // now place an army there and check we've lost an army.
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 1));
        int expectedNumArmies = 33;
        assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(PlayerColor.BLUE));
    }

    private static Stream<Arguments> generateNonAdjacentTerritoryPairs() {
        Set<Arguments> toStream = new HashSet<>();

        toStream.add(Arguments.of(TerritoryType.IRKUTSK, TerritoryType.ALASKA));
        toStream.add(Arguments.of(TerritoryType.PERU, TerritoryType.YAKUTSK));
        toStream.add(Arguments.of(TerritoryType.ALBERTA, TerritoryType.JAPAN));
        toStream.add(Arguments.of(TerritoryType.ARGENTINA, TerritoryType.MADAGASCAR));
        toStream.add(Arguments.of(TerritoryType.CENTRAL_AMERICA, TerritoryType.GREAT_BRITAIN));
        toStream.add(Arguments.of(TerritoryType.INDONESIA, TerritoryType.EASTERN_AUSTRALIA));
        toStream.add(Arguments.of(TerritoryType.CHINA, TerritoryType.GREAT_BRITAIN));
        toStream.add(Arguments.of(TerritoryType.MONGOLIA, TerritoryType.EASTERN_UNITED_STATES));
        toStream.add(Arguments.of(TerritoryType.UKRAINE, TerritoryType.BRAZIL));
        toStream.add(Arguments.of(TerritoryType.AFGHANISTAN, TerritoryType.CONGO));
        toStream.add(Arguments.of(TerritoryType.NEW_GUINEA, TerritoryType.CENTRAL_AMERICA));
        toStream.add(Arguments.of(TerritoryType.SIAM, TerritoryType.JAPAN));
        toStream.add(Arguments.of(TerritoryType.KAMCHATKA, TerritoryType.BRAZIL));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateNonAdjacentTerritoryPairs")
    public void test20_attackTerritory_territoriesAreNotAdjacent_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        // since we aim to utilize as few mocks as possible, this test will be a little long.
        List<PlayerColor> playersList = List.of(PlayerColor.PURPLE, PlayerColor.YELLOW, PlayerColor.GREEN);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for Purple
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for Yellow
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        // place their remaining armies, so we can move into attack.
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.YELLOW);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 5));

        // move into attack
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.attackTerritory(sourceTerritory, destTerritory, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source and destination territory must be two adjacent territories!";
        assertEquals(actualMessage, expectedMessage);
    }

    private static Stream<Arguments> generateAdjacentTerritoryPairs() {
        // need to test an edge of EVERY territory if we want full mutation coverage
        return Stream.of(
                // North America
                Arguments.of(TerritoryType.ALASKA, TerritoryType.NORTHWEST_TERRITORY),
                Arguments.of(TerritoryType.NORTHWEST_TERRITORY, TerritoryType.ALBERTA),
                Arguments.of(TerritoryType.GREENLAND, TerritoryType.ICELAND),
                Arguments.of(TerritoryType.ALBERTA, TerritoryType.ONTARIO),
                Arguments.of(TerritoryType.ONTARIO, TerritoryType.QUEBEC),
                Arguments.of(TerritoryType.QUEBEC, TerritoryType.EASTERN_UNITED_STATES),
                Arguments.of(TerritoryType.WESTERN_UNITED_STATES, TerritoryType.CENTRAL_AMERICA),
                Arguments.of(TerritoryType.EASTERN_UNITED_STATES, TerritoryType.CENTRAL_AMERICA),
                Arguments.of(TerritoryType.CENTRAL_AMERICA, TerritoryType.VENEZUELA),
                // South America
                Arguments.of(TerritoryType.VENEZUELA, TerritoryType.PERU),
                Arguments.of(TerritoryType.PERU, TerritoryType.BRAZIL),
                Arguments.of(TerritoryType.BRAZIL, TerritoryType.NORTH_AFRICA),
                Arguments.of(TerritoryType.ARGENTINA, TerritoryType.PERU),
                // Europe
                Arguments.of(TerritoryType.GREAT_BRITAIN, TerritoryType.ICELAND),
                Arguments.of(TerritoryType.ICELAND, TerritoryType.SCANDINAVIA),
                Arguments.of(TerritoryType.SCANDINAVIA, TerritoryType.UKRAINE),
                Arguments.of(TerritoryType.NORTHERN_EUROPE, TerritoryType.SOUTHERN_EUROPE),
                Arguments.of(TerritoryType.SOUTHERN_EUROPE, TerritoryType.WESTERN_EUROPE),
                Arguments.of(TerritoryType.UKRAINE, TerritoryType.URAL),
                Arguments.of(TerritoryType.WESTERN_EUROPE, TerritoryType.NORTH_AFRICA),
                // Africa
                Arguments.of(TerritoryType.NORTH_AFRICA, TerritoryType.EGYPT),
                Arguments.of(TerritoryType.EGYPT, TerritoryType.EAST_AFRICA),
                Arguments.of(TerritoryType.CONGO, TerritoryType.SOUTH_AFRICA),
                Arguments.of(TerritoryType.EAST_AFRICA, TerritoryType.MIDDLE_EAST),
                Arguments.of(TerritoryType.SOUTH_AFRICA, TerritoryType.MADAGASCAR),
                Arguments.of(TerritoryType.MADAGASCAR, TerritoryType.EAST_AFRICA),
                // Asia
                Arguments.of(TerritoryType.AFGHANISTAN, TerritoryType.MIDDLE_EAST),
                Arguments.of(TerritoryType.MIDDLE_EAST, TerritoryType.INDIA),
                Arguments.of(TerritoryType.URAL, TerritoryType.SIBERIA),
                Arguments.of(TerritoryType.INDIA, TerritoryType.CHINA),
                Arguments.of(TerritoryType.CHINA, TerritoryType.SIAM),
                Arguments.of(TerritoryType.SIBERIA, TerritoryType.IRKUTSK),
                Arguments.of(TerritoryType.SIAM, TerritoryType.INDONESIA),
                Arguments.of(TerritoryType.MONGOLIA, TerritoryType.JAPAN),
                Arguments.of(TerritoryType.IRKUTSK, TerritoryType.KAMCHATKA),
                Arguments.of(TerritoryType.YAKUTSK, TerritoryType.KAMCHATKA),
                Arguments.of(TerritoryType.JAPAN, TerritoryType.KAMCHATKA),
                Arguments.of(TerritoryType.KAMCHATKA, TerritoryType.MONGOLIA),
                // Oceania
                Arguments.of(TerritoryType.INDONESIA, TerritoryType.WESTERN_AUSTRALIA),
                Arguments.of(TerritoryType.NEW_GUINEA, TerritoryType.EASTERN_AUSTRALIA),
                Arguments.of(TerritoryType.WESTERN_AUSTRALIA, TerritoryType.EASTERN_AUSTRALIA),
                Arguments.of(TerritoryType.EASTERN_AUSTRALIA, TerritoryType.NEW_GUINEA)
        );
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test21_attackTerritory_attackerDoesNotOwnSource_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.GREEN,
                PlayerColor.PURPLE);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for Red
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for Yellow
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        // place their remaining armies, so we can move into attack.
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.YELLOW);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 5));

        // move into attack
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.attackTerritory(sourceTerritory, destTerritory, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source territory is not owned by the current player!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test22_attackTerritory_attackerOwnsDestination_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.RED,
                PlayerColor.PURPLE);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for Green
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for Green (again)
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 5));

        // move into attack
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.attackTerritory(sourceTerritory, destTerritory, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Destination territory is owned by the current player!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateAdjacentTerritoryPairsAndIncorrectPhases() {
        List<Arguments> territoryPairs = generateAdjacentTerritoryPairs().collect(Collectors.toList());
        List<GamePhase> illegalGamePhases = new ArrayList<>(List.of(GamePhase.values()));
        illegalGamePhases.remove(GamePhase.ATTACK);
        Set<Arguments> toStream = new HashSet<>();

        for (Arguments territoryPair : territoryPairs) {
            for (GamePhase illegalGamePhase : illegalGamePhases) {
                Object[] territories = territoryPair.get();
                toStream.add(Arguments.of(territories[0], territories[1], illegalGamePhase));
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairsAndIncorrectPhases")
    public void test23_attackTerritory_incorrectGamePhase_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory, GamePhase invalidPhase) {
        List<PlayerColor> playersList = List.of(PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.RED,
                PlayerColor.PURPLE);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for Yellow
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for Green
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.YELLOW);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 5));

        // move into an "illegal" phase
        unitUnderTest.setGamePhase(invalidPhase);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.attackTerritory(sourceTerritory, destTerritory, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Attacking territories is not allowed in any phase besides attack!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateAdjacentTerritoryPairsAndIncorrectAttackerAmounts() {
        List<Arguments> territoryPairs = generateAdjacentTerritoryPairs().collect(Collectors.toList());
        List<Integer> illegalAttackerAmounts = List.of(Integer.MIN_VALUE, -1, 0, 4, 17, Integer.MAX_VALUE);
        Set<Arguments> toStream = new HashSet<>();

        for (Arguments territoryPair : territoryPairs) {
            for (Integer illegalAttackerAmount : illegalAttackerAmounts) {
                Object[] territories = territoryPair.get();
                toStream.add(Arguments.of(territories[0], territories[1], illegalAttackerAmount));
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairsAndIncorrectAttackerAmounts")
    public void test24_attackTerritory_invalidNumAttackers_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int invalidAttackerAmount) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for Blue
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for Black
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLACK);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 5));

        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.attackTerritory(sourceTerritory, destTerritory, invalidAttackerAmount, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Number of armies to attack with must be within [1, 3]!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateAdjacentTerritoryPairsAndInvalidAmountInSourceCombos() {
        List<Arguments> territoryPairs = generateAdjacentTerritoryPairs().collect(Collectors.toList());
        List<Integer> armyInTerritoryAmounts = List.of(1, 1, 1, 2, 2, 3);
        List<Integer> numAttackerAmounts = List.of(1, 2, 3, 2, 3, 3);
        Set<Arguments> toStream = new HashSet<>();

        for (Arguments territoryPair : territoryPairs) {
            int index = 0;
            for (Integer armyInTerritoryAmount : armyInTerritoryAmounts) {
                Object[] territories = territoryPair.get();
                toStream.add(Arguments.of(territories[0], territories[1], armyInTerritoryAmount,
                        numAttackerAmounts.get(index)));
                index++;
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairsAndInvalidAmountInSourceCombos")
    public void test25_attackTerritory_invalidNumArmiesInSource_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int numArmiesInTerritory, int numAttackers) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for Blue
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for Black
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        if (numArmiesInTerritory != 1) { // if it is 1, we will error during placement.
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, numArmiesInTerritory - 1));
        }
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLACK);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 5));

        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.attackTerritory(sourceTerritory, destTerritory, numAttackers, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source territory has too few armies to use in this attack!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateAdjacentTerritoryPairsAndInvalidNumDefenders() {
        List<Arguments> territoryPairs = generateAdjacentTerritoryPairs().collect(Collectors.toList());
        List<Integer> invalidNumDefenders = List.of(Integer.MIN_VALUE, -1, 0, 3, 17, Integer.MAX_VALUE);
        Set<Arguments> toStream = new HashSet<>();

        for (Arguments territoryPair : territoryPairs) {
            for (Integer defenderAmount : invalidNumDefenders) {
                Object[] territories = territoryPair.get();
                toStream.add(Arguments.of(territories[0], territories[1], defenderAmount));
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairsAndInvalidNumDefenders")
    public void test26_attackTerritory_invalidNumDefenders_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int invalidNumDefenders) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for Blue
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for Black
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLACK);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 5));

        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.attackTerritory(sourceTerritory, destTerritory, 3, invalidNumDefenders));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Number of armies to defend with must be within [1, 2]!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test27_attackTerritory_invalidNumArmiesInDestination_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for Blue
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for Black
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));
        // don't place any additional armies in the destination; there's only one case in which the number of defenders
        // is invalid in normal play: 1 army in territory and 2 defenders are being used

        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.attackTerritory(sourceTerritory, destTerritory, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Destination territory has too few defenders for this defense!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateReturnsAndExpectationsForAttackerNotTakingTerritory() {
        Set<Arguments> toStream = new HashSet<>();

        List<BattleResult> doubleDefVictory = List.of(BattleResult.DEFENDER_VICTORY, BattleResult.DEFENDER_VICTORY);
        List<BattleResult> singleDefVictory = List.of(BattleResult.DEFENDER_VICTORY);
        List<BattleResult> doubleAttVictory = List.of(BattleResult.ATTACKER_VICTORY, BattleResult.ATTACKER_VICTORY);
        List<BattleResult> singleAttVictory = List.of(BattleResult.ATTACKER_VICTORY);
        List<Arguments> territoryPairs = generateAdjacentTerritoryPairs().collect(Collectors.toList());

        List<List<Integer>> attackerDiceRolls = List.of(List.of(4, 3, 2), List.of(4, 3), List.of(1), List.of(2),
                List.of(6, 5, 4), List.of(3, 2), List.of(6));
        List<List<Integer>> defenderDiceRolls = List.of(List.of(6, 5), List.of(6, 5), List.of(5), List.of(4, 3),
                List.of(1, 2), List.of(1, 1), List.of(3));
        List<List<BattleResult>> battleResults = List.of(doubleDefVictory, doubleDefVictory, singleDefVictory,
                singleDefVictory, doubleAttVictory, doubleAttVictory, singleAttVictory);
        List<Integer> startingAttackerAmounts = List.of(4, 3, 2, 2, 4, 3, 2);
        List<Integer> startingDefenderAmounts = List.of(2, 2, 1, 2, 3, 3, 2);
        List<Integer> endingAttackerAmounts = List.of(2, 1, 1, 1, 4, 3, 2);
        List<Integer> endingDefenderAmounts = List.of(2, 2, 1, 2, 1, 1, 1);
        List<Integer> numAttackers = List.of(3, 2, 1, 1, 3, 2, 1);
        List<Integer> numDefenders = List.of(2, 2, 1, 2, 2, 2, 1);

        for (Arguments territoryPair : territoryPairs) {
            Object[] territories = territoryPair.get();
            for (int i = 0; i < startingAttackerAmounts.size(); i++) {
                toStream.add(Arguments.of(attackerDiceRolls.get(i), defenderDiceRolls.get(i),
                        battleResults.get(i), startingAttackerAmounts.get(i), startingDefenderAmounts.get(i),
                        endingAttackerAmounts.get(i), endingDefenderAmounts.get(i), numAttackers.get(i),
                        numDefenders.get(i), territories[0], territories[1]));
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateReturnsAndExpectationsForAttackerNotTakingTerritory")
    public void test28_attackTerritory_validInput_attackerDoesNotTakeTerritory_expectDecreasedArmiesAndNoCardClaiming(
            List<Integer> attackDiceRolls, List<Integer> defenseDiceRolls, List<BattleResult> battleResults,
            int armiesInSource, int armiesInDest, int numAttackersExpectedAfter, int numDefendersExpectedAfter,
            int numAttackers, int numDefenders, TerritoryType source, TerritoryType dest) {
        // I'll utilize a mocked DieRollParser here to ensure we don't take the territory.
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);

        DieRollParser mockedParser = EasyMock.createMock(DieRollParser.class);
        List<Integer> dieRolls = new ArrayList<>();
        for (int i = playersList.size(); i > 0; i--) {
            dieRolls.add(i);
        }
        EasyMock.expect(mockedParser.rollDiceToDeterminePlayerOrder(playersList.size())).andReturn(dieRolls);
        EasyMock.expect(mockedParser.rollAttackerDice(numAttackers)).andReturn(attackDiceRolls);
        EasyMock.expect(mockedParser.rollDefenderDice(numDefenders)).andReturn(defenseDiceRolls);
        EasyMock.expect(mockedParser.generateBattleResults(defenseDiceRolls, attackDiceRolls)).andReturn(battleResults);
        EasyMock.replay(mockedParser);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(source, 1)); // claim for Blue
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(dest, 1)); // claim for Black
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        // subtract 1 from num armies in each as we already put 1 there in claiming the territories.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        if (armiesInSource != 1) {
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(source, armiesInSource - 1));
        }
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLACK);
        if (armiesInDest != 1) {
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(dest, armiesInDest - 1));
        }
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        assertEquals(0, unitUnderTest.attackTerritory(source, dest, numAttackers, numDefenders));

        assertEquals(source, unitUnderTest.getRecentlyAttackedSource());
        assertEquals(dest, unitUnderTest.getRecentlyAttackedDest());

        assertEquals(numAttackersExpectedAfter, unitUnderTest.getNumberOfArmies(source));
        assertEquals(numDefendersExpectedAfter, unitUnderTest.getNumberOfArmies(dest));
        assertFalse(unitUnderTest.getIfCurrentPlayerCanClaimCard());

        EasyMock.verify(mockedParser);
    }

    private static Stream<Arguments> generateReturnsAndExpectationsForAttackerTakingTerritory() {
        Set<Arguments> toStream = new HashSet<>();

        List<BattleResult> doubleAttVictory = List.of(BattleResult.ATTACKER_VICTORY, BattleResult.ATTACKER_VICTORY);
        List<BattleResult> singleAttVictory = List.of(BattleResult.ATTACKER_VICTORY);
        List<List<Integer>> attackerDiceRolls = List.of(List.of(6, 6, 4), List.of(6, 6), List.of(5, 4),
                List.of(5, 5), List.of(6));
        List<List<Integer>> defenderDiceRolls = List.of(List.of(3, 2), List.of(4, 3),
                List.of(2, 1), List.of(2), List.of(1));
        List<List<BattleResult>> battleResults = List.of(doubleAttVictory, doubleAttVictory, doubleAttVictory,
                singleAttVictory, singleAttVictory);

        List<Integer> startingAttackerAmounts = List.of(10, 7, 6, 5, 2);
        List<Integer> startingDefenderAmounts = List.of(2, 2, 2, 1, 1);
        List<Integer> endingAttackerAmountsInSource = List.of(7, 5, 4, 3, 1);
        List<Integer> numAttackers = List.of(3, 2, 2, 2, 1);
        List<Integer> numDefenders = List.of(2, 2, 2, 1, 1);
        List<Integer> anticipatedResult = List.of(6, 4, 3, 2, 0);
        List<Arguments> territoryPairs = generateAdjacentTerritoryPairs().collect(Collectors.toList());

        for (Arguments territoryPair : territoryPairs) {
            Object[] territories = territoryPair.get();
            for (int i = 0; i < startingAttackerAmounts.size(); i++) {
                toStream.add(Arguments.of(attackerDiceRolls.get(i), defenderDiceRolls.get(i),
                        battleResults.get(i), startingAttackerAmounts.get(i), startingDefenderAmounts.get(i),
                        endingAttackerAmountsInSource.get(i), numAttackers.get(i), numDefenders.get(i), territories[1],
                        territories[0], anticipatedResult.get(i)));
            }

        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateReturnsAndExpectationsForAttackerTakingTerritory")
    public void test29_attackTerritory_validInput_attackerTakesTerritory_expectTerritoryTakeOverAndCardClaimability(
            List<Integer> attackDiceRolls, List<Integer> defenseDiceRolls, List<BattleResult> battleResults,
            int armiesInSource, int armiesInDest, int numAttackersInSourceAfter, int numAttackers, int numDefenders,
            TerritoryType source, TerritoryType dest, int anticipatedResult) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);

        DieRollParser mockedParser = EasyMock.createMock(DieRollParser.class);
        List<Integer> dieRolls = new ArrayList<>();
        for (int i = playersList.size(); i > 0; i--) {
            dieRolls.add(i);
        }
        EasyMock.expect(mockedParser.rollDiceToDeterminePlayerOrder(playersList.size())).andReturn(dieRolls);
        EasyMock.expect(mockedParser.rollAttackerDice(numAttackers)).andReturn(attackDiceRolls);
        EasyMock.expect(mockedParser.rollDefenderDice(numDefenders)).andReturn(defenseDiceRolls);
        EasyMock.expect(mockedParser.generateBattleResults(defenseDiceRolls, attackDiceRolls)).andReturn(battleResults);
        EasyMock.replay(mockedParser);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(source, 1)); // claim for Blue
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(dest, 1)); // claim for Black
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        // subtract 1 from num armies in each as we already put 1 there in claiming the territories.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(source, armiesInSource - 1));

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLACK);
        if (armiesInDest != 1) {
            assertTrue(unitUnderTest.placeNewArmiesInTerritory(dest, armiesInDest - 1));
        }
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        assertEquals(anticipatedResult, unitUnderTest.attackTerritory(source, dest, numAttackers, numDefenders));

        assertEquals(source, unitUnderTest.getRecentlyAttackedSource());
        assertEquals(dest, unitUnderTest.getRecentlyAttackedDest());

        assertEquals(numAttackersInSourceAfter, unitUnderTest.getNumberOfArmies(source));
        assertEquals(numAttackers, unitUnderTest.getNumberOfArmies(dest));
        assertTrue(unitUnderTest.getIfCurrentPlayerCanClaimCard());

        EasyMock.verify(mockedParser);
    }

    @Test
    public void test30_attackTerritory_validInput_playerWinsGame_expectGameOverGamePhase() {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);

        DieRollParser mockedParser = EasyMock.createMock(DieRollParser.class);
        List<Integer> dieRolls = new ArrayList<>();
        for (int i = playersList.size(); i > 0; i--) {
            dieRolls.add(i);
        }
        EasyMock.expect(mockedParser.rollDiceToDeterminePlayerOrder(playersList.size())).andReturn(dieRolls);
        EasyMock.expect(mockedParser.rollAttackerDice(3)).andReturn(List.of(6, 6, 5));
        EasyMock.expect(mockedParser.rollDefenderDice(1)).andReturn(List.of(3));
        EasyMock.expect(mockedParser.generateBattleResults(List.of(3), List.of(6, 6, 5)))
                .andReturn(List.of(BattleResult.ATTACKER_VICTORY));
        EasyMock.replay(mockedParser);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);
        // skew the list to only have two players to speed up the test.
        unitUnderTest.setPlayerOrderList(List.of(PlayerColor.BLUE, PlayerColor.PURPLE));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        // note that PURPLE is going to need more armies in order to actually claim 41 territories
        unitUnderTest.setNumArmiesForPlayer(PlayerColor.PURPLE, 60);
        for (TerritoryType territory : TerritoryType.values()) {
            if (territory == TerritoryType.ALASKA) { // guarantee adjacent territories.
                unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);
                assertTrue(unitUnderTest.placeNewArmiesInTerritory(territory, 1));
            } else {
                assertTrue(unitUnderTest.placeNewArmiesInTerritory(territory, 1));
            }
            unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        }
        // purple should now own all but 1 territory, and have 19 armies left. Skip to placement, place 14 more in
        // ALBERTA, and wipe out BLUE.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALBERTA, 14);

        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        unitUnderTest.attackTerritory(TerritoryType.ALBERTA, TerritoryType.ALASKA, 3, 1);

        assertEquals(GamePhase.GAME_OVER, unitUnderTest.getCurrentGamePhase());

        EasyMock.verify(mockedParser);
    }

    @Test
    public void test31_attackTerritory_validInput_defenderLosesGame_expectCardTransferBetweenPlayersAndLoserRemoved() {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);

        DieRollParser mockedParser = EasyMock.createMock(DieRollParser.class);
        List<Integer> dieRolls = new ArrayList<>();
        for (int i = playersList.size(); i > 0; i--) {
            dieRolls.add(i);
        }
        EasyMock.expect(mockedParser.rollDiceToDeterminePlayerOrder(playersList.size())).andReturn(dieRolls);
        EasyMock.expect(mockedParser.rollAttackerDice(3)).andReturn(List.of(6, 6, 5));
        EasyMock.expect(mockedParser.rollDefenderDice(1)).andReturn(List.of(3));
        EasyMock.expect(mockedParser.generateBattleResults(List.of(3), List.of(6, 6, 5)))
                .andReturn(List.of(BattleResult.ATTACKER_VICTORY));
        EasyMock.replay(mockedParser);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        // provide BLUE with some cards first.
        Set<Card> blueCards = Set.of(new WildCard(), new TerritoryCard(TerritoryType.MADAGASCAR, PieceType.ARTILLERY));
        // provide BLACK with some cards too.
        Set<Card> blackCards = Set.of(new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY),
                new TerritoryCard(TerritoryType.URAL, PieceType.CAVALRY));
        unitUnderTest.setCardsForPlayer(PlayerColor.BLUE, blueCards);
        unitUnderTest.setCardsForPlayer(PlayerColor.BLACK, blackCards);

        unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 1); // claim for BLUE
        unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALBERTA, 1); // claim for BLACK

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, 5); // place more armies for BLUE

        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        // now have BLUE attack BLACK in ALBERTA. Since black only owns this territory, they should lose the game.
        unitUnderTest.attackTerritory(TerritoryType.ALASKA, TerritoryType.ALBERTA, 3, 1);

        // check that BLUE owns BLACK's cards.
        assertTrue(unitUnderTest.getCardsOwnedByPlayer(PlayerColor.BLUE).containsAll(blackCards));
        // make sure BLACK is no longer in turns order / the map.
        assertFalse(unitUnderTest.getPlayerOrder().contains(PlayerColor.BLACK));
        assertFalse(unitUnderTest.getPlayerMap().containsKey(PlayerColor.BLACK));

        EasyMock.verify(mockedParser);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test34_moveArmiesBetweenFriendlyTerritories_fortifyPhase_sourceNotOwned_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));

        // advance to FORTIFY, go back to PURPLE, and try moving the armies.
        unitUnderTest.setGamePhase(GamePhase.FORTIFY);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 4));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Provided territories are not owned by the current player!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test35_moveArmiesBetweenFriendlyTerritories_fortifyPhase_destNotOwned_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for purple
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for yellow
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));

        // advance to FORTIFY, and try moving the armies.
        unitUnderTest.setGamePhase(GamePhase.FORTIFY);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 4));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Provided territories are not owned by the current player!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateNonAdjacentTerritoryPairs")
    public void test36_moveArmiesBetweenFriendlyTerritories_fortifyPhase_territoriesAreNotAdjacent_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));

        // advance to FORTIFY, and try moving the armies.
        unitUnderTest.setGamePhase(GamePhase.FORTIFY);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 4));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source and destination territory must be two adjacent territories!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test37_moveArmiesBetweenFriendlyTerritories_fortifyPhase_invalidNumOfArmiesToMove_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1));

        // advance to FORTIFY, and try moving the armies.
        unitUnderTest.setGamePhase(GamePhase.FORTIFY);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source territory does not have enough armies to support this movement!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateAdjacentTerritoryPairsAndInvalidMovementPhases() {
        Set<Arguments> toStream = new HashSet<>();
        List<Arguments> territoryPairs = generateAdjacentTerritoryPairs().collect(Collectors.toList());
        List<GamePhase> invalidGamePhases = new ArrayList<>(List.of(GamePhase.values()));
        invalidGamePhases.removeAll(List.of(GamePhase.ATTACK, GamePhase.FORTIFY));

        for (Arguments territoryPair : territoryPairs) {
            Object[] territories = territoryPair.get();
            for (GamePhase invalidPhase : invalidGamePhases) {
                toStream.add(Arguments.of(territories[0], territories[1], invalidPhase));
            }

        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairsAndInvalidMovementPhases")
    public void test38_moveArmiesBetweenFriendlyTerritories_invalidPhase_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 3));

        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Friendly army movement can only be done in the ATTACK or FORTIFY phase!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test39_moveArmiesBetweenFriendlyTerritories_attackPhase_playerTradesIn_clearedRecent_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 3));

        // move into attack, set recently attacked stuff to pretend that it happened.
        unitUnderTest.setRecentlyAttackedSource(sourceTerritory);
        unitUnderTest.setRecentlyAttackedDest(destTerritory);

        // now trade in cards, and assert that we throw an error (and fields are cleared)
        Card wildCard = new WildCard();
        Card alaskaCard = new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY);
        Card brazilCard = new TerritoryCard(TerritoryType.BRAZIL, PieceType.CAVALRY);
        Set<Card> toTradeIn = Set.of(wildCard, alaskaCard, brazilCard);
        Set<Card> playerCards = new HashSet<>(toTradeIn);
        playerCards.addAll(Set.of(
                new TerritoryCard(TerritoryType.CHINA, PieceType.CAVALRY),
                new TerritoryCard(TerritoryType.UKRAINE, PieceType.INFANTRY),
                new WildCard()));

        unitUnderTest.setCardsForPlayer(PlayerColor.PURPLE, playerCards);
        unitUnderTest.tradeInCards(toTradeIn);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Cannot split armies between this source and destination!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateAdjacentTerritoryTrios() {
        return Stream.of(
                // North America
                Arguments.of(TerritoryType.ALASKA, TerritoryType.NORTHWEST_TERRITORY, TerritoryType.KAMCHATKA),
                Arguments.of(TerritoryType.NORTHWEST_TERRITORY, TerritoryType.ALBERTA, TerritoryType.ALASKA),
                Arguments.of(TerritoryType.GREENLAND, TerritoryType.ICELAND, TerritoryType.QUEBEC),
                Arguments.of(TerritoryType.ALBERTA, TerritoryType.ONTARIO, TerritoryType.WESTERN_UNITED_STATES),
                Arguments.of(TerritoryType.ONTARIO, TerritoryType.QUEBEC, TerritoryType.EASTERN_UNITED_STATES),
                Arguments.of(TerritoryType.QUEBEC, TerritoryType.EASTERN_UNITED_STATES, TerritoryType.ONTARIO),
                Arguments.of(TerritoryType.WESTERN_UNITED_STATES, TerritoryType.CENTRAL_AMERICA, TerritoryType.ALBERTA),
                Arguments.of(TerritoryType.EASTERN_UNITED_STATES, TerritoryType.CENTRAL_AMERICA, TerritoryType.ONTARIO),
                Arguments.of(TerritoryType.CENTRAL_AMERICA, TerritoryType.VENEZUELA,
                        TerritoryType.EASTERN_UNITED_STATES),
                // South America
                Arguments.of(TerritoryType.VENEZUELA, TerritoryType.PERU, TerritoryType.BRAZIL),
                Arguments.of(TerritoryType.PERU, TerritoryType.BRAZIL, TerritoryType.ARGENTINA),
                Arguments.of(TerritoryType.BRAZIL, TerritoryType.NORTH_AFRICA, TerritoryType.ARGENTINA),
                Arguments.of(TerritoryType.ARGENTINA, TerritoryType.PERU, TerritoryType.BRAZIL),
                // Europe
                Arguments.of(TerritoryType.GREAT_BRITAIN, TerritoryType.ICELAND, TerritoryType.WESTERN_EUROPE),
                Arguments.of(TerritoryType.ICELAND, TerritoryType.SCANDINAVIA, TerritoryType.GREENLAND),
                Arguments.of(TerritoryType.SCANDINAVIA, TerritoryType.UKRAINE, TerritoryType.GREAT_BRITAIN),
                Arguments.of(TerritoryType.NORTHERN_EUROPE, TerritoryType.SOUTHERN_EUROPE, TerritoryType.UKRAINE),
                Arguments.of(TerritoryType.SOUTHERN_EUROPE, TerritoryType.WESTERN_EUROPE, TerritoryType.MIDDLE_EAST),
                Arguments.of(TerritoryType.UKRAINE, TerritoryType.URAL, TerritoryType.AFGHANISTAN),
                Arguments.of(TerritoryType.WESTERN_EUROPE, TerritoryType.NORTH_AFRICA, TerritoryType.SOUTHERN_EUROPE),
                // Africa
                Arguments.of(TerritoryType.NORTH_AFRICA, TerritoryType.EGYPT, TerritoryType.CONGO),
                Arguments.of(TerritoryType.EGYPT, TerritoryType.EAST_AFRICA, TerritoryType.NORTH_AFRICA),
                Arguments.of(TerritoryType.CONGO, TerritoryType.SOUTH_AFRICA, TerritoryType.EAST_AFRICA),
                Arguments.of(TerritoryType.EAST_AFRICA, TerritoryType.MIDDLE_EAST, TerritoryType.EGYPT),
                Arguments.of(TerritoryType.SOUTH_AFRICA, TerritoryType.MADAGASCAR, TerritoryType.CONGO),
                Arguments.of(TerritoryType.MADAGASCAR, TerritoryType.EAST_AFRICA, TerritoryType.SOUTH_AFRICA),
                // Asia
                Arguments.of(TerritoryType.AFGHANISTAN, TerritoryType.MIDDLE_EAST, TerritoryType.INDIA),
                Arguments.of(TerritoryType.MIDDLE_EAST, TerritoryType.INDIA, TerritoryType.AFGHANISTAN),
                Arguments.of(TerritoryType.URAL, TerritoryType.SIBERIA, TerritoryType.UKRAINE),
                Arguments.of(TerritoryType.INDIA, TerritoryType.CHINA, TerritoryType.SIAM),
                Arguments.of(TerritoryType.CHINA, TerritoryType.SIAM, TerritoryType.MONGOLIA),
                Arguments.of(TerritoryType.SIBERIA, TerritoryType.IRKUTSK, TerritoryType.URAL),
                Arguments.of(TerritoryType.SIAM, TerritoryType.INDONESIA, TerritoryType.CHINA),
                Arguments.of(TerritoryType.MONGOLIA, TerritoryType.JAPAN, TerritoryType.KAMCHATKA),
                Arguments.of(TerritoryType.IRKUTSK, TerritoryType.KAMCHATKA, TerritoryType.SIBERIA),
                Arguments.of(TerritoryType.YAKUTSK, TerritoryType.KAMCHATKA, TerritoryType.IRKUTSK),
                Arguments.of(TerritoryType.JAPAN, TerritoryType.KAMCHATKA, TerritoryType.MONGOLIA),
                Arguments.of(TerritoryType.KAMCHATKA, TerritoryType.MONGOLIA, TerritoryType.IRKUTSK),
                // Oceania
                Arguments.of(TerritoryType.INDONESIA, TerritoryType.WESTERN_AUSTRALIA, TerritoryType.NEW_GUINEA),
                Arguments.of(TerritoryType.NEW_GUINEA, TerritoryType.EASTERN_AUSTRALIA,
                        TerritoryType.WESTERN_AUSTRALIA),
                Arguments.of(TerritoryType.WESTERN_AUSTRALIA, TerritoryType.EASTERN_AUSTRALIA, TerritoryType.INDONESIA),
                Arguments.of(TerritoryType.EASTERN_AUSTRALIA, TerritoryType.NEW_GUINEA, TerritoryType.WESTERN_AUSTRALIA)
        );
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryTrios")
    public void test40_moveArmiesBetweenFriendlyTerritories_attackPhase_playerStartsNewAttack_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory, TerritoryType toAttack) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, new DieRollParser());

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.YELLOW);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(toAttack, 1)); // claim for yellow
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 8));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.YELLOW);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(toAttack, 5));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // move into attack, set recently attacked stuff to pretend that we could split.
        unitUnderTest.setRecentlyAttackedSource(sourceTerritory);
        unitUnderTest.setRecentlyAttackedDest(destTerritory);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        // now start another attack between two other territories
        assertDoesNotThrow(() -> unitUnderTest.attackTerritory(
                sourceTerritory, toAttack, 3, 2));

        // recently attacked source and destination should be cleared, so we should error.
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Cannot split armies between this source and destination!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test41_moveArmiesBetweenFriendlyTerritories_attackPhase_validInput_expectNoAbilityToSplitAgainAfter(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, new DieRollParser());

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1)); // claim for purple
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 8));

        // move into attack, set recently attacked stuff to pretend that we could split.
        unitUnderTest.setRecentlyAttackedSource(sourceTerritory);
        unitUnderTest.setRecentlyAttackedDest(destTerritory);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        // move the armies once, assert that things are null, then try splitting again. it should throw
        // an exception the second time around.
        assertDoesNotThrow(() -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 3));

        assertNull(unitUnderTest.getRecentlyAttackedDest());
        assertNull(unitUnderTest.getRecentlyAttackedSource());

        // now do it again.
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 3));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Cannot split armies between this source and destination!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryTrios")
    public void test42_moveArmiesBetweenFriendlyTerritories_fortifyPhase_playerTookTerritory_expectCardToBeAwarded(
            TerritoryType sourceTerritory, TerritoryType destTerritory, TerritoryType yellowOwns) {
        List<PlayerColor> playersList = List.of(PlayerColor.BLUE, PlayerColor.BLACK, PlayerColor.RED,
                PlayerColor.PURPLE, PlayerColor.YELLOW);
        DieRollParser mockedParser = generateMockedParser(playersList);
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine(playersList, mockedParser);

        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 1));
        // give yellow a territory, so they *actually* exist in the context of the game.
        unitUnderTest.placeNewArmiesInTerritory(yellowOwns, 1);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(destTerritory, 1));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        // advance to placement, so we can have valid army amounts.
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(sourceTerritory, 5));

        // advance to FORTIFY, and try moving the armies. Also, say they've earned the chance to get a card.
        unitUnderTest.setGamePhase(GamePhase.FORTIFY);
        unitUnderTest.setAbilityToClaimCard();

        assertDoesNotThrow(() -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 3));

        assertFalse(unitUnderTest.getIfCurrentPlayerCanClaimCard());
        // card set was presumably of size 0 beforehand, so it should be size 1 now.
        assertEquals(1, unitUnderTest.getCardsOwnedByPlayer(PlayerColor.PURPLE).size());
        assertEquals(PlayerColor.YELLOW, unitUnderTest.getCurrentPlayer());
        // since yellow only owns 1 territory in this example, they'll get 3 armies...
        // slight complication, though: since we didn't spend all of their setup armies in the traditional manner,
        // they will get 24 + 3 armies total (setup amount + placement phase bonus for 1 territory)
        assertEquals(27, unitUnderTest.getNumArmiesByPlayerColor(PlayerColor.YELLOW));
        assertEquals(GamePhase.PLACEMENT, unitUnderTest.getCurrentGamePhase());
    }
}
