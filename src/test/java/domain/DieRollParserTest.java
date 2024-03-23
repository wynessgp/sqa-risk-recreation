package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest
    @CsvSource({"1, 0", "2, 1"})
    public void test02_rollAttackerDice_notEnoughDiceInCollection_expectException(
            int illegalInput, int numDiceToInitialize) {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();

        // preliminary op:
        assertTrue(unitUnderTest.buildDiceLists(numDiceToInitialize));

        // perform the operation
        String expectedMessage = "Not enough dice to fulfill requested amount to roll";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.rollAttackerDice(illegalInput));

        // assert
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test03_rollAttackerDice_rollThreeDice_expectSortedListOfSizeThree() {
        // variable setup
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        Die secondDie = EasyMock.mock(Die.class);
        Die thirdDie = EasyMock.mock(Die.class);

        // record - note these are in UNSORTED order!
        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(1);
        EasyMock.expect(secondDie.rollSingleDie(random)).andReturn(3);
        EasyMock.expect(thirdDie.rollSingleDie(random)).andReturn(2);

        // provide the underlying dice collection to our class.
        DieRollParser unitUnderTest = new DieRollParser(random,
                List.of(firstDie, secondDie, thirdDie));

        // replay
        EasyMock.replay(firstDie, secondDie, thirdDie);

        // do regular JUnit test calculations
        List<Integer> actual = unitUnderTest.rollAttackerDice(3);

        // verify
        EasyMock.verify(firstDie, secondDie, thirdDie);

        // do regular JUnit assertions
        assertEquals(3, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
        assertEquals(List.of(3, 2, 1), actual);
    }

    private boolean isSortedInNonIncreasingOrder(List<Integer> listInQuestion) {
        for (int i = 0; i < listInQuestion.size() - 1; i++) {
            if (listInQuestion.get(i) < listInQuestion.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
