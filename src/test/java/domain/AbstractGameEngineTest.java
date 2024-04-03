package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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


}
