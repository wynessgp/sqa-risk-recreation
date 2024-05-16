package domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class WorldDominationGameEngineTest {

    private static final int GET_TERRITORY_ONCE = 1;
    private static final int GET_TERRITORY_TWICE = 2;

    private static final int OCEANIA_BONUS = 2;
    private static final int SOUTH_AMERICA_BONUS = 2;
    private static final int AFRICA_BONUS = 3;
    private static final int NORTH_AMERICA_BONUS = 5;
    private static final int EUROPE_BONUS = 5;
    private static final int ASIA_BONUS = 7;

    private static Stream<Arguments> generateVariousIllegalPlayerOrderLists() {
        Set<Arguments> illegalPlayerOrderArguments = new HashSet<>();
        illegalPlayerOrderArguments.add(Arguments.of(List.of()));
        illegalPlayerOrderArguments.add(Arguments.of(List.of(PlayerColor.YELLOW)));
        illegalPlayerOrderArguments.add(Arguments.of(List.of(PlayerColor.BLUE, PlayerColor.RED)));
        illegalPlayerOrderArguments.add(Arguments.of(List.of(PlayerColor.BLUE, PlayerColor.BLUE, PlayerColor.SETUP,
                PlayerColor.PURPLE, PlayerColor.YELLOW, PlayerColor.YELLOW, PlayerColor.BLACK)));

        List<PlayerColor> listWithFourOfEachPlayerColor = new ArrayList<>();
        for (PlayerColor playerColor : PlayerColor.values()) {
            listWithFourOfEachPlayerColor.addAll(List.of(playerColor, playerColor, playerColor, playerColor));
        }
        illegalPlayerOrderArguments.add(Arguments.of(listWithFourOfEachPlayerColor));

        return illegalPlayerOrderArguments.stream();
    }

    @ParameterizedTest
    @MethodSource("generateVariousIllegalPlayerOrderLists")
    public void test00_initializePlayersList_playerOrderSizeOutsideOfAcceptableRange_expectException(
            List<PlayerColor> illegalPlayerOrder) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        String expectedMessage = "playerOrder's size is not within: [3, 6]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(illegalPlayerOrder));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateListsOfVaryingSizeAllContainingDuplicates() {
        Set<Arguments> duplicatePlayerColorArguments = new HashSet<>();
        // size 3 duplicate lists
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.RED, PlayerColor.BLACK, PlayerColor.RED)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.YELLOW, PlayerColor.YELLOW, PlayerColor.SETUP)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.PURPLE, PlayerColor.SETUP, PlayerColor.SETUP)));

        // size 4 duplicate lists
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.YELLOW, PlayerColor.BLACK)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.RED)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.BLACK, PlayerColor.BLACK, PlayerColor.BLACK, PlayerColor.BLUE)));

        // size 5 duplicate lists
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.PURPLE)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.SETUP, PlayerColor.GREEN)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLACK, PlayerColor.YELLOW, PlayerColor.BLUE)));

        // size 6 duplicate lists
        duplicatePlayerColorArguments.add(Arguments.of(List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.SETUP,
                PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.RED)));
        duplicatePlayerColorArguments.add(Arguments.of(List.of(PlayerColor.BLUE, PlayerColor.PURPLE, PlayerColor.RED,
                PlayerColor.BLACK, PlayerColor.YELLOW, PlayerColor.BLUE)));
        duplicatePlayerColorArguments.add(Arguments.of(List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.SETUP,
                PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.YELLOW)));

        return duplicatePlayerColorArguments.stream();
    }

    @ParameterizedTest
    @MethodSource("generateListsOfVaryingSizeAllContainingDuplicates")
    public void test01_initializePlayersList_playerOrderContainsDuplicates_expectException(
            List<PlayerColor> playerOrderWithDuplicates) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        String expectedMessage = "playerOrder contains duplicate entries";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(playerOrderWithDuplicates));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateListsOfVaryingSizeAndSetupAtDifferentIndices() {
        Set<Arguments> duplicatePlayerColorArguments = new HashSet<>();
        // size 3 with setup
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.SETUP, PlayerColor.BLACK, PlayerColor.RED)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.SETUP)));

        // size 4 with setup
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.YELLOW, PlayerColor.SETUP, PlayerColor.RED, PlayerColor.BLACK)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.SETUP)));

        // size 5 with setup
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.BLUE, PlayerColor.SETUP, PlayerColor.RED, PlayerColor.GREEN, PlayerColor.PURPLE)));
        duplicatePlayerColorArguments.add(Arguments.of(
                List.of(PlayerColor.GREEN, PlayerColor.BLACK, PlayerColor.RED, PlayerColor.BLUE, PlayerColor.SETUP)));

        // size 6 with setup
        duplicatePlayerColorArguments.add(Arguments.of(List.of(PlayerColor.PURPLE, PlayerColor.BLUE, PlayerColor.BLACK,
                PlayerColor.YELLOW, PlayerColor.SETUP, PlayerColor.RED)));
        duplicatePlayerColorArguments.add(Arguments.of(List.of(PlayerColor.BLUE, PlayerColor.PURPLE, PlayerColor.RED,
                PlayerColor.BLACK, PlayerColor.YELLOW, PlayerColor.SETUP)));

        return duplicatePlayerColorArguments.stream();
    }

    @ParameterizedTest
    @MethodSource("generateListsOfVaryingSizeAndSetupAtDifferentIndices")
    public void test02_initializePlayersList_playerOrderContainsSetup_expectException(
            List<PlayerColor> playerOrderWithSetup) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        String expectedMessage = "playerOrder contains SETUP as one of the players";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(playerOrderWithSetup));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

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
    public void test03_initializePlayersList_playerOrderIsValid_expectTrueAndCheckStorageIsSameAsInput(
            List<PlayerColor> validPlayerOrder) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        assertTrue(unitUnderTest.initializePlayersList(validPlayerOrder));
        assertEquals(validPlayerOrder, unitUnderTest.getPlayerOrder());
    }


    @Test
    public void test04_assignSetupArmiesToPlayers_playerColorListIsEmpty_expectException() {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> emptyList = List.of();

        unitUnderTest.setPlayerOrderList(emptyList);

        String expectedMessage = "No player objects exist, call initializePlayersList first with the correct arguments";
        Exception exception = assertThrows(IllegalStateException.class,
                unitUnderTest::assignSetupArmiesToPlayers);

        assertEquals(expectedMessage, exception.getMessage());
    }

    private List<Player> createMockedPlayersList(List<PlayerColor> playerColors, int numberOfPlayers) {
        List<Player> listOfPlayersToReturn = new ArrayList<>();
        for (PlayerColor playerColor : playerColors) {
            Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                    .withConstructor(PlayerColor.class)
                    .withArgs(playerColor)
                    .addMockedMethod("setNumArmiesToPlace")
                    .createMock();
            // 35 is the MAX (3 players), lose 5 additional armies per extra player.
            int expectedNumArmies = 35 - ((numberOfPlayers - 3) * 5);
            mockedPlayer.setNumArmiesToPlace(expectedNumArmies);
            EasyMock.expectLastCall().once();
            EasyMock.replay(mockedPlayer);
            listOfPlayersToReturn.add(mockedPlayer);
        }
        return listOfPlayersToReturn;
    }

    @ParameterizedTest
    @MethodSource("generateValidPlayerListsSizesThreeThroughSix")
    public void test05_assignSetupArmiesToPlayers_playerListSizeVaries_returnsTrueAndAssignsExpectedAmount(
            List<PlayerColor> playerColorList) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setPlayerOrderList(playerColorList);

        List<Player> playerMocks = createMockedPlayersList(playerColorList, playerColorList.size());

        unitUnderTest.provideMockedPlayerObjects(playerMocks);

        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());

        for (Player mockedPlayer : playerMocks) {
            EasyMock.verify(mockedPlayer);
        }
    }

    private Territory createMockedTerritoryWithExpectations(PlayerColor playerColorToReturn) {
        Territory mockedTerritory = EasyMock.createMock(Territory.class);

        EasyMock.expect(mockedTerritory.isOwnedByPlayer(playerColorToReturn)).andReturn(true);

        EasyMock.replay(mockedTerritory);
        return mockedTerritory;
    }

    private TerritoryGraph createMockedGraphWithExpectations(
            TerritoryType relevantTerritory, Territory mockedTerritory, int numTimesToGetTerritory) {
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);

        EasyMock.expect(mockedGraph.getTerritory(relevantTerritory))
                .andReturn(mockedTerritory)
                .times(numTimesToGetTerritory);

        EasyMock.replay(mockedGraph);
        return mockedGraph;
    }

    private Territory createMockedTerritoryReturnsFalseForIsOwnedBy(PlayerColor toReturnFalseWith) {
        Territory mockedTerritory = EasyMock.createMock(Territory.class);

        EasyMock.expect(mockedTerritory.isOwnedByPlayer(toReturnFalseWith)).andReturn(false);

        EasyMock.replay(mockedTerritory);
        return mockedTerritory;
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerColors")
    public void test06_checkIfPlayerOwnsTerritory_playerDoesNotOwnTerritory_expectFalse(
            TerritoryType relevantTerritory, PlayerColor notInControl) {
        Territory mockedTerritory = createMockedTerritoryReturnsFalseForIsOwnedBy(notInControl);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(
                relevantTerritory, mockedTerritory, GET_TERRITORY_ONCE);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertFalse(unitUnderTest.checkIfPlayerOwnsTerritory(relevantTerritory, notInControl));

        EasyMock.verify(mockedGraph, mockedTerritory);
    }

    private static Stream<Arguments> generateAllTerritoryTypesAndPlayerColors() {
        Set<Arguments> allTerritoriesAndPlayerColors = new HashSet<>();
        for (TerritoryType territoryType : TerritoryType.values()) {
            for (PlayerColor playerColor : PlayerColor.values()) {
                allTerritoriesAndPlayerColors.add(Arguments.of(territoryType, playerColor));
            }
        }
        return allTerritoriesAndPlayerColors.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerColors")
    public void test07_checkIfPlayerOwnsTerritory_playerOwnsTerritory_expectTrue(
            TerritoryType relevantTerritory, PlayerColor playerInControlOfTerritory) {
        Territory mockedTerritory = createMockedTerritoryWithExpectations(playerInControlOfTerritory);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(
                relevantTerritory, mockedTerritory, GET_TERRITORY_ONCE);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertTrue(unitUnderTest.checkIfPlayerOwnsTerritory(relevantTerritory, playerInControlOfTerritory));

        EasyMock.verify(mockedGraph, mockedTerritory);
    }

    private static Stream<Arguments> generateAllTerritoryTypesAndPlayerMinusSetupCombinations() {
        // each set is going to be a "tuple" of (TerritoryType, PlayerColor)
        Set<Arguments> outputSet = new HashSet<>();
        Set<TerritoryType> territoryTypes = Set.of(TerritoryType.values());
        Set<PlayerColor> playerColors = new HashSet<>(Set.of(PlayerColor.values()));
        playerColors.remove(PlayerColor.SETUP);

        for (TerritoryType territoryType : territoryTypes) {
            for (PlayerColor playerColor : playerColors) {
                outputSet.add(Arguments.of(territoryType, playerColor));
            }
        }
        return outputSet.stream();
    }

    @ParameterizedTest
    @EnumSource(TerritoryType.class)
    public void test08_placeNewArmiesInTerritory_territoryAlreadyClaimedByCurrentPlayerInScramble_expectException(
            TerritoryType relevantTerritory) {
        Territory mockedTerritory = createMockedTerritoryReturnsFalseForIsOwnedBy(PlayerColor.SETUP);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 1);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        int numArmiesToPlace = 10;
        String expectedMessage = "Cannot place armies in a claimed territory until the scramble phase is over";
        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, numArmiesToPlace));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedTerritory, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerMinusSetupCombinations")
    public void test09_placeNewArmiesInTerritory_invalidAmountOfPlayerArmies_expectException(
            TerritoryType relevantTerritory, PlayerColor playerToTakeControl) {
        Territory mockedTerritory = createMockedTerritoryWithExpectations(PlayerColor.SETUP);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 1);
        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(playerToTakeControl)
                .addMockedMethod("getNumArmiesToPlace")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(0);

        EasyMock.replay(mockedPlayer);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(playerToTakeControl);
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));

        int validNumArmies = 1;
        String expectedMessage = "Player does not have enough armies to place!";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, validNumArmies));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer, mockedTerritory, mockedGraph);
    }

    private static Stream<Arguments> generateAllTerritoryTypesAndIllegalArmyInputs() {
        Set<Arguments> outputSet = new HashSet<>();
        Set<TerritoryType> territoryTypes = Set.of(TerritoryType.values());
        Set<Integer> illegalArmyInputs = Set.of(-1, 0, 2, Integer.MAX_VALUE);

        for (TerritoryType territoryType : territoryTypes) {
            for (Integer illegalArmyInput : illegalArmyInputs) {
                outputSet.add(Arguments.of(territoryType, illegalArmyInput));
            }
        }
        return outputSet.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndIllegalArmyInputs")
    public void test10_placeNewArmiesInTerritory_moreThanOneArmyInScramblePhase_expectException(
            TerritoryType relevantTerritory, int illegalArmyAmount) {
        Territory mockedTerritory = createMockedTerritoryWithExpectations(PlayerColor.SETUP);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 1);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        String expectedMessage = "You can only place 1 army on an unclaimed territory until the scramble phase is over";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, illegalArmyAmount));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedTerritory, mockedGraph);
    }

    private Territory createMockedTerritoryWithArmyPlacementAndPlayerColorSettingExpectations(
            int numArmiesToExpect, PlayerColor playerToExpect, PlayerColor playerColorToReturn) {
        Territory mockedTerritory = EasyMock.createMock(Territory.class);

        EasyMock.expect(mockedTerritory.isOwnedByPlayer(playerColorToReturn)).andReturn(true);
        EasyMock.expect(mockedTerritory.setNumArmiesPresent(numArmiesToExpect)).andReturn(true);
        EasyMock.expect(mockedTerritory.setPlayerInControl(playerToExpect)).andReturn(true);

        EasyMock.replay(mockedTerritory);
        return mockedTerritory;
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerMinusSetupCombinations")
    public void test11_placeNewArmiesInTerritory_scramblePhaseValidInput_expectTrueAndTerritoryGetsPlayerColorAndArmies(
            TerritoryType relevantTerritory, PlayerColor currentlyGoingPlayer) {
        Territory mockedTerritory = createMockedTerritoryWithArmyPlacementAndPlayerColorSettingExpectations(
                1, currentlyGoingPlayer, PlayerColor.SETUP);
        // we ask for the territory twice here, so make sure we indicate that.
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 2);
        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(currentlyGoingPlayer)
                .addMockedMethod("getNumArmiesToPlace")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(10).anyTimes();

        EasyMock.replay(mockedPlayer);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(currentlyGoingPlayer);
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));

        int validNumArmies = 1;
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, validNumArmies));

        EasyMock.verify(mockedTerritory, mockedPlayer, mockedGraph);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 14})
    public void test12_placeNewArmiesInTerritory_setupPhase_invalidArmyInput_expectException(int illegalInput) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        TerritoryType targetTerritory = TerritoryType.ALASKA;

        unitUnderTest.setGamePhase(GamePhase.SETUP);

        String expectedMessage = "Cannot place anything other than 1 army in a territory during setup phase";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(targetTerritory, illegalInput));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerColors")
    public void test13_placeNewArmiesInTerritory_setupPhase_playerDoesNotOwnTerritory_expectException(
            TerritoryType relevantTerritory, PlayerColor playerAttemptingToPlace) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        Territory mockedTerritory = createMockedTerritoryReturnsFalseForIsOwnedBy(playerAttemptingToPlace);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 1);

        unitUnderTest.setGamePhase(GamePhase.SETUP);
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(playerAttemptingToPlace);

        int numArmiesToPlace = 1;
        String expectedMessage = "Cannot place armies on a territory you do not own";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, numArmiesToPlace));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedTerritory, mockedGraph);
    }

    private static Stream<Arguments> generateAllTerritoriesAndPlayerColorsAndSomeArmyCounts() {
        Set<Arguments> allTerritoryPlayerColorAndSomeArmyPairs = new HashSet<>();
        List<PlayerColor> playerColorsMinusSetup = new ArrayList<>(List.of(PlayerColor.values()));
        playerColorsMinusSetup.remove(PlayerColor.SETUP);

        List<Integer> armyCounts = List.of(1, 2, 3, 4, 5, 6, 7);
        for (TerritoryType territory : TerritoryType.values()) {
            for (PlayerColor playerColor : playerColorsMinusSetup) {
                for (Integer armyCount : armyCounts) {
                    allTerritoryPlayerColorAndSomeArmyPairs.add(Arguments.of(territory, playerColor, armyCount));
                }
            }
        }
        return allTerritoryPlayerColorAndSomeArmyPairs.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoriesAndPlayerColorsAndSomeArmyCounts")
    public void test14_placeNewArmiesInTerritory_setupPhase_playerOwnsTerritory_expectTrueAndIncreaseArmiesInTerritory(
            TerritoryType relevantTerritory, PlayerColor playerInControl, int numArmiesPreviouslyPresent) {
        int numValidArmies = 1;

        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedTerritory.isOwnedByPlayer(playerInControl)).andReturn(true);
        EasyMock.expect(mockedTerritory.getNumArmiesPresent()).andReturn(numArmiesPreviouslyPresent);
        EasyMock.expect(mockedTerritory.setNumArmiesPresent(numArmiesPreviouslyPresent + numValidArmies))
                .andReturn(true);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(playerInControl)
                .createMock();
        mockedPlayer.setNumArmiesToPlace(40); // we're not testing for this,
        // but we still rely on having a player object exist.

        EasyMock.replay(mockedTerritory);

        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 2);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.SETUP);
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(playerInControl);
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, numValidArmies));

        EasyMock.verify(mockedTerritory, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoriesAndPlayerColorsAndSomeArmyCounts")
    public void test15_placeNewArmiesInTerritory_setupPhase_validInput_decrementPlayerArmiesToPlace(
            TerritoryType relevantTerritory, PlayerColor playerInControl, int numArmiesPreviouslyPresent) {
        int numValidArmies = 1;

        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedTerritory.isOwnedByPlayer(playerInControl)).andReturn(true);
        EasyMock.expect(mockedTerritory.getNumArmiesPresent()).andReturn(numArmiesPreviouslyPresent);
        EasyMock.expect(mockedTerritory.setNumArmiesPresent(numArmiesPreviouslyPresent + numValidArmies))
                .andReturn(true);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(playerInControl)
                .addMockedMethod("setNumArmiesToPlace")
                .addMockedMethod("getNumArmiesToPlace")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(14).times(2);
        mockedPlayer.setNumArmiesToPlace(13);
        EasyMock.expectLastCall().once();

        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 2);
        EasyMock.replay(mockedTerritory, mockedPlayer);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.SETUP);
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(playerInControl);
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, numValidArmies));

        EasyMock.verify(mockedTerritory, mockedGraph, mockedPlayer);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerMinusSetupCombinations")
    public void test16_placeNewArmiesInTerritory_setupPhase_playerHasTooFewArmiesToPlace_expectException(
            TerritoryType relevantTerritory, PlayerColor playerInControl) {
        Territory mockedTerritory = createMockedTerritoryWithExpectations(playerInControl);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 1);
        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(playerInControl)
                .addMockedMethod("getNumArmiesToPlace")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(0);

        EasyMock.replay(mockedPlayer);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(playerInControl);
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.setGamePhase(GamePhase.SETUP);

        int validNumArmies = 1;
        String expectedMessage = "Player does not have enough armies to place!";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, validNumArmies));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedTerritory, mockedGraph, mockedPlayer);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerMinusSetupCombinations")
    public void test17_placeNewArmiesInTerritory_scramblePhase_validInput_ensurePlayerObjectHasTerritoryAdded(
            TerritoryType relevantTerritory, PlayerColor currentPlayer) {

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(currentPlayer)
                .addMockedMethod("addTerritoryToCollection")
                .createMock();
        mockedPlayer.setNumArmiesToPlace(10);
        mockedPlayer.addTerritoryToCollection(relevantTerritory);
        EasyMock.expectLastCall().once();

        EasyMock.replay(mockedPlayer);

        Territory mockedTerritory = createMockedTerritoryWithArmyPlacementAndPlayerColorSettingExpectations(
                1, currentPlayer, PlayerColor.SETUP);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 2);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(currentPlayer);
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));

        int validNumArmies = 1;
        assertTrue(unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, validNumArmies));

        EasyMock.verify(mockedTerritory, mockedPlayer, mockedGraph);
    }

    private void testShufflePlayersMethod(List<PlayerColor> players, List<Integer> dieRolls,
                                          List<PlayerColor> expectedPlayers) {
        DieRollParser parser = EasyMock.createMock(DieRollParser.class);
        EasyMock.expect(parser.rollDiceToDeterminePlayerOrder(players.size())).andReturn(dieRolls);
        EasyMock.replay(parser);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setPlayerOrderList(players);
        unitUnderTest.setParser(parser);

        unitUnderTest.shufflePlayers();
        assertEquals(dieRolls, unitUnderTest.getDieRolls());
        assertEquals(expectedPlayers, unitUnderTest.getPlayerOrder());
        EasyMock.verify(parser);
    }

    @Test
    public void test18_shufflePlayers_withThreeUniquePlayers_returnsRollsAndShuffledList() {
        testShufflePlayersMethod(List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.GREEN), List.of(1, 2, 3),
                List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.RED));
    }

    @Test
    public void test19_shufflePlayers_withFourUniquePlayers_returnsRollsAndShuffledList() {
        testShufflePlayersMethod(List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.BLUE),
                List.of(2, 3, 5, 1), List.of(PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE));
    }

    @Test
    public void test20_shufflePlayers_withFiveUniquePlayers_returnsRollsAndShuffledList() {
        testShufflePlayersMethod(List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.BLUE,
                PlayerColor.PURPLE), List.of(5, 4, 6, 2, 1), List.of(PlayerColor.GREEN, PlayerColor.RED,
                PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.PURPLE));
    }

    @Test
    public void test21_shufflePlayers_withSixUniquePlayers_returnsRollsAndShuffledList() {
        testShufflePlayersMethod(List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.BLUE,
                PlayerColor.PURPLE, PlayerColor.BLACK), List.of(5, 4, 6, 2, 1, 3), List.of(PlayerColor.GREEN,
                PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.BLACK, PlayerColor.BLUE, PlayerColor.PURPLE));
    }

    @Test
    public void test22_shufflePlayers_withThreeUniquePlayers_listDoesNotChange() {
        testShufflePlayersMethod(List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.GREEN), List.of(3, 2, 1),
                List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.GREEN));
    }

    @Test
    public void test23_calculatePlacementPhaseArmiesForCurrentPlayer_playerOwnsNoTerritories_expectException() {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        Player redPlayer = new Player(PlayerColor.RED);

        // ensure that this test will still throw an exception (but not for this reason)
        unitUnderTest.provideMockedPlayerObjects(List.of(redPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Exception exception = assertThrows(IllegalStateException.class,
                unitUnderTest::calculatePlacementPhaseArmiesForCurrentPlayer);
        String actualMessage = exception.getMessage();

        String expectedMessage = "The current player should no longer exist!";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test24_calculatePlacementPhaseArmiesForCurrentPlayer_playerOwnsAllTerritories_expectException() {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        Player redPlayer = new Player(PlayerColor.RED);

        unitUnderTest.provideMockedPlayerObjects(List.of(redPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);
        unitUnderTest.claimAllTerritoriesForCurrentPlayer(Set.of(TerritoryType.values()));
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Exception exception = assertThrows(IllegalStateException.class,
                unitUnderTest::calculatePlacementPhaseArmiesForCurrentPlayer);
        String actualMessage = exception.getMessage();

        String expectedMessage = "Given player owns every territory, the game should be over!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Map<Continent, Set<TerritoryType>> getContinentTerritorySets() {
        Set<TerritoryType> africa = Set.of(TerritoryType.CONGO, TerritoryType.EAST_AFRICA, TerritoryType.EGYPT,
                TerritoryType.MADAGASCAR, TerritoryType.NORTH_AFRICA, TerritoryType.SOUTH_AFRICA);
        Set<TerritoryType> asia = Set.of(TerritoryType.AFGHANISTAN, TerritoryType.CHINA, TerritoryType.INDIA,
                TerritoryType.IRKUTSK, TerritoryType.JAPAN, TerritoryType.KAMCHATKA, TerritoryType.MIDDLE_EAST,
                TerritoryType.MONGOLIA, TerritoryType.SIAM, TerritoryType.SIBERIA, TerritoryType.URAL,
                TerritoryType.YAKUTSK);
        Set<TerritoryType> europe = Set.of(TerritoryType.UKRAINE, TerritoryType.GREAT_BRITAIN,
                TerritoryType.ICELAND, TerritoryType.NORTHERN_EUROPE, TerritoryType.SCANDINAVIA,
                TerritoryType.SOUTHERN_EUROPE, TerritoryType.WESTERN_EUROPE);
        Set<TerritoryType> northAmerica = Set.of(TerritoryType.ALASKA, TerritoryType.ALBERTA,
                TerritoryType.CENTRAL_AMERICA, TerritoryType.EASTERN_UNITED_STATES, TerritoryType.GREENLAND,
                TerritoryType.NORTHWEST_TERRITORY, TerritoryType.ONTARIO, TerritoryType.QUEBEC,
                TerritoryType.WESTERN_UNITED_STATES);
        Set<TerritoryType> oceania = Set.of(TerritoryType.EASTERN_AUSTRALIA, TerritoryType.INDONESIA,
                TerritoryType.NEW_GUINEA, TerritoryType.WESTERN_AUSTRALIA);
        Set<TerritoryType> southAmerica = Set.of(TerritoryType.ARGENTINA, TerritoryType.BRAZIL, TerritoryType.PERU,
                TerritoryType.VENEZUELA);

        return Map.ofEntries(
                Map.entry(Continent.AFRICA, africa),
                Map.entry(Continent.ASIA, asia),
                Map.entry(Continent.EUROPE, europe),
                Map.entry(Continent.NORTH_AMERICA, northAmerica),
                Map.entry(Continent.OCEANIA, oceania),
                Map.entry(Continent.SOUTH_AMERICA, southAmerica)
        );
    }

    private static Stream<Arguments> generateSetsOfTerritoriesSpecificSizesNoFullContinents() {
        Map<Continent, Set<TerritoryType>> allContinentInfo = getContinentTerritorySets();

        Set<Arguments> toStream = new HashSet<>();
        toStream.add(Arguments.of(Set.of(TerritoryType.ALASKA)));

        Set<TerritoryType> sizeElevenSet = new HashSet<>(allContinentInfo.get(Continent.NORTH_AMERICA));
        sizeElevenSet.addAll(allContinentInfo.get(Continent.OCEANIA));
        sizeElevenSet.remove(TerritoryType.EASTERN_UNITED_STATES); // remove 1 from each continent
        sizeElevenSet.remove(TerritoryType.INDONESIA); // meaning 9 + 4 - 2 = 11 territories

        Set<TerritoryType> sizeTwelveSet = new HashSet<>(sizeElevenSet);
        sizeTwelveSet.add(TerritoryType.JAPAN); // add from a new continent

        Set<TerritoryType> sizeFourteenSet = new HashSet<>(sizeTwelveSet);
        sizeTwelveSet.add(TerritoryType.MONGOLIA);
        sizeTwelveSet.add(TerritoryType.SIAM);

        Set<TerritoryType> sizeFifteenSet = new HashSet<>(sizeFourteenSet);
        sizeFifteenSet.add(TerritoryType.ARGENTINA);

        Set<TerritoryType> sizeThirtySixSet = new HashSet<>();
        for (Set<TerritoryType> currentContinentTerritories : allContinentInfo.values()) {
            sizeThirtySixSet.addAll(currentContinentTerritories);
        }
        Set<TerritoryType> toRemove = Set.of(TerritoryType.ALASKA, TerritoryType.BRAZIL, TerritoryType.CONGO,
                TerritoryType.INDIA, TerritoryType.INDONESIA, TerritoryType.GREAT_BRITAIN);
        sizeThirtySixSet.removeAll(toRemove);

        toStream.add(Arguments.of(sizeElevenSet));
        toStream.add(Arguments.of(sizeTwelveSet));
        toStream.add(Arguments.of(sizeFourteenSet));
        toStream.add(Arguments.of(sizeFifteenSet));
        toStream.add(Arguments.of(sizeThirtySixSet));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateSetsOfTerritoriesSpecificSizesNoFullContinents")
    public void test25_calculatePlacementPhaseArmiesForCurrentPlayer_playerOwnsManyTerritories_expectFormulaNumber(
            Set<TerritoryType> ownedTerritories) {
        testCalculatePlacementPhaseNumArmiesMethod(ownedTerritories, 0);
    }

    private void testCalculatePlacementPhaseNumArmiesMethod(
            Set<TerritoryType> playerOwnedTerritories, int expectedContinentBonus) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        Player redPlayer = new Player(PlayerColor.RED);

        unitUnderTest.provideMockedPlayerObjects(List.of(redPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);
        unitUnderTest.claimAllTerritoriesForCurrentPlayer(playerOwnedTerritories);
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        int expectedNumArmies = (playerOwnedTerritories.size() < 12) ? 3 : playerOwnedTerritories.size() / 3;
        expectedNumArmies += expectedContinentBonus;
        assertEquals(expectedNumArmies, unitUnderTest.calculatePlacementPhaseArmiesForCurrentPlayer());
    }

    private static Stream<Arguments> generateSetsOfTerritoriesFullContinentsAndExpectedBonuses() {
        Map<Continent, Set<TerritoryType>> allContinentInfo = getContinentTerritorySets();

        Set<Arguments> toStream = new HashSet<>();
        toStream.add(Arguments.of(allContinentInfo.get(Continent.OCEANIA), OCEANIA_BONUS));
        toStream.add(Arguments.of(allContinentInfo.get(Continent.SOUTH_AMERICA), SOUTH_AMERICA_BONUS));
        toStream.add(Arguments.of(allContinentInfo.get(Continent.AFRICA), AFRICA_BONUS));
        toStream.add(Arguments.of(allContinentInfo.get(Continent.NORTH_AMERICA), NORTH_AMERICA_BONUS));
        toStream.add(Arguments.of(allContinentInfo.get(Continent.EUROPE), EUROPE_BONUS));
        toStream.add(Arguments.of(allContinentInfo.get(Continent.ASIA), ASIA_BONUS));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateSetsOfTerritoriesFullContinentsAndExpectedBonuses")
    public void test26_calculatePlacementPhaseArmiesForCurrentPlayer_playerOwnsContinent_expectBonusAndTerritories(
            Set<TerritoryType> ownedTerritories, int continentBonus) {
        testCalculatePlacementPhaseNumArmiesMethod(ownedTerritories, continentBonus);
    }

    private static Stream<Arguments> generateMultiContinentSetsAndAssociatedBonus() {
        Map<Continent, Set<TerritoryType>> allContinentInfo = getContinentTerritorySets();
        Map<Continent, Integer> continentToBonusMap = Map.ofEntries(
                Map.entry(Continent.ASIA, ASIA_BONUS),
                Map.entry(Continent.AFRICA, AFRICA_BONUS),
                Map.entry(Continent.SOUTH_AMERICA, SOUTH_AMERICA_BONUS),
                Map.entry(Continent.EUROPE, EUROPE_BONUS),
                Map.entry(Continent.NORTH_AMERICA, NORTH_AMERICA_BONUS),
                Map.entry(Continent.OCEANIA, OCEANIA_BONUS)
        );

        Set<Arguments> toStream = new HashSet<>();

        // 2-continent sets
        Set<TerritoryType> twoContinentTerritories = new HashSet<>(allContinentInfo.get(Continent.EUROPE));
        twoContinentTerritories.addAll(allContinentInfo.get(Continent.ASIA));
        int twoContinentExpectedBonus = continentToBonusMap.get(Continent.EUROPE)
                + continentToBonusMap.get(Continent.ASIA);
        toStream.add(Arguments.of(twoContinentTerritories, twoContinentExpectedBonus));

        twoContinentTerritories = new HashSet<>(allContinentInfo.get(Continent.AFRICA));
        twoContinentTerritories.addAll(allContinentInfo.get(Continent.NORTH_AMERICA));
        twoContinentExpectedBonus = continentToBonusMap.get(Continent.AFRICA)
                + continentToBonusMap.get(Continent.NORTH_AMERICA);
        toStream.add(Arguments.of(twoContinentTerritories, twoContinentExpectedBonus));

        // 3-continent set
        Set<TerritoryType> threeContinentTerritories = new HashSet<>(allContinentInfo.get(Continent.NORTH_AMERICA));
        threeContinentTerritories.addAll(allContinentInfo.get(Continent.SOUTH_AMERICA));
        threeContinentTerritories.addAll(allContinentInfo.get(Continent.AFRICA));
        int threeContinentExpectedBonus = continentToBonusMap.get(Continent.NORTH_AMERICA)
                + continentToBonusMap.get(Continent.SOUTH_AMERICA) + continentToBonusMap.get(Continent.AFRICA);
        toStream.add(Arguments.of(threeContinentTerritories, threeContinentExpectedBonus));

        // 4-continent set
        Set<TerritoryType> fourContinentTerritories = new HashSet<>(threeContinentTerritories);
        fourContinentTerritories.addAll(allContinentInfo.get(Continent.ASIA));
        int fourContinentExpectedBonus = threeContinentExpectedBonus + continentToBonusMap.get(Continent.ASIA);
        toStream.add(Arguments.of(fourContinentTerritories, fourContinentExpectedBonus));

        // 5-continent set
        Set<TerritoryType> fiveContinentTerritories = new HashSet<>(fourContinentTerritories);
        fiveContinentTerritories.addAll(allContinentInfo.get(Continent.OCEANIA));
        int fiveContinentExpectedBonus = fourContinentExpectedBonus + continentToBonusMap.get(Continent.OCEANIA);
        toStream.add(Arguments.of(fiveContinentTerritories, fiveContinentExpectedBonus));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateMultiContinentSetsAndAssociatedBonus")
    public void test27_calculatePlacementPhaseArmiesForCurrentPlayer_inputIsMultipleContinents_expectBonusAndNormal(
            Set<TerritoryType> ownedTerritories, int multiContinentBonus) {
        testCalculatePlacementPhaseNumArmiesMethod(ownedTerritories, multiContinentBonus);
    }

    private static Stream<Arguments> generateAllTerritoriesAndSomeNumCardsToHave() {
        Set<Arguments> toStream = new HashSet<>();
        List<Integer> cardAmountsGreaterThanOrEqualToFive = List.of(5, 6, 7, 42, 44);

        for (TerritoryType territory : TerritoryType.values()) {
            for (Integer cardAmount : cardAmountsGreaterThanOrEqualToFive) {
                toStream.add(Arguments.of(territory, cardAmount));
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoriesAndSomeNumCardsToHave")
    public void test28_placeNewArmiesInTerritory_placementPhase_playerHasTooManyCards_expectException(
            TerritoryType territoryToAttemptPlacingIn, Integer invalidCardAmount) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);

        Player mockedPlayer = createMockedPlayerWithGetNumCardsHeldExpectation(invalidCardAmount);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));

        int numArmiesToTryPlacing = 2;

        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(territoryToAttemptPlacingIn, numArmiesToTryPlacing));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Player cannot place armies while they are holding more than 5 cards!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer);
    }

    private Player createMockedPlayerWithGetNumCardsHeldExpectation(int returnValueForExpectedCall) {
        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(PlayerColor.RED)
                .addMockedMethod("getNumCardsHeld")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumCardsHeld()).andReturn(returnValueForExpectedCall);
        EasyMock.replay(mockedPlayer);

        return mockedPlayer;
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -2, -1, 0})
    public void test29_placeNewArmiesInTerritory_placementPhase_invalidNumberOfArmies_expectException(
            int illegalNumArmiesToPlace) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Player mockedPlayer = createMockedPlayerWithGetNumCardsHeldExpectation(1);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(TerritoryType.ALASKA, illegalNumArmiesToPlace));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Cannot place < 1 army on a territory during the Placement phase";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer);
    }

    @ParameterizedTest
    @EnumSource(TerritoryType.class)
    public void test30_placeNewArmiesInTerritory_placementPhase_playerDoesNotOwnTerritory_expectException(
            TerritoryType relevantTerritory) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Player mockedPlayer = createMockedPlayerWithGetNumCardsHeldExpectation(1);

        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedTerritory.isOwnedByPlayer(PlayerColor.RED)).andReturn(false);

        EasyMock.replay(mockedTerritory);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);

        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory,
                mockedTerritory, GET_TERRITORY_ONCE);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        int validNumArmiesToPlace = 4;
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, validNumArmiesToPlace));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Cannot place armies on a territory you do not own";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer, mockedTerritory, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoriesAndPlayerColorsAndSomeArmyCounts")
    public void test31_placeNewArmiesInTerritory_placementPhase_playerDoesNotHaveEnoughArmies_expectException(
            TerritoryType relevantTerritory, PlayerColor playerColor, int numArmiesPlayerCurrentlyHas) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(playerColor)
                .addMockedMethod("getNumCardsHeld")
                .addMockedMethod("getNumArmiesToPlace")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumCardsHeld()).andReturn(3);
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(numArmiesPlayerCurrentlyHas);

        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedTerritory.isOwnedByPlayer(playerColor)).andReturn(true);

        EasyMock.replay(mockedPlayer, mockedTerritory);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(playerColor);

        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory,
                mockedTerritory, GET_TERRITORY_ONCE);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, numArmiesPlayerCurrentlyHas + 1));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Player does not have enough armies to place!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer, mockedTerritory, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoriesAndPlayerColorsAndSomeArmyCounts")
    public void test32_placeNewArmiesInTerritory_placementPhase_inputIsValid_expectTrue(
            TerritoryType relevantTerritory, PlayerColor currentPlayer, int validNumArmiesToPlace) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(currentPlayer)
                .addMockedMethod("getNumCardsHeld")
                .addMockedMethod("getNumArmiesToPlace")
                .addMockedMethod("setNumArmiesToPlace")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumCardsHeld()).andReturn(3);
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(validNumArmiesToPlace + 1).anyTimes();
        mockedPlayer.setNumArmiesToPlace(1);
        EasyMock.expectLastCall().once();

        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedTerritory.isOwnedByPlayer(currentPlayer)).andReturn(true);
        EasyMock.expect(mockedTerritory.getNumArmiesPresent()).andReturn(1); // some safe amount.
        EasyMock.expect(mockedTerritory.setNumArmiesPresent(validNumArmiesToPlace + 1)).andReturn(true);

        EasyMock.replay(mockedPlayer, mockedTerritory);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(currentPlayer);

        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory,
                mockedTerritory, GET_TERRITORY_TWICE);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, validNumArmiesToPlace));
        assertEquals(GamePhase.PLACEMENT, unitUnderTest.getCurrentGamePhase());

        EasyMock.verify(mockedPlayer, mockedTerritory, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoriesAndPlayerColorsAndSomeArmyCounts")
    public void test33_placeNewArmiesInTerritory_placementPhase_inputIsValid_expectTrueAndAdvanceToAttackPhase(
            TerritoryType relevantTerritory, PlayerColor currentPlayer, int validNumArmiesToPlace) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(currentPlayer)
                .addMockedMethod("getNumCardsHeld")
                .addMockedMethod("getNumArmiesToPlace")
                .addMockedMethod("setNumArmiesToPlace")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumCardsHeld()).andReturn(3);
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(validNumArmiesToPlace).times(2);
        mockedPlayer.setNumArmiesToPlace(0);
        EasyMock.expectLastCall().once();
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(0).once();

        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedTerritory.isOwnedByPlayer(currentPlayer)).andReturn(true);
        EasyMock.expect(mockedTerritory.getNumArmiesPresent()).andReturn(1); // some safe amount.
        EasyMock.expect(mockedTerritory.setNumArmiesPresent(validNumArmiesToPlace + 1)).andReturn(true);

        EasyMock.replay(mockedPlayer, mockedTerritory);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(currentPlayer);

        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory,
                mockedTerritory, GET_TERRITORY_TWICE);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, validNumArmiesToPlace));
        assertEquals(GamePhase.ATTACK, unitUnderTest.getCurrentGamePhase());

        EasyMock.verify(mockedPlayer, mockedTerritory, mockedGraph);
    }

    private static Stream<Arguments> generateInvalidTradeInSets() {
        Set<Arguments> toStream = new HashSet<>();

        // make sure that all error types make it through.
        toStream.add(Arguments.of(Set.of(), "must trade in exactly 3 cards"));
        toStream.add(Arguments.of(Set.of(new WildCard()), "must trade in exactly 3 cards"));
        toStream.add(Arguments.of(Set.of(
                        new TerritoryCard(TerritoryType.CONGO, PieceType.CAVALRY),
                        new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY),
                        new TerritoryCard(TerritoryType.BRAZIL, PieceType.CAVALRY)),
                "invalid trade in set"));
        toStream.add(Arguments.of(Set.of(
                        new TerritoryCard(TerritoryType.CONGO, PieceType.INFANTRY),
                        new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY),
                        new TerritoryCard(TerritoryType.BRAZIL, PieceType.CAVALRY)),
                "invalid trade in set"));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateInvalidTradeInSets")
    public void test34_tradeInCards_invalidTradeInSet_expectException(
            Set<Card> illegalTradeInSet, String associatedErrorMessage) {
        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(PlayerColor.RED)
                .addMockedMethod("ownsAllGivenCards")
                .createMock();
        EasyMock.expect(mockedPlayer.ownsAllGivenCards(illegalTradeInSet)).andReturn(true);

        TradeInParser mockedParser = EasyMock.createMock(TradeInParser.class);
        EasyMock.expect(mockedParser.startTrade(illegalTradeInSet))
                .andThrow(new IllegalStateException(associatedErrorMessage));
        EasyMock.replay(mockedParser, mockedPlayer);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTradeInParser(mockedParser);
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.tradeInCards(illegalTradeInSet));
        String actualMessage = exception.getMessage();

        String expectedMessage = String.format("Could not trade in cards: %s", associatedErrorMessage);
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedParser);
    }

    private static Stream<Arguments> generateValidTradeInSets() {
        Set<Arguments> toStream = new HashSet<>();

        // generate the trade in sets, let them all be valid too
        Set<Set<Card>> tradeInSets = Set.of(
                Set.of(new WildCard(), new WildCard(), new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY)),
                Set.of(new TerritoryCard(TerritoryType.BRAZIL, PieceType.CAVALRY),
                        new TerritoryCard(TerritoryType.ARGENTINA, PieceType.ARTILLERY),
                        new TerritoryCard(TerritoryType.NORTHWEST_TERRITORY, PieceType.INFANTRY)),
                Set.of(new TerritoryCard(TerritoryType.INDONESIA, PieceType.ARTILLERY),
                        new WildCard(),
                        new TerritoryCard(TerritoryType.CONGO, PieceType.CAVALRY)),
                Set.of(new TerritoryCard(TerritoryType.GREAT_BRITAIN, PieceType.ARTILLERY),
                        new TerritoryCard(TerritoryType.CENTRAL_AMERICA, PieceType.CAVALRY),
                        new TerritoryCard(TerritoryType.ALBERTA, PieceType.INFANTRY))
        );

        for (Set<Card> tradeInSet : tradeInSets) {
            toStream.add(Arguments.of(tradeInSet));
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateValidTradeInSets")
    public void test35_tradeInCards_playerDoesNotOwnAllCards_expectException(Set<Card> attemptedTradeInCards) {
        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(PlayerColor.RED)
                .addMockedMethod("ownsAllGivenCards")
                .createMock();
        EasyMock.expect(mockedPlayer.ownsAllGivenCards(attemptedTradeInCards)).andReturn(false);

        EasyMock.replay(mockedPlayer);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);
        unitUnderTest.setGamePhase(GamePhase.PLACEMENT);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.tradeInCards(attemptedTradeInCards));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Player doesn't own all the selected cards!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer);
    }

    private static Stream<Arguments> generateGamePhasesMinusValidTradeInOnes() {
        Set<Arguments> toStream = new HashSet<>();

        List<GamePhase> gamePhases = new ArrayList<>(List.of(GamePhase.values()));
        gamePhases.remove(GamePhase.ATTACK);
        gamePhases.remove(GamePhase.PLACEMENT);

        for (GamePhase gamePhase : gamePhases) {
            toStream.add(Arguments.of(gamePhase));
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateGamePhasesMinusValidTradeInOnes")
    public void test36_tradeInCards_invalidGamePhase_expectException(GamePhase currentPhase) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        unitUnderTest.setGamePhase(currentPhase);

        Exception exception = assertThrows(IllegalStateException.class, () -> unitUnderTest.tradeInCards(Set.of()));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Can only trade in cards during the PLACEMENT or ATTACK phase";
        assertEquals(expectedMessage, actualMessage);
    }

    private void testTradeInCards(
            int numArmiesToSetForPlayer, int cardReturnBonus, Set<TerritoryType> territoriesToReturn) {
        TerritoryCard alaskaCard = new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY);
        TerritoryCard brazilCard = new TerritoryCard(TerritoryType.BRAZIL, PieceType.ARTILLERY);
        Set<Card> cardsToBeTradedIn = Set.of(new WildCard(), alaskaCard, brazilCard);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(PlayerColor.BLUE)
                .addMockedMethod("ownsAllGivenCards")
                .addMockedMethod("setNumArmiesToPlace")
                .addMockedMethod("getNumArmiesToPlace")
                .addMockedMethod("removeAllGivenCards")
                .createMock();
        EasyMock.expect(mockedPlayer.ownsAllGivenCards(cardsToBeTradedIn)).andReturn(true);
        EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(numArmiesToSetForPlayer - cardReturnBonus);
        mockedPlayer.setNumArmiesToPlace(numArmiesToSetForPlayer);
        EasyMock.expectLastCall().once();

        mockedPlayer.removeAllGivenCards(cardsToBeTradedIn);
        EasyMock.expectLastCall().once();

        TradeInParser mockedParser = EasyMock.createMock(TradeInParser.class);
        EasyMock.expect(mockedParser.startTrade(cardsToBeTradedIn)).andReturn(cardReturnBonus);
        EasyMock.expect(mockedParser.getMatchedTerritories(mockedPlayer, cardsToBeTradedIn))
                .andReturn(territoriesToReturn);

        EasyMock.replay(mockedPlayer, mockedParser);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTradeInParser(mockedParser);
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.BLUE);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        assertEquals(territoriesToReturn, unitUnderTest.tradeInCards(cardsToBeTradedIn));

        EasyMock.verify(mockedPlayer, mockedParser);
    }

    @Test
    public void test37_tradeInCards_validInput_expectReturnSetOfSizeTwo() {
        testTradeInCards(5, 4, Set.of(TerritoryType.ALASKA, TerritoryType.BRAZIL));
    }

    @Test
    public void test38_tradeInCards_validInput_expectReturnSetOfSizeOne() {
        testTradeInCards(6, 6, Set.of(TerritoryType.BRAZIL));
    }

    @ParameterizedTest
    @EnumSource(TerritoryType.class)
    public void test39_handleErrorCasesForAttackingTerritory_inputIsDuplicatedTerritory_expectException(
            TerritoryType duplicateTerritory) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(duplicateTerritory, duplicateTerritory,
                        3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source and destination territory must be two adjacent territories!";
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateNonAdjacentTerritories() {
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
    @MethodSource("generateNonAdjacentTerritories")
    public void test40_handleErrorCasesForAttackingTerritory_inputTerritoriesAreNotAdjacent_expectException(
            TerritoryType firstTerritory, TerritoryType nonAdjacentTerritory) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(firstTerritory, nonAdjacentTerritory)).andReturn(false);

        EasyMock.replay(mockedGraph);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(firstTerritory, nonAdjacentTerritory, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source and destination territory must be two adjacent territories!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerMinusSetupCombinations")
    public void test41_handleErrorCasesForAttackingTerritory_sourceTerritoryNotOwnedByPlayer_expectException(
            TerritoryType sourceTerritory, PlayerColor currentPlayer) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(currentPlayer);

        Territory mockedTerritory = createMockedTerritoryReturnsFalseForIsOwnedBy(currentPlayer);
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(sourceTerritory, TerritoryType.ALASKA)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(sourceTerritory)).andReturn(mockedTerritory).anyTimes();

        EasyMock.replay(mockedGraph);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(sourceTerritory, TerritoryType.ALASKA, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source territory is not owned by the current player!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypesAndPlayerMinusSetupCombinations")
    public void test42_handleErrorCasesForAttackingTerritory_destinationTerritoryOwnedByCurrentPlayer_expectException(
            TerritoryType destinationTerritory, PlayerColor currentPlayer) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(currentPlayer);

        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedTerritory.isOwnedByPlayer(currentPlayer)).andReturn(true);

        Territory mockedAlaska = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedAlaska.isOwnedByPlayer(currentPlayer)).andReturn(true);

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(TerritoryType.ALASKA, destinationTerritory)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(TerritoryType.ALASKA)).andReturn(mockedAlaska);
        EasyMock.expect(mockedGraph.getTerritory(destinationTerritory)).andReturn(mockedTerritory);

        EasyMock.replay(mockedGraph, mockedTerritory, mockedAlaska);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(TerritoryType.ALASKA, destinationTerritory,
                        3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Destination territory is owned by the current player!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedGraph, mockedTerritory, mockedAlaska);
    }

    private static Stream<Arguments> generateAllPhasesBesidesAttack() {
        Set<Arguments> toStream = new HashSet<>();

        for (GamePhase gamePhase : GamePhase.values()) {
            if (gamePhase != GamePhase.ATTACK) {
                toStream.add(Arguments.of(gamePhase));
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllPhasesBesidesAttack")
    public void test43_handleErrorCasesForAttackingTerritory_gamePhaseIsNotAttack_expectException(
            GamePhase providedGamePhase) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(providedGamePhase);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(TerritoryType.ALASKA,
                        TerritoryType.NORTHWEST_TERRITORY, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Attacking territories is not allowed in any phase besides attack!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 4, 17, Integer.MAX_VALUE})
    public void test44_handleErrorCasesForAttackingTerritory_invalidAmountOfAttackers_expectException(
            int illegalAttackerAmount) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(TerritoryType.ALASKA,
                        TerritoryType.NORTHWEST_TERRITORY, illegalAttackerAmount, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Number of armies to attack with must be within [1, 3]!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 3, 17, Integer.MAX_VALUE})
    public void test45_handleErrorCasesForAttackingTerritory_invalidAmountOfDefenders_expectException(
            int illegalDefenderAmount) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(TerritoryType.ALASKA,
                        TerritoryType.NORTHWEST_TERRITORY, 3, illegalDefenderAmount));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Number of armies to defend with must be within [1, 2]!";
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 15, Integer.MAX_VALUE})
    public void test46_handleErrorCasesForAttackingTerritory_playerHasTooManyCards_expectException(
            int illegalAmountOfCards) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(PlayerColor.RED)
                .addMockedMethod("getNumCardsHeld")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumCardsHeld()).andReturn(illegalAmountOfCards);

        Territory mockedAlaska = EasyMock.createMock(Territory.class);
        Territory mockedNorthwestTerritories = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedAlaska.isOwnedByPlayer(PlayerColor.RED)).andReturn(true);
        EasyMock.expect(mockedNorthwestTerritories.isOwnedByPlayer(PlayerColor.RED)).andReturn(false);

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(
                TerritoryType.ALASKA, TerritoryType.NORTHWEST_TERRITORY)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(TerritoryType.ALASKA)).andReturn(mockedAlaska);
        EasyMock.expect(mockedGraph.getTerritory(TerritoryType.NORTHWEST_TERRITORY))
                .andReturn(mockedNorthwestTerritories);

        EasyMock.replay(mockedPlayer, mockedGraph, mockedAlaska, mockedNorthwestTerritories);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(TerritoryType.ALASKA,
                        TerritoryType.NORTHWEST_TERRITORY, 3, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Player must trade in cards before they can attack!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer, mockedGraph, mockedAlaska, mockedNorthwestTerritories);
    }

    private static Stream<Arguments> generateInvalidAmountOfAttackerArmiesInTerritoryPairs() {
        return Stream.of(Arguments.of(3, 3), Arguments.of(2, 3), Arguments.of(2, 2),
                Arguments.of(1, 3), Arguments.of(1, 2), Arguments.of(1, 1));
    }

    @ParameterizedTest
    @MethodSource("generateInvalidAmountOfAttackerArmiesInTerritoryPairs")
    public void test47_handleErrorCasesForAttackingTerritory_sourceTerritoryHasTooFewAttackers_expectException(
            int numArmiesInSourceTerritory, int numAttackers) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(PlayerColor.RED)
                .addMockedMethod("getNumCardsHeld")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumCardsHeld()).andReturn(3); // safe, valid amount

        Territory mockedAlaska = EasyMock.createMock(Territory.class);
        Territory mockedNorthwestTerritories = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedAlaska.isOwnedByPlayer(PlayerColor.RED)).andReturn(true);
        EasyMock.expect(mockedAlaska.getNumArmiesPresent()).andReturn(numArmiesInSourceTerritory);
        EasyMock.expect(mockedNorthwestTerritories.isOwnedByPlayer(PlayerColor.RED)).andReturn(false);

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(
                TerritoryType.ALASKA, TerritoryType.NORTHWEST_TERRITORY)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(TerritoryType.ALASKA)).andReturn(mockedAlaska).anyTimes();
        EasyMock.expect(mockedGraph.getTerritory(TerritoryType.NORTHWEST_TERRITORY))
                .andReturn(mockedNorthwestTerritories);

        EasyMock.replay(mockedPlayer, mockedGraph, mockedAlaska, mockedNorthwestTerritories);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(TerritoryType.ALASKA,
                        TerritoryType.NORTHWEST_TERRITORY, numAttackers, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source territory has too few armies to use in this attack!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer, mockedGraph, mockedAlaska, mockedNorthwestTerritories);
    }

    @Test
    public void test48_handleErrorCasesForAttackingTerritory_destinationTerritoryHasTooFewDefenders_expectException() {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(PlayerColor.RED)
                .addMockedMethod("getNumCardsHeld")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumCardsHeld()).andReturn(3); // safe, valid amount

        Territory mockedAlaska = EasyMock.createMock(Territory.class);
        Territory mockedNorthwestTerritories = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedAlaska.isOwnedByPlayer(PlayerColor.RED)).andReturn(true);
        EasyMock.expect(mockedAlaska.getNumArmiesPresent()).andReturn(3);
        EasyMock.expect(mockedNorthwestTerritories.isOwnedByPlayer(PlayerColor.RED)).andReturn(false);
        EasyMock.expect(mockedNorthwestTerritories.getNumArmiesPresent()).andReturn(1);

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(
                TerritoryType.ALASKA, TerritoryType.NORTHWEST_TERRITORY)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(TerritoryType.ALASKA)).andReturn(mockedAlaska).anyTimes();
        EasyMock.expect(mockedGraph.getTerritory(TerritoryType.NORTHWEST_TERRITORY))
                .andReturn(mockedNorthwestTerritories).anyTimes();

        EasyMock.replay(mockedPlayer, mockedGraph, mockedAlaska, mockedNorthwestTerritories);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.handleErrorCasesForAttackingTerritory(TerritoryType.ALASKA,
                        TerritoryType.NORTHWEST_TERRITORY, 2, 2));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Destination territory has too few defenders for this defense!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedPlayer, mockedGraph, mockedAlaska, mockedNorthwestTerritories);
    }

    private static Stream<Arguments> generateAdjacentTerritoryPairs() {
        // just pick a few adjacent territory pairs.
        return Stream.of(
                Arguments.of(TerritoryType.MONGOLIA, TerritoryType.CHINA),
                Arguments.of(TerritoryType.GREAT_BRITAIN, TerritoryType.NORTHERN_EUROPE),
                Arguments.of(TerritoryType.CONGO, TerritoryType.EGYPT),
                Arguments.of(TerritoryType.SIAM, TerritoryType.INDIA),
                Arguments.of(TerritoryType.EASTERN_UNITED_STATES, TerritoryType.WESTERN_UNITED_STATES),
                Arguments.of(TerritoryType.NEW_GUINEA, TerritoryType.INDONESIA)
        );
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test49_handleErrorCasesForAttackingTerritory_validInput_expectNoException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.RED);

        Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(PlayerColor.RED)
                .addMockedMethod("getNumCardsHeld")
                .createMock();
        EasyMock.expect(mockedPlayer.getNumCardsHeld()).andReturn(3); // safe, valid amount

        Territory mockedSource = EasyMock.createMock(Territory.class);
        Territory mockedDestination = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.isOwnedByPlayer(PlayerColor.RED)).andReturn(true);
        EasyMock.expect(mockedSource.getNumArmiesPresent()).andReturn(3);
        EasyMock.expect(mockedDestination.isOwnedByPlayer(PlayerColor.RED)).andReturn(false);
        EasyMock.expect(mockedDestination.getNumArmiesPresent()).andReturn(1);

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(sourceTerritory, destTerritory)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(sourceTerritory)).andReturn(mockedSource).anyTimes();
        EasyMock.expect(mockedGraph.getTerritory(destTerritory)).andReturn(mockedDestination).anyTimes();

        EasyMock.replay(mockedPlayer, mockedGraph, mockedSource, mockedDestination);

        unitUnderTest.provideMockedPlayerObjects(List.of(mockedPlayer));
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertDoesNotThrow(() -> unitUnderTest.handleErrorCasesForAttackingTerritory(
                sourceTerritory, destTerritory, 2, 1));

        EasyMock.verify(mockedPlayer, mockedGraph, mockedSource, mockedDestination);
    }

    private static Stream<Arguments> generateInvalidInputsForRollDiceForBattle() {
        int attackerUpperBound = 3;
        int defenderUpperBound = 2;
        return Stream.of(
                Arguments.of(Integer.MIN_VALUE, 2, attackerUpperBound),
                Arguments.of(-1, 2, attackerUpperBound),
                Arguments.of(0, 2, attackerUpperBound),
                Arguments.of(4, 2, attackerUpperBound),
                Arguments.of(Integer.MAX_VALUE, 2, attackerUpperBound),
                Arguments.of(1, Integer.MIN_VALUE, defenderUpperBound),
                Arguments.of(1, -1, defenderUpperBound),
                Arguments.of(1, 0, defenderUpperBound),
                Arguments.of(1, 3, defenderUpperBound),
                Arguments.of(1, Integer.MAX_VALUE, defenderUpperBound)
        );
    }

    @ParameterizedTest
    @MethodSource("generateInvalidInputsForRollDiceForBattle")
    public void test50_rollDiceForBattle_invalidInput_expectException(
            int numAttackers, int numDefenders, int upperBoundOnError) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideDieRollParser(new DieRollParser());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.rollDiceForBattle(numAttackers, numDefenders));
        String actualMessage = exception.getMessage();

        String expectedMessage = String.format("Valid amount of dice is in the range [1, %d]", upperBoundOnError);
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generateValidInputsForRollDiceAndOutputs() {
        return Stream.of(
                Arguments.of(1, 1, List.of(5), List.of(6), List.of(BattleResult.DEFENDER_VICTORY)),
                Arguments.of(1, 2, List.of(4), List.of(3, 2), List.of(BattleResult.ATTACKER_VICTORY)),
                Arguments.of(2, 1, List.of(5, 2), List.of(5), List.of(BattleResult.DEFENDER_VICTORY)),
                Arguments.of(2, 2, List.of(4, 3), List.of(5, 2), List.of(BattleResult.DEFENDER_VICTORY,
                        BattleResult.ATTACKER_VICTORY)),
                Arguments.of(3, 1, List.of(2, 1, 1), List.of(5), List.of(BattleResult.DEFENDER_VICTORY)),
                Arguments.of(3, 2, List.of(4, 2, 1), List.of(5, 5), List.of(BattleResult.DEFENDER_VICTORY,
                        BattleResult.DEFENDER_VICTORY))
        );
    }

    @ParameterizedTest
    @MethodSource("generateValidInputsForRollDiceAndOutputs")
    public void test51_rollDiceForBattle_validInput_expectSpecificOutput(
            int numAttackers, int numDefenders, List<Integer> attackRolls,
            List<Integer> defenseRolls, List<BattleResult> battleResults) {

        DieRollParser mockedDieRollParser = EasyMock.createMock(DieRollParser.class);
        EasyMock.expect(mockedDieRollParser.rollAttackerDice(numAttackers)).andReturn(attackRolls);
        EasyMock.expect(mockedDieRollParser.rollDefenderDice(numDefenders)).andReturn(defenseRolls);
        EasyMock.expect(mockedDieRollParser.generateBattleResults(attackRolls, defenseRolls)).andReturn(battleResults);

        EasyMock.replay(mockedDieRollParser);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideDieRollParser(mockedDieRollParser);

        assertEquals(battleResults, unitUnderTest.rollDiceForBattle(numAttackers, numDefenders));

        assertEquals(attackRolls, unitUnderTest.getAttackerDiceRolls());
        assertEquals(defenseRolls, unitUnderTest.getDefenderDiceRolls());
        assertEquals(battleResults, unitUnderTest.getBattleResults());

        EasyMock.verify(mockedDieRollParser);
    }

    private static Stream<Arguments> generateNoChangeInputsForHandleArmyLosses() {
        return Stream.of(
                Arguments.of(TerritoryType.SIAM, TerritoryType.CHINA, List.of(BattleResult.ATTACKER_VICTORY)),
                Arguments.of(TerritoryType.SIAM, TerritoryType.CHINA,
                        List.of(BattleResult.ATTACKER_VICTORY, BattleResult.ATTACKER_VICTORY)),
                Arguments.of(TerritoryType.BRAZIL, TerritoryType.PERU,
                        List.of(BattleResult.ATTACKER_VICTORY, BattleResult.DEFENDER_VICTORY)),
                Arguments.of(TerritoryType.URAL, TerritoryType.UKRAINE,
                        List.of(BattleResult.DEFENDER_VICTORY, BattleResult.DEFENDER_VICTORY)),
                Arguments.of(TerritoryType.INDONESIA, TerritoryType.NEW_GUINEA, List.of(BattleResult.DEFENDER_VICTORY))
        );
    }

    @ParameterizedTest
    @MethodSource("generateNoChangeInputsForHandleArmyLosses")
    public void test52_handleArmyLosses_inputsResultInNoChange_expectNoChangeAsResult(
            TerritoryType sourceTerritory, TerritoryType destinationTerritory, List<BattleResult> battleResults) {

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.getNumArmiesPresent()).andReturn(3).anyTimes();
        int numAttackersRemaining = 3 - Collections.frequency(battleResults, BattleResult.DEFENDER_VICTORY);
        EasyMock.expect(mockedSource.setNumArmiesPresent(numAttackersRemaining)).andReturn(true).anyTimes();

        Territory mockedDest = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDest.getNumArmiesPresent()).andReturn(3).anyTimes();
        int numDefendersRemaining = 3 - Collections.frequency(battleResults, BattleResult.ATTACKER_VICTORY);
        EasyMock.expect(mockedDest.setNumArmiesPresent(numDefendersRemaining)).andReturn(true).anyTimes();

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.getTerritory(sourceTerritory)).andReturn(mockedSource).anyTimes();
        EasyMock.expect(mockedGraph.getTerritory(destinationTerritory)).andReturn(mockedDest).anyTimes();

        EasyMock.replay(mockedSource, mockedDest, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertEquals(AttackConsequence.NO_CHANGE,
                unitUnderTest.handleArmyLosses(sourceTerritory, destinationTerritory, battleResults));

        EasyMock.verify(mockedSource, mockedDest, mockedGraph);
    }

    private static Stream<Arguments> generateDefenderLosesTerritoryInputsForHandleArmyLosses() {
        return Stream.of(
                Arguments.of(TerritoryType.SIAM, TerritoryType.CHINA, List.of(BattleResult.ATTACKER_VICTORY), 1),
                Arguments.of(TerritoryType.SIAM, TerritoryType.CHINA,
                        List.of(BattleResult.ATTACKER_VICTORY, BattleResult.ATTACKER_VICTORY), 2),
                Arguments.of(TerritoryType.EGYPT, TerritoryType.CONGO, List.of(BattleResult.ATTACKER_VICTORY), 1),
                Arguments.of(TerritoryType.NORTHERN_EUROPE, TerritoryType.GREAT_BRITAIN,
                        List.of(BattleResult.ATTACKER_VICTORY, BattleResult.ATTACKER_VICTORY), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("generateDefenderLosesTerritoryInputsForHandleArmyLosses")
    public void test53_handleArmyLosses_inputsResultInDefenderLosingTerritory_expectDefenderLosesTerritory(
            TerritoryType sourceTerritory, TerritoryType destinationTerritory, List<BattleResult> battleResults,
            int initialNumDefenders) {

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.getNumArmiesPresent()).andReturn(3).anyTimes();
        EasyMock.expect(mockedSource.setNumArmiesPresent(3)).andReturn(true); // note that the attacker will
        // never lose any armies in an attack in the event that they take over a territory

        Territory mockedDest = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDest.getNumArmiesPresent()).andReturn(initialNumDefenders).once();
        EasyMock.expect(mockedDest.setNumArmiesPresent(0)).andReturn(true).anyTimes();
        EasyMock.expect(mockedDest.getNumArmiesPresent()).andReturn(0).once();

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.getTerritory(sourceTerritory)).andReturn(mockedSource).anyTimes();
        EasyMock.expect(mockedGraph.getTerritory(destinationTerritory)).andReturn(mockedDest).anyTimes();

        EasyMock.replay(mockedSource, mockedDest, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertEquals(AttackConsequence.DEFENDER_LOSES_TERRITORY,
                unitUnderTest.handleArmyLosses(sourceTerritory, destinationTerritory, battleResults));

        EasyMock.verify(mockedSource, mockedDest, mockedGraph);
    }

    private static Stream<Arguments> generateAllValidCombosForHandleAttacker() {
        Set<Arguments> toStream = new HashSet<>();
        List<Integer> validNumAttackers = List.of(1, 2, 3);
        List<PlayerColor> validPlayerColors = new ArrayList<>(List.of(PlayerColor.values()));
        validPlayerColors.remove(PlayerColor.SETUP);

        for (TerritoryType territory : TerritoryType.values()) {
            for (Integer numAttackers : validNumAttackers) {
                for (PlayerColor attackingPlayer : validPlayerColors) {
                    for (PlayerColor defendingPlayer : validPlayerColors) {
                        if (attackingPlayer != defendingPlayer) {
                            toStream.add(Arguments.of(territory, numAttackers, attackingPlayer, defendingPlayer));
                        }
                    }
                }
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllValidCombosForHandleAttacker")
    public void test54_handleAttackerTakingTerritory_validInput_expectPlayerColorOfDefendingPlayer(
            TerritoryType destTerritory, int numAttackers, PlayerColor attackingPlayer, PlayerColor defendingPlayer) {

        Territory mockedDestTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDestTerritory.isOwnedByPlayer(attackingPlayer)).andReturn(false).once();
        EasyMock.expect(mockedDestTerritory.isOwnedByPlayer(defendingPlayer)).andReturn(true).once();
        EasyMock.expect(mockedDestTerritory.setNumArmiesPresent(numAttackers)).andReturn(true).once();
        EasyMock.expect(mockedDestTerritory.setPlayerInControl(attackingPlayer)).andReturn(true).once();

        Player mockedAttacker = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(attackingPlayer)
                .addMockedMethod("addTerritoryToCollection")
                .createMock();
        mockedAttacker.addTerritoryToCollection(destTerritory);
        EasyMock.expectLastCall().once();

        Player mockedDefender = EasyMock.partialMockBuilder(Player.class)
                .withConstructor(PlayerColor.class)
                .withArgs(defendingPlayer)
                .addMockedMethod("removeTerritoryFromCollection")
                .createMock();
        mockedDefender.removeTerritoryFromCollection(destTerritory);
        EasyMock.expectLastCall().once();

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.getTerritory(destTerritory)).andReturn(mockedDestTerritory).anyTimes();

        EasyMock.replay(mockedDestTerritory, mockedAttacker, mockedDefender, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.setPlayerOrderList(List.of(attackingPlayer, defendingPlayer));
        unitUnderTest.provideMockedPlayerObjects(List.of(mockedAttacker, mockedDefender));

        assertEquals(defendingPlayer, unitUnderTest.handleAttackerTakingTerritory(destTerritory, numAttackers));

        EasyMock.verify(mockedDestTerritory, mockedAttacker, mockedDefender, mockedGraph);
    }

    private static Stream<Arguments> generateInputsForHandlePlayerLosingGameIfNecessary() {
        Set<Arguments> toStream = new HashSet<>();
        List<Integer> ownedTerritoryIndex = List.of(1, 2, 17, 30, 41);
        List<PlayerColor> playerInQuestion = List.of(PlayerColor.PURPLE, PlayerColor.BLACK, PlayerColor.YELLOW,
                PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED);
        // we should make these lists mutable, so we can potentially remove players...
        List<List<PlayerColor>> playersListContainingPotentiallyLosingPlayer = List.of(
                new ArrayList<>(List.of(PlayerColor.BLACK, PlayerColor.PURPLE, PlayerColor.GREEN)),
                new ArrayList<>(List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLACK, PlayerColor.BLUE)),
                new ArrayList<>(List.of(PlayerColor.BLUE, PlayerColor.YELLOW)),
                new ArrayList<>(List.of(PlayerColor.PURPLE, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.RED,
                        PlayerColor.BLACK, PlayerColor.YELLOW)),
                new ArrayList<>(List.of(PlayerColor.YELLOW, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.PURPLE,
                        PlayerColor.BLUE)),
                new ArrayList<>(List.of(PlayerColor.RED, PlayerColor.PURPLE))
        );

        for (Integer index : ownedTerritoryIndex) {
            for (PlayerColor player : playerInQuestion) {
                int indexOfPlayer = playerInQuestion.indexOf(player);
                toStream.add(Arguments.of(index, player,
                        playersListContainingPotentiallyLosingPlayer.get(indexOfPlayer)));
            }
        }

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateInputsForHandlePlayerLosingGameIfNecessary")
    public void test55_handlePlayerLosingGameIfNecessary_playerHasNotLost_expectCollectionsToRemainTheSame(
            int ownedTerritoryIndex, PlayerColor potentiallyLosingPlayer, List<PlayerColor> playersList) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        List<Territory> allMockedTerritories = new ArrayList<>();

        int currentIndex = 0;
        for (TerritoryType territory : TerritoryType.values()) {
            Territory mockedTerritory = EasyMock.createMock(Territory.class);
            EasyMock.expect(mockedGraph.getTerritory(territory)).andReturn(mockedTerritory).anyTimes();
            allMockedTerritories.add(mockedTerritory);
            if (currentIndex < ownedTerritoryIndex) {
                EasyMock.expect(mockedTerritory.isOwnedByPlayer(potentiallyLosingPlayer)).andReturn(false);
                EasyMock.replay(mockedTerritory);
                currentIndex++;
            } else {
                EasyMock.expect(mockedTerritory.isOwnedByPlayer(potentiallyLosingPlayer)).andReturn(true);
                EasyMock.replay(mockedTerritory);
                break;
            }
        }
        EasyMock.replay(mockedGraph);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.setPlayerOrderList(playersList);
        List<Player> playerObjects = new ArrayList<>();
        for (PlayerColor player : playersList) {
            playerObjects.add(new Player(player));
        }
        unitUnderTest.provideMockedPlayerObjects(playerObjects);

        unitUnderTest.handlePlayerLosingGameIfNecessary(potentiallyLosingPlayer);

        assertTrue(unitUnderTest.getPlayerOrder().contains(potentiallyLosingPlayer));
        assertTrue(unitUnderTest.getPlayerMap().containsKey(potentiallyLosingPlayer));

        EasyMock.verify(mockedGraph);

        for (Territory mockedTerritory : allMockedTerritories) {
            EasyMock.verify(mockedTerritory);
        }
    }

    private static Stream<Arguments> generateLosingPlayerCardsAndRespectivePlayersAndPlayerList() {
        return Stream.of(
                Arguments.of(
                        Set.of(),
                        PlayerColor.RED, PlayerColor.BLUE,
                        new ArrayList<>(List.of(PlayerColor.RED, PlayerColor.PURPLE,
                                PlayerColor.BLUE, PlayerColor.GREEN))),
                Arguments.of(
                        Set.of(new TerritoryCard(TerritoryType.IRKUTSK, PieceType.INFANTRY),
                                new TerritoryCard(TerritoryType.UKRAINE, PieceType.CAVALRY)),
                        PlayerColor.YELLOW, PlayerColor.GREEN,
                        new ArrayList<>(List.of(PlayerColor.YELLOW, PlayerColor.PURPLE, PlayerColor.GREEN)))
        );
    }

    @ParameterizedTest
    @MethodSource("generateLosingPlayerCardsAndRespectivePlayersAndPlayerList")
    public void test56_handlePlayerLosingGameIfNecessary_playerLosesGame_expectCardTransferAndCollectionsChange(
            Set<Card> losingPlayerCards, PlayerColor attackerColor, PlayerColor defenderColor,
            List<PlayerColor> playersList) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        List<Territory> allMockedTerritories = new ArrayList<>();

        for (TerritoryType territory : TerritoryType.values()) {
            Territory mockedTerritory = EasyMock.createMock(Territory.class);
            EasyMock.expect(mockedTerritory.isOwnedByPlayer(defenderColor)).andReturn(false);
            EasyMock.expect(mockedGraph.getTerritory(territory)).andReturn(mockedTerritory).anyTimes();
            allMockedTerritories.add(mockedTerritory);
            EasyMock.replay(mockedTerritory);
        }
        EasyMock.replay(mockedGraph);
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        Map<PlayerColor, Player> playersMap = new HashMap<>();
        List<Player> mockedPlayerObjects = new ArrayList<>(); // verify that everything happened at the end.
        for (PlayerColor player : playersList) {
            Player mockedPlayer = EasyMock.createMock(Player.class);
            if (player == attackerColor) {
                mockedPlayer.addCardsToCollection(losingPlayerCards);
                EasyMock.expectLastCall().once();
            } else if (player == defenderColor) {
                EasyMock.expect(mockedPlayer.getOwnedCards()).andReturn(losingPlayerCards);
                EasyMock.expectLastCall().once();
            }
            playersMap.put(player, mockedPlayer);
            EasyMock.replay(mockedPlayer);
            mockedPlayerObjects.add(mockedPlayer);
        }
        unitUnderTest.provideMockedPlayersMap(playersMap);
        unitUnderTest.provideCurrentPlayerForTurn(attackerColor);
        unitUnderTest.handlePlayerLosingGameIfNecessary(defenderColor);

        assertFalse(unitUnderTest.getPlayerOrder().contains(defenderColor));
        assertFalse(unitUnderTest.getPlayerMap().containsKey(defenderColor));

        EasyMock.verify(mockedGraph);

        for (Territory mockedTerritory : allMockedTerritories) {
            EasyMock.verify(mockedTerritory);
        }

        for (Player mockedPlayer : mockedPlayerObjects) {
            EasyMock.verify(mockedPlayer);
        }
    }

    private static Stream<Arguments> generateNonWinningArgumentsForHandleCurrentPlayerWinning() {
        Set<Arguments> toStream = new HashSet<>();

        List<Integer> territoryIndices = List.of(2, 3, 4, 17, 30, 31, 40, 41);
        List<PlayerColor> allPlayersWithoutSetup = new ArrayList<>(List.of(PlayerColor.values()));
        allPlayersWithoutSetup.remove(PlayerColor.SETUP);

        for (Integer index : territoryIndices) {
            for (PlayerColor player : allPlayersWithoutSetup) {
                toStream.add(Arguments.of(index, player));
            }
        }
        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateNonWinningArgumentsForHandleCurrentPlayerWinning")
    public void test57_handleCurrentPlayerWinningGameIfNecessary_currentPlayerDoesNotWin_expectGameInAttackPhase(
            int indexOfFirstNonOwnedTerritory, PlayerColor currentPlayer) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        int currentIndex = 0;
        List<Territory> allMockedTerritories = new ArrayList<>();
        for (TerritoryType territory : TerritoryType.values()) {
            Territory mockedTerritory = EasyMock.createMock(Territory.class);
            if (currentIndex == indexOfFirstNonOwnedTerritory) {
                // add one extra territory to stop us from rolling over...
                EasyMock.expect(mockedTerritory.isOwnedByPlayer(currentPlayer)).andReturn(false);
                EasyMock.expect(mockedGraph.getTerritory(territory)).andReturn(mockedTerritory);
                EasyMock.replay(mockedTerritory);
                break;
            }
            EasyMock.expect(mockedTerritory.isOwnedByPlayer(currentPlayer)).andReturn(true);
            EasyMock.expect(mockedGraph.getTerritory(territory)).andReturn(mockedTerritory);
            EasyMock.replay(mockedTerritory);
            allMockedTerritories.add(mockedTerritory);
            currentIndex++;
        }
        EasyMock.replay(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(currentPlayer);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        unitUnderTest.handleCurrentPlayerWinningGameIfNecessary();

        assertEquals(GamePhase.ATTACK, unitUnderTest.getCurrentGamePhase());

        EasyMock.verify(mockedGraph);
        for (Territory mockedTerritory : allMockedTerritories) {
            EasyMock.verify(mockedTerritory);
        }
    }

    private static Stream<Arguments> generateAllPlayerColorsMinusSetup() {
        List<PlayerColor> playersWithoutSetup = new ArrayList<>(List.of(PlayerColor.values()));
        playersWithoutSetup.remove(PlayerColor.SETUP);

        return playersWithoutSetup.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("generateAllPlayerColorsMinusSetup")
    public void test58_handleCurrentPlayerWinningGameIfNecessary_currentPlayerWins_expectGameInGameOverPhase(
            PlayerColor winningPlayer) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        List<Territory> allMockedTerritories = new ArrayList<>();
        for (TerritoryType territory : TerritoryType.values()) {
            Territory mockedTerritory = EasyMock.createMock(Territory.class);
            EasyMock.expect(mockedTerritory.isOwnedByPlayer(winningPlayer)).andReturn(true);
            EasyMock.expect(mockedGraph.getTerritory(territory)).andReturn(mockedTerritory);
            EasyMock.replay(mockedTerritory);
            allMockedTerritories.add(mockedTerritory);
        }
        EasyMock.replay(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(winningPlayer);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);

        unitUnderTest.handleCurrentPlayerWinningGameIfNecessary();

        assertEquals(GamePhase.GAME_OVER, unitUnderTest.getCurrentGamePhase());

        EasyMock.verify(mockedGraph);
        for (Territory mockedTerritory : allMockedTerritories) {
            EasyMock.verify(mockedTerritory);
        }
    }

    @ParameterizedTest
    @MethodSource("generateNonAdjacentTerritories")
    public void test59_moveArmiesBetweenFriendlyTerritories_territoriesAreNotAdjacent_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(sourceTerritory, destTerritory)).andReturn(false);
        EasyMock.replay(mockedGraph);

        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 1));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source and destination territory must be two adjacent territories!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test60_moveArmiesBetweenFriendlyTerritories_sourceIsNotOwnedByCurrentPlayer_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(sourceTerritory, destTerritory)).andReturn(true);

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(false);
        EasyMock.expect(mockedGraph.getTerritory(sourceTerritory)).andReturn(mockedSource);

        EasyMock.replay(mockedSource, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 1));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Provided territories are not owned by the current player!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedSource, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAdjacentTerritoryPairs")
    public void test61_moveArmiesBetweenFriendlyTerritories_destIsNotOwnedByCurrentPlayer_expectException(
            TerritoryType sourceTerritory, TerritoryType destTerritory) {

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(sourceTerritory, destTerritory)).andReturn(true);

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(sourceTerritory)).andReturn(mockedSource);

        Territory mockedDest = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDest.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(false);
        EasyMock.expect(mockedGraph.getTerritory(destTerritory)).andReturn(mockedDest);

        EasyMock.replay(mockedDest, mockedSource, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, 1));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Provided territories are not owned by the current player!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedDest, mockedSource, mockedGraph);
    }

    private static Stream<Arguments> generateInvalidNumArmiesInTerritoryAndAmountToMovePairs() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(1, 2),
                Arguments.of(1, Integer.MAX_VALUE),
                Arguments.of(2, 2),
                Arguments.of(2, 3),
                Arguments.of(2, Integer.MAX_VALUE),
                Arguments.of(3, 3),
                Arguments.of(3, 27),
                Arguments.of(3, Integer.MAX_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("generateInvalidNumArmiesInTerritoryAndAmountToMovePairs")
    public void test62_moveArmiesBetweenFriendlyTerritories_notEnoughArmiesInSource_expectException(
            int numArmiesInTerritory, int invalidAmountToMoveOut) {
        TerritoryType alaska = TerritoryType.ALASKA;
        TerritoryType kamchatka = TerritoryType.KAMCHATKA;

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(alaska, kamchatka)).andReturn(true);

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(true);
        EasyMock.expect(mockedSource.getNumArmiesPresent()).andReturn(numArmiesInTerritory).anyTimes();
        EasyMock.expect(mockedGraph.getTerritory(alaska)).andReturn(mockedSource).anyTimes();

        Territory mockedDest = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDest.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(kamchatka)).andReturn(mockedDest).anyTimes();

        EasyMock.replay(mockedDest, mockedSource, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(alaska, kamchatka, invalidAmountToMoveOut));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Source territory does not have enough armies to support this movement!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedDest, mockedSource, mockedGraph);
    }

    private static Stream<Arguments> generateAllPhasesMinusAttackAndFortify() {
        List<GamePhase> allPhasesMinusAttackAndFortify = new ArrayList<>(List.of(GamePhase.values()));
        allPhasesMinusAttackAndFortify.remove(GamePhase.ATTACK);
        allPhasesMinusAttackAndFortify.remove(GamePhase.FORTIFY);

        return allPhasesMinusAttackAndFortify.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("generateAllPhasesMinusAttackAndFortify")
    public void test63_moveArmiesBetweenFriendlyTerritories_invalidGamePhase_expectException(
            GamePhase invalidGamePhase) {
        TerritoryType alaska = TerritoryType.ALASKA;
        TerritoryType kamchatka = TerritoryType.KAMCHATKA;

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(alaska, kamchatka)).andReturn(true);

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(true);
        EasyMock.expect(mockedSource.getNumArmiesPresent()).andReturn(2).anyTimes();
        EasyMock.expect(mockedGraph.getTerritory(alaska)).andReturn(mockedSource).anyTimes();

        Territory mockedDest = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDest.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(kamchatka)).andReturn(mockedDest).anyTimes();

        EasyMock.replay(mockedDest, mockedSource, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        unitUnderTest.setGamePhase(invalidGamePhase);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(alaska, kamchatka, 1));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Friendly army movement can only be done in the ATTACK or FORTIFY phase!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedDest, mockedSource, mockedGraph);
    }

    private static Stream<Arguments> generateInvalidRecentlyAttackedTerritories() {
        return Stream.of(
                Arguments.of(TerritoryType.IRKUTSK, TerritoryType.KAMCHATKA),
                Arguments.of(TerritoryType.ALASKA, TerritoryType.NORTHWEST_TERRITORY),
                Arguments.of(TerritoryType.UKRAINE, TerritoryType.URAL),
                Arguments.of(TerritoryType.EGYPT, TerritoryType.CONGO),
                Arguments.of(TerritoryType.KAMCHATKA, TerritoryType.ALASKA),
                Arguments.of(TerritoryType.INDIA, TerritoryType.SIAM)
        );
    }

    @ParameterizedTest
    @MethodSource("generateInvalidRecentlyAttackedTerritories")
    public void test64_moveArmiesBetweenFriendlyTerritories_territoriesDoNotMatchRecentlyAttacked_expectException(
            TerritoryType recentlyAttackedSrc, TerritoryType recentlyAttackedDest) {
        TerritoryType alaska = TerritoryType.ALASKA;
        TerritoryType kamchatka = TerritoryType.KAMCHATKA;

        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(alaska, kamchatka)).andReturn(true);

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.isOwnedByPlayer(PlayerColor.GREEN)).andReturn(true);
        EasyMock.expect(mockedSource.getNumArmiesPresent()).andReturn(2).anyTimes();
        EasyMock.expect(mockedGraph.getTerritory(alaska)).andReturn(mockedSource).anyTimes();

        Territory mockedDest = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDest.isOwnedByPlayer(PlayerColor.GREEN)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(kamchatka)).andReturn(mockedDest).anyTimes();

        EasyMock.replay(mockedDest, mockedSource, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.setRecentlyAttackedSource(recentlyAttackedSrc);
        unitUnderTest.setRecentlyAttackedDest(recentlyAttackedDest);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.moveArmiesBetweenFriendlyTerritories(alaska, kamchatka, 1));
        String actualMessage = exception.getMessage();

        String expectedMessage = "Cannot split armies between this source and destination!";
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedDest, mockedSource, mockedGraph);
    }

    private static Stream<Arguments> generateValidInputsForMoveArmiesAttackPhase() {
        return Stream.of(
                Arguments.of(TerritoryType.MONGOLIA, TerritoryType.CHINA, 4, 1),
                Arguments.of(TerritoryType.MONGOLIA, TerritoryType.CHINA, 4, 2),
                Arguments.of(TerritoryType.MONGOLIA, TerritoryType.CHINA, 4, 3),
                Arguments.of(TerritoryType.GREAT_BRITAIN, TerritoryType.NORTHERN_EUROPE, 7, 2),
                Arguments.of(TerritoryType.GREAT_BRITAIN, TerritoryType.NORTHERN_EUROPE, 7, 5),
                Arguments.of(TerritoryType.GREAT_BRITAIN, TerritoryType.NORTHERN_EUROPE, 7, 6),
                Arguments.of(TerritoryType.CONGO, TerritoryType.EGYPT, 2, 1),
                Arguments.of(TerritoryType.SIAM, TerritoryType.INDIA, 75, 74),
                Arguments.of(TerritoryType.EASTERN_UNITED_STATES, TerritoryType.WESTERN_UNITED_STATES, 4, 3),
                Arguments.of(TerritoryType.ALASKA, TerritoryType.KAMCHATKA, 10, 9),
                Arguments.of(TerritoryType.ALASKA, TerritoryType.KAMCHATKA, 10, 5),
                Arguments.of(TerritoryType.ALASKA, TerritoryType.KAMCHATKA, 10, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("generateValidInputsForMoveArmiesAttackPhase")
    public void test65_moveArmiesBetweenFriendlyTerritories_validInputAttackPhase_expectTerritoriesToReflectMovement(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int numArmiesInSource, int numArmiesToMove) {
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(sourceTerritory, destTerritory)).andReturn(true);

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.isOwnedByPlayer(PlayerColor.GREEN)).andReturn(true);
        EasyMock.expect(mockedSource.getNumArmiesPresent()).andReturn(numArmiesInSource).anyTimes();
        EasyMock.expect(mockedSource.setNumArmiesPresent(numArmiesInSource - numArmiesToMove)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(sourceTerritory)).andReturn(mockedSource).anyTimes();

        Territory mockedDest = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDest.isOwnedByPlayer(PlayerColor.GREEN)).andReturn(true);
        EasyMock.expect(mockedDest.getNumArmiesPresent()).andReturn(2).anyTimes();
        EasyMock.expect(mockedDest.setNumArmiesPresent(2 + numArmiesToMove)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(destTerritory)).andReturn(mockedDest).anyTimes();

        EasyMock.replay(mockedDest, mockedSource, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);
        unitUnderTest.setGamePhase(GamePhase.ATTACK);
        unitUnderTest.setRecentlyAttackedSource(sourceTerritory);
        unitUnderTest.setRecentlyAttackedDest(destTerritory);

        unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, numArmiesToMove);

        assertEquals(GamePhase.ATTACK, unitUnderTest.getCurrentGamePhase());
        assertEquals(PlayerColor.GREEN, unitUnderTest.getCurrentPlayer());
        assertNull(unitUnderTest.getRecentlyAttackedSource());
        assertNull(unitUnderTest.getRecentlyAttackedDest());

        EasyMock.verify(mockedDest, mockedSource, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateValidInputsForMoveArmiesAttackPhase")
    public void test66_moveArmiesBetweenFriendlyTerritories_fortifyPhase_endPlayerTurnAndExpectMovedArmies(
            TerritoryType sourceTerritory, TerritoryType destTerritory, int numArmiesInSource, int numArmiesToMove) {
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        EasyMock.expect(mockedGraph.areTerritoriesAdjacent(sourceTerritory, destTerritory)).andReturn(true);

        Territory mockedSource = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedSource.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(true);
        EasyMock.expect(mockedSource.getNumArmiesPresent()).andReturn(numArmiesInSource).anyTimes();
        EasyMock.expect(mockedSource.setNumArmiesPresent(numArmiesInSource - numArmiesToMove)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(sourceTerritory)).andReturn(mockedSource).anyTimes();

        Territory mockedDest = EasyMock.createMock(Territory.class);
        EasyMock.expect(mockedDest.isOwnedByPlayer(PlayerColor.PURPLE)).andReturn(true);
        EasyMock.expect(mockedDest.getNumArmiesPresent()).andReturn(2).anyTimes();
        EasyMock.expect(mockedDest.setNumArmiesPresent(2 + numArmiesToMove)).andReturn(true);
        EasyMock.expect(mockedGraph.getTerritory(destTerritory)).andReturn(mockedDest).anyTimes();

        EasyMock.replay(mockedDest, mockedSource, mockedGraph);

        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.PURPLE);
        unitUnderTest.setPlayerOrderList(List.of(PlayerColor.PURPLE, PlayerColor.GREEN));
        unitUnderTest.setGamePhase(GamePhase.FORTIFY);

        unitUnderTest.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destTerritory, numArmiesToMove);

        assertEquals(GamePhase.PLACEMENT, unitUnderTest.getCurrentGamePhase());
        assertEquals(PlayerColor.GREEN, unitUnderTest.getCurrentPlayer());
        assertNull(unitUnderTest.getRecentlyAttackedSource());
        assertNull(unitUnderTest.getRecentlyAttackedDest());

        EasyMock.verify(mockedDest, mockedSource, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllPhasesMinusAttackAndFortify")
    public void test67_forceGamePhaseToEnd_invalidGamePhase_expectException(GamePhase invalidGamePhase) {
        WorldDominationGameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.setGamePhase(invalidGamePhase);
        unitUnderTest.provideCurrentPlayerForTurn(PlayerColor.GREEN);

        Exception exception = assertThrows(IllegalStateException.class, unitUnderTest::forceGamePhaseToEnd);
        String actualMessage = exception.getMessage();

        String expectedMessage = "Cannot forcibly end this game phase!";
        assertEquals(expectedMessage, actualMessage);
    }

}
