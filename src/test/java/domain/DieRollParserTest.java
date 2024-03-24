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

    private boolean isSortedInNonIncreasingOrder(List<Integer> listInQuestion) {
        for (int i = 0; i < listInQuestion.size() - 1; i++) {
            if (listInQuestion.get(i) < listInQuestion.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

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

    @Test
    public void test02_rollAttackerDice_rollThreeDice_expectSortedListOfSizeThree() {
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
                List.of(firstDie, secondDie, thirdDie), null);

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

    @Test
    public void test03_rollAttackerDice_rollTwoDice_expectSortedListOfSizeTwo() {
        // variable setup
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        Die secondDie = EasyMock.mock(Die.class);

        // record
        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(4);
        EasyMock.expect(secondDie.rollSingleDie(random)).andReturn(4);

        // provide the underlying dice collection to our class.
        DieRollParser unitUnderTest = new DieRollParser(random,
                List.of(firstDie, secondDie), null);

        // replay
        EasyMock.replay(firstDie, secondDie);

        // do regular JUnit test calculations
        List<Integer> actual = unitUnderTest.rollAttackerDice(2);

        // verify
        EasyMock.verify(firstDie, secondDie);

        // do regular JUnit assertions
        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
        assertEquals(List.of(4, 4), actual);
    }

    @Test
    public void test04_rollAttackerDice_rollOneDie_expectListOfSizeOne() {
        // variable setup
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);

        // record
        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(3);

        // provide the underlying dice collection to our class.
        DieRollParser unitUnderTest = new DieRollParser(random, List.of(firstDie), null);

        // replay
        EasyMock.replay(firstDie);

        // do regular JUnit test calculations
        List<Integer> actual = unitUnderTest.rollAttackerDice(1);

        // verify
        EasyMock.verify(firstDie);

        // do regular JUnit assertions
        assertEquals(1, actual.size());
        assertEquals(List.of(3), actual);
    }

    @Test
    public void test05_rollAttackerDice_rollTwoUnsetValueDice_expectSortedListOfSizeTwo() {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();

        // ok, create the dice lists.
        assertTrue(unitUnderTest.buildDiceLists());

        // now attempt to roll the dice to ensure the UUT did its job
        List<Integer> actual = unitUnderTest.rollAttackerDice(2);
        // we don't care what the actual numbers are in the output; just that it's sorted.
        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 3})
    public void test06_rollDefenderDice_tooFewOrTooManyDice_expectException(int illegalInput) {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();

        // preliminary operation:
        assertTrue(unitUnderTest.buildDiceLists());

        // perform the operation
        String expectedMessage = "Valid amount of dice is in the range [1, 2]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.rollDefenderDice(illegalInput));

        // assert
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test07_rollDefenderDice_rollTwoDice_expectSortedListOfSizeTwo() {
        // variable setup
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        Die secondDie = EasyMock.mock(Die.class);

        // record - note these are not in sorted order!
        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(2);
        EasyMock.expect(secondDie.rollSingleDie(random)).andReturn(3);

        // provide the underlying dice collection to our class.
        DieRollParser unitUnderTest = new DieRollParser(random,
                null, List.of(firstDie, secondDie));

        // replay
        EasyMock.replay(firstDie, secondDie);

        // do regular JUnit test calculations
        List<Integer> actual = unitUnderTest.rollDefenderDice(2);

        // verify
        EasyMock.verify(firstDie, secondDie);

        // do regular JUnit assertions
        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
        assertEquals(List.of(3, 2), actual);
    }

    @Test
    public void test08_rollDefenderDice_rollTwoDiceIdenticalValues_expectSortedListOfSizeTwo() {
        // variable setup
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        Die secondDie = EasyMock.mock(Die.class);

        // record - note these are not in sorted order!
        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(4);
        EasyMock.expect(secondDie.rollSingleDie(random)).andReturn(4);

        // provide the underlying dice collection to our class.
        DieRollParser unitUnderTest = new DieRollParser(random,
                null, List.of(firstDie, secondDie));

        // replay
        EasyMock.replay(firstDie, secondDie);

        // do regular JUnit test calculations
        List<Integer> actual = unitUnderTest.rollDefenderDice(2);

        // verify
        EasyMock.verify(firstDie, secondDie);

        // do regular JUnit assertions
        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
        assertEquals(List.of(4, 4), actual);
    }

    @Test
    public void test09_rollDefenderDice_rollOneDie_expectListOfSizeOne() {
        // variable setup
        Random random = new Random();
        Die firstDie = EasyMock.mock(Die.class);

        // record
        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(3);

        // provide the underlying dice collection to our class.
        DieRollParser unitUnderTest = new DieRollParser(random,
                null, List.of(firstDie));

        // replay
        EasyMock.replay(firstDie);

        // do regular JUnit test calculations
        List<Integer> actual = unitUnderTest.rollDefenderDice(1);

        // verify
        EasyMock.verify(firstDie);

        // do regular JUnit assertions
        assertEquals(1, actual.size());
        assertEquals(List.of(3), actual);
    }

    @Test
    public void test10_rollDefenderDice_rollTwoUnsetValueDice_expectSortedListOfSizeTwo() {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();

        // ok, create the dice lists.
        assertTrue(unitUnderTest.buildDiceLists());

        // now attempt to roll the dice to ensure the UUT did its job
        List<Integer> actual = unitUnderTest.rollDefenderDice(2);
        // we don't care what the actual numbers are in the output; just that it's sorted.
        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
    }

    @Test
    public void test11_validateSortIsInNonIncreasingOrder_listOfSizeOne_expectTrue() {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> list = List.of(1);

        // assert statement
        assertTrue(unitUnderTest.validateSortIsInNonIncreasingOrder(list));
    }

    @ParameterizedTest
    @CsvSource({"4, 2, true", "3, 3, true", "2, 4, false"})
    public void test12_validateSortIsInNonIncreasingOrder_listOfSizeTwo_resultVaries(
            int listElementOne, int listElementTwo, boolean expectedResult) {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> list = List.of(listElementOne, listElementTwo);

        // assert statement
        assertEquals(expectedResult, unitUnderTest.validateSortIsInNonIncreasingOrder(list));
    }

    @ParameterizedTest
    @CsvSource({"6, 5, 1, true", "1, 3, 5, false"})
    public void test13_validateSortIsInNonIncreasingOrder_listOfSizeThree_resultVaries(
            int listElementOne, int listElementTwo,
            int listElementThree, boolean expectedResult) {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> list = List.of(listElementOne, listElementTwo, listElementThree);

        // assert statement
        assertEquals(expectedResult, unitUnderTest.validateSortIsInNonIncreasingOrder(list));
    }

    @Test
    public void test14_generateBattleResults_emptyAttackerRolls_expectException() {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(5);
        List<Integer> attackerRolls = List.of();

        // perform the operation
        String expectedMessage = "Both arguments must have at least one element";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.generateBattleResults(defenderRolls, attackerRolls));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test15_generateBattleResults_emptyDefenderRolls_expectException() {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of();
        List<Integer> attackerRolls = List.of(5);

        // perform the operation
        String expectedMessage = "Both arguments must have at least one element";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.generateBattleResults(defenderRolls, attackerRolls));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test16_generateBattleResults_bothInputsEmpty_expectException() {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of();
        List<Integer> attackerRolls = List.of();

        // perform the operation
        String expectedMessage = "Both arguments must have at least one element";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.generateBattleResults(defenderRolls, attackerRolls));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test17_generateBattleResults_defenderRollsNotSorted_expectException() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(1, 2);
        List<Integer> attackerRolls = List.of(5);

        // perform the operation
        String expectedMessage = "defenderRolls are not sorted in non-increasing order";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.generateBattleResults(defenderRolls, attackerRolls));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test18_generateBattleResults_attackerRollsNotSorted_expectException() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(5);
        List<Integer> attackerRolls = List.of(1, 2);

        // perform the operation
        String expectedMessage = "attackerRolls are not sorted in non-increasing order";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.generateBattleResults(defenderRolls, attackerRolls));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test19_generateBattleResults_rolledSameValueOnce_expectOneDefenderVictory() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(5);
        List<Integer> attackerRolls = List.of(5);

        // perform the operation
        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        // assertions
        assertEquals(1, actual.size());
        assertEquals(List.of(BattleResult.DEFENDER_VICTORY), actual);
    }

    @Test
    public void test20_generateBattleResults_twoDefenderDice_expectOneDefenderVictory() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(6, 6);
        List<Integer> attackerRolls = List.of(1);

        // perform the operation
        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        // assertions
        assertEquals(1, actual.size());
        assertEquals(List.of(BattleResult.DEFENDER_VICTORY), actual);
    }

    @Test
    public void test21_generateBattleResults_defenderWinsTwice_expectTwoDefenderVictories() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(6, 6);
        List<Integer> attackerRolls = List.of(5, 3, 2);

        // perform the operation
        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        // assertions
        assertEquals(2, actual.size());
        assertEquals(List.of(BattleResult.DEFENDER_VICTORY, BattleResult.DEFENDER_VICTORY),
                actual);
    }

    @Test
    public void test22_generateBattleResults_oneDefenderOneAttackerVictory_expectBothTypes() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(6, 2);
        List<Integer> attackerRolls = List.of(4, 4, 3);

        // perform the operation
        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        // assertions
        assertEquals(2, actual.size());
        assertEquals(List.of(BattleResult.DEFENDER_VICTORY, BattleResult.ATTACKER_VICTORY),
                actual);
    }

    @Test
    public void test23_generateBattleResults_attackerWinsOnce_expectOneAttackerVictory() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(3);
        List<Integer> attackerRolls = List.of(4);

        // perform the operation
        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        // assertions
        assertEquals(1, actual.size());
        assertEquals(List.of(BattleResult.ATTACKER_VICTORY), actual);
    }

    @Test
    public void test24_generateBattleResults_attackerWinsTwice_expectTwoAttackerVictories() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(3, 2);
        List<Integer> attackerRolls = List.of(5, 4);

        // perform the operation
        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        // assertions
        assertEquals(2, actual.size());
        assertEquals(List.of(BattleResult.ATTACKER_VICTORY, BattleResult.ATTACKER_VICTORY),
                actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 7})
    public void test25_rollDiceToDeterminePlayerOrder_invalidInput_expectException(
            int illegalInput) {
        DieRollParser unitUnderTest = new DieRollParser();

        // perform the operation
        String expectedMessage = "Valid amount of dice is in the range [2, 6]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.rollDiceToDeterminePlayerOrder(illegalInput));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }


}
