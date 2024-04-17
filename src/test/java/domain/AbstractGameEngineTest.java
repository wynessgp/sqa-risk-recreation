package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class AbstractGameEngineTest {

    // this testing file will only test things ALL the win conditions share.
    // namely, the non-abstract methods in GameEngine.

    @ParameterizedTest
    @ValueSource(ints = {-1, 1, 7})
    public void test00_initializePlayersList_allIllegalInputs_expectException(int illegalInput) {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> emptyPlayerColorList = List.of();

        String expectedMessage = "amountOfPlayers is not within: [2, 6]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(emptyPlayerColorList, illegalInput));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test01_initializePlayersList_sevenPlayersInIntAndPlayerOrderListIsSizeSeven_expectException() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> sizeSevenPlayerColorList = List.of(PlayerColor.BLUE, PlayerColor.BLUE, PlayerColor.SETUP,
                PlayerColor.PURPLE, PlayerColor.YELLOW, PlayerColor.YELLOW, PlayerColor.BLACK);
        int amountOfPlayers = 7;

        String expectedMessage = "amountOfPlayers is not within: [2, 6]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(sizeSevenPlayerColorList, amountOfPlayers));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test02_initializePlayersList_sizeMismatchBetweenOrderAndNumberPlayers_expectException() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> emptyList = List.of();
        int amountOfPlayers = 2;

        String expectedMessage = "Size mismatch between playerOrder: 0 and amountOfPlayers: 2";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(emptyList, amountOfPlayers));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test03_initializePlayersList_sizeMismatchBetweenOrderAndNumberPlayers_validAmounts_expectException() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> sizeThreePlayerColorList = List.of(PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.PURPLE);
        int amountOfPlayers = 2;

        String expectedMessage = "Size mismatch between playerOrder: 3 and amountOfPlayers: 2";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(sizeThreePlayerColorList, amountOfPlayers));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test04_initializePlayersList_playerOrderContainsDuplicates_expectException() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> listWithDupes = List.of(PlayerColor.YELLOW, PlayerColor.YELLOW, PlayerColor.SETUP);
        int amountOfPlayers = 3;

        String expectedMessage = "Player order contains duplicate entries";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(listWithDupes, amountOfPlayers));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test05_initializePlayersList_playerOrderContainsDuplicatesListSizeFour_expectException() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> listWithDupes = List.of(
                PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.RED);
        int amountOfPlayers = 4;

        String expectedMessage = "Player order contains duplicate entries";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(listWithDupes, amountOfPlayers));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test06_initializePlayersList_playerOrderContainsSetupListSizeThree_expectException() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> listWithSetup = List.of(PlayerColor.BLUE, PlayerColor.RED, PlayerColor.SETUP);

        String expectedMessage = "Player order contains SETUP as one of the players";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(listWithSetup, listWithSetup.size()));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generatePlayerLists_sizeSix_withSetupType() {
        Set<Arguments> arguments = new HashSet<>();
        Set<PlayerColor> validPlayers = new HashSet<>(Arrays.asList(PlayerColor.values()));
        validPlayers.remove(PlayerColor.NEUTRAL);
        validPlayers.remove(PlayerColor.SETUP);

        for (PlayerColor player : validPlayers) {
            List<PlayerColor> playerListVariant = new ArrayList<>(validPlayers);
            int indexToReplace = playerListVariant.indexOf(player);
            playerListVariant.remove(indexToReplace);
            playerListVariant.add(indexToReplace, PlayerColor.SETUP);
            arguments.add(Arguments.of(playerListVariant));
        }
        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("generatePlayerLists_sizeSix_withSetupType")
    public void test07_initializePlayersList_playerOrderContainsSetupSizeSixAllIndices_expectException(
            List<PlayerColor> listWithSetup) {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        int amountOfPlayers = 6;

        String expectedMessage = "Player order contains SETUP as one of the players";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.initializePlayersList(listWithSetup, amountOfPlayers));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> generatePlayerLists_sizesTwoThroughSix() {
        Set<Arguments> arguments = new HashSet<>();
        Set<PlayerColor> validPlayers = new HashSet<>(Arrays.asList(PlayerColor.values()));
        validPlayers.remove(PlayerColor.NEUTRAL);
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
    @MethodSource("generatePlayerLists_sizesTwoThroughSix")
    public void test08_initializePlayersList_validInput_expectTrue(List<PlayerColor> validPlayerOrder) {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        int amountOfPlayers = validPlayerOrder.size();

        assertTrue(unitUnderTest.initializePlayersList(validPlayerOrder, amountOfPlayers));
    }

    @ParameterizedTest
    @MethodSource("generatePlayerLists_sizesTwoThroughSix")
    public void test09_initializePlayersList_validInputEnsureListIsSet_expectTrue(List<PlayerColor> validPlayerOrder) {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        int amountOfPlayers = validPlayerOrder.size();

        assertTrue(unitUnderTest.initializePlayersList(validPlayerOrder, amountOfPlayers));

        assertEquals(validPlayerOrder, unitUnderTest.getPlayersList());
    }

    @Test
    public void test10_assignSetupArmiesToPlayers_playerColorListIsEmpty_expectException() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
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
                    .addMockedMethod("getNumArmiesToPlace")
                    .createMock();
            // 40 is the MAX (2 players), account for that offset on the size
            // and lose 5 additional armies per extra player.
            int expectedNumArmies = 40 - ((numberOfPlayers - 2) * 5);
            mockedPlayer.setNumArmiesToPlace(expectedNumArmies);
            EasyMock.expectLastCall().once();
            EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(expectedNumArmies);
            EasyMock.replay(mockedPlayer);
            listOfPlayersToReturn.add(mockedPlayer);
        }
        return listOfPlayersToReturn;
    }

    @Test
    public void test11_assignSetupArmiesToPlayers_playerListSizeTwoAddNeutral_returnsTrueAndCorrectlyAssigns() {
        // employ partial mocks for this method to ensure the Player objects are utilized correctly.
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> twoPlayerList = List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.NEUTRAL);

        unitUnderTest.setPlayerOrderList(twoPlayerList);

        List<Player> playerMocks = createMockedPlayersList(twoPlayerList, 2); // "remove" neutral

        unitUnderTest.provideMockedPlayerObjects(playerMocks);

        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());
        int expectedNumArmies = 40;

        for (Player mockedPlayer : playerMocks) {
            assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(mockedPlayer.getColor()));
            EasyMock.verify(mockedPlayer);
        }
    }

    @Test
    public void test12_assignSetupArmiesToPlayers_playerListSizeThree_returnsTrueAndCorrectlyAssigns() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> threePlayerList = List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.PURPLE);

        unitUnderTest.setPlayerOrderList(threePlayerList);

        List<Player> playerMocks = createMockedPlayersList(threePlayerList, threePlayerList.size());

        unitUnderTest.provideMockedPlayerObjects(playerMocks);

        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());
        int expectedNumArmies = 35;

        for (Player mockedPlayer : playerMocks) {
            assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(mockedPlayer.getColor()));
            EasyMock.verify(mockedPlayer);
        }
    }

    @Test
    public void test13_assignSetupArmiesToPlayers_playerListSizeFour_returnsTrueAndCorrectlyAssigns() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> fourPlayerList = List.of(PlayerColor.RED, PlayerColor.YELLOW,
                PlayerColor.PURPLE, PlayerColor.GREEN);

        unitUnderTest.setPlayerOrderList(fourPlayerList);

        List<Player> playerMocks = createMockedPlayersList(fourPlayerList, fourPlayerList.size());

        unitUnderTest.provideMockedPlayerObjects(playerMocks);

        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());
        int expectedNumArmies = 30;

        for (Player mockedPlayer : playerMocks) {
            assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(mockedPlayer.getColor()));
            EasyMock.verify(mockedPlayer);
        }
    }

    @Test
    public void test14_assignSetupArmiesToPlayers_playerListSizeFive_returnsTrueAndCorrectlyAssigns() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> fivePlayerList = List.of(PlayerColor.RED, PlayerColor.YELLOW,
                PlayerColor.PURPLE, PlayerColor.GREEN, PlayerColor.BLACK);

        unitUnderTest.setPlayerOrderList(fivePlayerList);

        List<Player> playerMocks = createMockedPlayersList(fivePlayerList, fivePlayerList.size());

        unitUnderTest.provideMockedPlayerObjects(playerMocks);

        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());
        int expectedNumArmies = 25;

        for (Player mockedPlayer : playerMocks) {
            assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(mockedPlayer.getColor()));
            EasyMock.verify(mockedPlayer);
        }
    }

    @Test
    public void test15_assignSetupArmiesToPlayers_playerListSizeSix_returnsTrueAndCorrectlyAssigns() {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> sixPlayerList = List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.PURPLE,
                PlayerColor.GREEN, PlayerColor.BLACK, PlayerColor.BLUE);

        unitUnderTest.setPlayerOrderList(sixPlayerList);

        List<Player> playerMocks = createMockedPlayersList(sixPlayerList, sixPlayerList.size());

        unitUnderTest.provideMockedPlayerObjects(playerMocks);

        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());
        int expectedNumArmies = 20;

        for (Player mockedPlayer : playerMocks) {
            assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(mockedPlayer.getColor()));
            EasyMock.verify(mockedPlayer);
        }
    }

    private static Stream<Arguments> generateAllNonDuplicatePlayerColorPairs() {
        Set<Set<PlayerColor>> colorPairsNoDuplicates = new HashSet<>();
        Set<PlayerColor> playerColors = new HashSet<>(Set.of(PlayerColor.values()));
        playerColors.remove(PlayerColor.SETUP);
        playerColors.remove(PlayerColor.NEUTRAL);

        for (PlayerColor firstPlayerColor : playerColors) {
            for (PlayerColor secondPlayerColor : playerColors) {
                if (firstPlayerColor != secondPlayerColor) {
                    Set<PlayerColor> playerPair = new HashSet<>();
                    playerPair.add(firstPlayerColor);
                    playerPair.add(secondPlayerColor);
                    colorPairsNoDuplicates.add(playerPair);
                }
            }
        }
        Set<Arguments> playerPairs = new HashSet<>();
        for (Set<PlayerColor> playerPair : colorPairsNoDuplicates) {
            Iterator<PlayerColor> iterator = playerPair.iterator();
            PlayerColor firstPlayer = iterator.next();
            PlayerColor secondPlayer = iterator.next();
            playerPairs.add(Arguments.of(firstPlayer, secondPlayer));
        }
        return playerPairs.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllNonDuplicatePlayerColorPairs")
    public void test16_assignSetupArmiesToPlayersIntegrationTest_noMockedPlayersList_expectTrueAndCorrectlyAssigns(
            PlayerColor firstPlayerColor, PlayerColor secondPlayerColor) {
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> twoPlayerList = List.of(firstPlayerColor, secondPlayerColor);

        // this test is meant to act as an "integration" test between initializePlayersList and
        // assignSetupArmiesToPlayers.
        assertTrue(unitUnderTest.initializePlayersList(twoPlayerList, twoPlayerList.size()));
        // if that worked, the mapping should now be set up.
        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());

        for (PlayerColor player : twoPlayerList) {
            assertEquals(40, unitUnderTest.getNumArmiesByPlayerColor(player));
        } // check for neutral too.
        assertEquals(40, unitUnderTest.getNumArmiesByPlayerColor(PlayerColor.NEUTRAL));
    }

    private static Stream<Arguments> generatePlayerLists_sizesThreeThroughSix() {
        Set<Arguments> arguments = new HashSet<>();
        Set<PlayerColor> validPlayers = new HashSet<>(Arrays.asList(PlayerColor.values()));
        validPlayers.remove(PlayerColor.NEUTRAL);
        validPlayers.remove(PlayerColor.SETUP);

        int size = validPlayers.size();
        for (PlayerColor player : new HashSet<>(validPlayers)) {
            if (size < 4) {
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
    @MethodSource("generatePlayerLists_sizesThreeThroughSix")
    public void test17_assignSetupArmiesToPlayersIntegrationTest_listSizeVaries_expectTrueAndCorrectlyAssigns(
            List<PlayerColor> playerColors) {
        GameEngine unitUnderTest = new WorldDominationGameEngine();

        assertTrue(unitUnderTest.initializePlayersList(playerColors, playerColors.size()));
        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());

        int expectedNumArmies = 40 - ((playerColors.size() - 2) * 5);
        for (PlayerColor player : playerColors) {
            assertEquals(expectedNumArmies, unitUnderTest.getNumArmiesByPlayerColor(player));
        }
    }

    private Territory createMockedTerritoryWithExpectations(PlayerColor playerColorToReturn) {
        Territory mockedTerritory = EasyMock.createMock(Territory.class);

        EasyMock.expect(mockedTerritory.getPlayerInControl()).andReturn(playerColorToReturn);

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

    @ParameterizedTest
    @EnumSource(TerritoryType.class)
    public void test21_checkIfPlayerOwnsTerritory_setupPlayerOwnsTerritory_inputIsBluePlayer_expectFalse(
            TerritoryType relevantTerritory) {
        Territory mockedTerritory = createMockedTerritoryWithExpectations(PlayerColor.SETUP);
        TerritoryGraph mockedGraph = createMockedGraphWithExpectations(relevantTerritory, mockedTerritory, 1);

        GameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertFalse(unitUnderTest.checkIfPlayerOwnsTerritory(relevantTerritory, PlayerColor.BLUE));

        EasyMock.verify(mockedGraph, mockedTerritory);
    }

    @ParameterizedTest
    @EnumSource(TerritoryType.class)
    public void test22_checkIfPlayerOwnsTerritory_nonSetupPlayerOwnsTerritory_inputIsBluePlayer_expectFalse(
            TerritoryType relevantTerritory) {
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);
        Territory mockedTerritory = EasyMock.createMock(Territory.class);

        EasyMock.expect(mockedGraph.getTerritory(relevantTerritory)).andReturn(mockedTerritory);
        EasyMock.expect(mockedTerritory.getPlayerInControl()).andReturn(PlayerColor.RED);

        EasyMock.replay(mockedGraph, mockedTerritory);

        GameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        assertFalse(unitUnderTest.checkIfPlayerOwnsTerritory(relevantTerritory, PlayerColor.BLUE));

        EasyMock.verify(mockedGraph, mockedTerritory);
    }

    private static Stream<Arguments> generateAllTerritoryTypeAndPlayerMinusSetupCombinations() {
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
    @MethodSource("generateAllTerritoryTypeAndPlayerMinusSetupCombinations")
    public void test23_placeNewArmiesInTerritory_territoryAlreadyClaimedByCurrentPlayerInScramble_expectException(
            TerritoryType relevantTerritory, PlayerColor playerInControl) {
        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);

        EasyMock.expect(mockedGraph.getTerritory(relevantTerritory)).andReturn(mockedTerritory);
        EasyMock.expect(mockedTerritory.getPlayerInControl()).andReturn(playerInControl);

        EasyMock.replay(mockedGraph, mockedTerritory);

        GameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        int numArmiesToPlace = 10;
        String expectedMessage = "Cannot place armies in a claimed territory until the scramble phase is over";
        Exception exception = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, numArmiesToPlace));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedTerritory, mockedGraph);
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
    public void test24_placeNewArmiesInTerritory_moreThanOneArmyInScramblePhase_expectException(
            TerritoryType relevantTerritory, int illegalArmyAmount) {
        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);

        EasyMock.expect(mockedGraph.getTerritory(relevantTerritory)).andReturn(mockedTerritory);
        EasyMock.expect(mockedTerritory.getPlayerInControl()).andReturn(PlayerColor.SETUP);

        EasyMock.replay(mockedGraph, mockedTerritory);

        GameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);

        String expectedMessage = "You can only place 1 army on an unclaimed territory until the scramble phase is over";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, illegalArmyAmount));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        EasyMock.verify(mockedTerritory, mockedGraph);
    }

    @ParameterizedTest
    @MethodSource("generateAllTerritoryTypeAndPlayerMinusSetupCombinations")
    public void test25_placeNewArmiesInTerritory_scramblePhaseValidInput_expectTrueAndTerritoryGetsPlayerColorAndArmies(
            TerritoryType relevantTerritory, PlayerColor currentlyGoingPlayer) {
        Territory mockedTerritory = EasyMock.createMock(Territory.class);
        TerritoryGraph mockedGraph = EasyMock.createMock(TerritoryGraph.class);

        EasyMock.expect(mockedGraph.getTerritory(relevantTerritory)).andReturn(mockedTerritory).times(2);
        EasyMock.expect(mockedTerritory.getPlayerInControl()).andReturn(PlayerColor.SETUP);

        int validNumArmies = 1;
        // Check that we actually set the number of armies in the territory
        EasyMock.expect(mockedTerritory.setNumArmiesPresent(validNumArmies)).andReturn(true);
        // Check that the proper player gets put in control during the scramble phase
        EasyMock.expect(mockedTerritory.setPlayerInControl(currentlyGoingPlayer)).andReturn(true);

        EasyMock.replay(mockedGraph, mockedTerritory);

        GameEngine unitUnderTest = new WorldDominationGameEngine();
        unitUnderTest.provideMockedTerritoryGraph(mockedGraph);
        unitUnderTest.provideCurrentPlayerForTurn(currentlyGoingPlayer);

        assertTrue(unitUnderTest.placeNewArmiesInTerritory(relevantTerritory, validNumArmies));

        EasyMock.verify(mockedTerritory, mockedGraph);
    }

}
