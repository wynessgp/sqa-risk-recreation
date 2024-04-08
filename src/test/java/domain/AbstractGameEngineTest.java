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

    @Test
    public void test11_assignSetupArmiesToPlayers_playerListSizeTwoAddNeutral_returnsTrueAndCorrectlyAssigns() {
        // employ partial mocks for this method to ensure the Player objects are utilized correctly.
        GameEngine unitUnderTest = new WorldDominationGameEngine();
        List<PlayerColor> twoPlayerList = List.of(PlayerColor.RED, PlayerColor.YELLOW, PlayerColor.NEUTRAL);
        List<Player> mockedPlayers = new ArrayList<>();

        unitUnderTest.setPlayerOrderList(twoPlayerList);

        for (PlayerColor playerColor : twoPlayerList) {
            Player mockedPlayer = EasyMock.partialMockBuilder(Player.class)
                    .withConstructor(PlayerColor.class)
                    .withArgs(playerColor)
                    .addMockedMethod("setNumArmiesToPlace")
                    .addMockedMethod("getNumArmiesToPlace")
                    .createMock();
            mockedPlayer.setNumArmiesToPlace(40);
            EasyMock.expectLastCall().once();
            EasyMock.expect(mockedPlayer.getNumArmiesToPlace()).andReturn(40);
            EasyMock.replay(mockedPlayer);
            mockedPlayers.add(mockedPlayer);
        }

        unitUnderTest.provideMockedPlayerObjects(mockedPlayers);

        assertTrue(unitUnderTest.assignSetupArmiesToPlayers());

        for (Player mockedPlayer : mockedPlayers) {
            assertEquals(40, unitUnderTest.getNumArmiesByPlayerColor(mockedPlayer.getColor()));
            EasyMock.verify(mockedPlayer);
        }
    }

}
