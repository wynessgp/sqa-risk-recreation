package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DieRollParserTest {

    @Test
    public void test00_buildDiceLists_expectReturnsTrue() {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();

        // perform the operation
        assertTrue(unitUnderTest.buildDiceLists());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 4})
    public void test01_rollAttackerDice_tooFewOrTooManyDice_expectException(int illegalInput) {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();

        // preliminary operation:
        assertTrue(unitUnderTest.buildDiceLists());

        // perform the operation
        String expectedMessage = "Valid amount of dice is in the range [1, 3]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.rollAttackerDice(illegalInput));

        // assert
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
