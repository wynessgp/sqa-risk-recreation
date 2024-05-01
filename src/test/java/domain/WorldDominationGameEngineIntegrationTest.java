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

}
