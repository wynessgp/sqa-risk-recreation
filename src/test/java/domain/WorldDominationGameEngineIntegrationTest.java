package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        Set<Card> playerCards = Set.of(new WildCard(), new WildCard(),
                new TerritoryCard(TerritoryType.BRAZIL, PieceType.INFANTRY));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN); // go back to green.
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.setCardsForPlayer(players.get(0), playerCards);

        unitUnderTest.tradeInCards(playerCards);

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



}
