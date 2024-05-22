package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 4})
    public void test01_rollAttackerDice_tooFewOrTooManyDice_expectException(int illegalInput) {
        DieRollParser unitUnderTest = new DieRollParser();

        String expectedMessage = "Valid amount of dice for attacker roll must be in the range [1, 3]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.rollAttackerDice(illegalInput));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test02_rollAttackerDice_rollThreeDice_expectSortedListOfSizeThree() {
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        Die secondDie = EasyMock.mock(Die.class);
        Die thirdDie = EasyMock.mock(Die.class);

        // put in unsorted order on purpose, to ensure the result actually sorts them.
        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(1);
        EasyMock.expect(secondDie.rollSingleDie(random)).andReturn(3);
        EasyMock.expect(thirdDie.rollSingleDie(random)).andReturn(2);

        DieRollParser unitUnderTest = new DieRollParser(random,
                List.of(firstDie, secondDie, thirdDie), List.of());

        EasyMock.replay(firstDie, secondDie, thirdDie);

        List<Integer> actual = unitUnderTest.rollAttackerDice(3);

        EasyMock.verify(firstDie, secondDie, thirdDie);

        assertEquals(3, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
        assertEquals(List.of(3, 2, 1), actual);
    }

    @Test
    public void test03_rollAttackerDice_rollTwoDice_expectSortedListOfSizeTwo() {
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        Die secondDie = EasyMock.mock(Die.class);

        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(4);
        EasyMock.expect(secondDie.rollSingleDie(random)).andReturn(4);

        DieRollParser unitUnderTest = new DieRollParser(random,
                List.of(firstDie, secondDie), List.of());

        EasyMock.replay(firstDie, secondDie);

        List<Integer> actual = unitUnderTest.rollAttackerDice(2);

        EasyMock.verify(firstDie, secondDie);

        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
        assertEquals(List.of(4, 4), actual);
    }

    @Test
    public void test04_rollAttackerDice_rollOneDie_expectListOfSizeOne() {
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(3);

        DieRollParser unitUnderTest = new DieRollParser(random, List.of(firstDie), List.of());

        EasyMock.replay(firstDie);

        List<Integer> actual = unitUnderTest.rollAttackerDice(1);

        EasyMock.verify(firstDie);

        assertEquals(1, actual.size());
        assertEquals(List.of(3), actual);
    }

    @Test
    public void test05_rollAttackerDice_rollTwoUnsetValueDice_expectSortedListOfSizeTwo() {
        DieRollParser unitUnderTest = new DieRollParser();

        List<Integer> actual = unitUnderTest.rollAttackerDice(2);

        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 3})
    public void test06_rollDefenderDice_tooFewOrTooManyDice_expectException(int illegalInput) {
        DieRollParser unitUnderTest = new DieRollParser();

        String expectedMessage = "Valid amount of dice for defender roll must be in the range [1, 2]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.rollDefenderDice(illegalInput));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test07_rollDefenderDice_rollTwoDice_expectSortedListOfSizeTwo() {
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        Die secondDie = EasyMock.mock(Die.class);

        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(2);
        EasyMock.expect(secondDie.rollSingleDie(random)).andReturn(3);

        DieRollParser unitUnderTest = new DieRollParser(random,
                List.of(), List.of(firstDie, secondDie));

        EasyMock.replay(firstDie, secondDie);

        List<Integer> actual = unitUnderTest.rollDefenderDice(2);

        EasyMock.verify(firstDie, secondDie);

        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
        assertEquals(List.of(3, 2), actual);
    }

    @Test
    public void test08_rollDefenderDice_rollTwoDiceIdenticalValues_expectSortedListOfSizeTwo() {
        Random random = new Random();

        Die firstDie = EasyMock.mock(Die.class);
        Die secondDie = EasyMock.mock(Die.class);

        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(4);
        EasyMock.expect(secondDie.rollSingleDie(random)).andReturn(4);

        DieRollParser unitUnderTest = new DieRollParser(random,
                List.of(), List.of(firstDie, secondDie));

        EasyMock.replay(firstDie, secondDie);

        List<Integer> actual = unitUnderTest.rollDefenderDice(2);

        EasyMock.verify(firstDie, secondDie);

        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
        assertEquals(List.of(4, 4), actual);
    }

    @Test
    public void test09_rollDefenderDice_rollOneDie_expectListOfSizeOne() {
        Random random = new Random();
        Die firstDie = EasyMock.mock(Die.class);

        EasyMock.expect(firstDie.rollSingleDie(random)).andReturn(3);

        DieRollParser unitUnderTest = new DieRollParser(random,
                List.of(), List.of(firstDie));

        EasyMock.replay(firstDie);

        List<Integer> actual = unitUnderTest.rollDefenderDice(1);

        EasyMock.verify(firstDie);

        assertEquals(1, actual.size());
        assertEquals(List.of(3), actual);
    }

    @Test
    public void test10_rollDefenderDice_rollTwoUnsetValueDice_expectSortedListOfSizeTwo() {
        DieRollParser unitUnderTest = new DieRollParser();

        List<Integer> actual = unitUnderTest.rollDefenderDice(2);

        assertEquals(2, actual.size());
        assertTrue(isSortedInNonIncreasingOrder(actual));
    }

    @Test
    public void test11_validateSortIsInNonIncreasingOrder_listOfSizeOne_expectTrue() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> list = List.of(1);

        assertTrue(unitUnderTest.validateSortIsInNonIncreasingOrder(list));
    }

    @ParameterizedTest
    @CsvSource({"4, 2, true", "3, 3, true", "2, 4, false"})
    public void test12_validateSortIsInNonIncreasingOrder_listOfSizeTwo_resultVaries(
            int listElementOne, int listElementTwo, boolean expectedResult) {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> list = List.of(listElementOne, listElementTwo);

        assertEquals(expectedResult, unitUnderTest.validateSortIsInNonIncreasingOrder(list));
    }

    @ParameterizedTest
    @CsvSource({"6, 5, 1, true", "1, 3, 5, false"})
    public void test13_validateSortIsInNonIncreasingOrder_listOfSizeThree_resultVaries(
            int listElementOne, int listElementTwo,
            int listElementThree, boolean expectedResult) {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> list = List.of(listElementOne, listElementTwo, listElementThree);

        assertEquals(expectedResult, unitUnderTest.validateSortIsInNonIncreasingOrder(list));
    }

    @Test
    public void test14_generateBattleResults_emptyAttackerRolls_expectException() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(5);
        List<Integer> attackerRolls = List.of();

        String expectedMessage = "Both arguments must have at least one element";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.generateBattleResults(defenderRolls, attackerRolls));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test15_generateBattleResults_emptyDefenderRolls_expectException() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of();
        List<Integer> attackerRolls = List.of(5);

        String expectedMessage = "Both arguments must have at least one element";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.generateBattleResults(defenderRolls, attackerRolls));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test16_generateBattleResults_bothInputsEmpty_expectException() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of();
        List<Integer> attackerRolls = List.of();

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

        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        assertEquals(1, actual.size());
        assertEquals(List.of(BattleResult.DEFENDER_VICTORY), actual);
    }

    @Test
    public void test20_generateBattleResults_twoDefenderDice_expectOneDefenderVictory() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(6, 6);
        List<Integer> attackerRolls = List.of(1);

        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        assertEquals(1, actual.size());
        assertEquals(List.of(BattleResult.DEFENDER_VICTORY), actual);
    }

    @Test
    public void test21_generateBattleResults_defenderWinsTwice_expectTwoDefenderVictories() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(6, 6);
        List<Integer> attackerRolls = List.of(5, 3, 2);

        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        assertEquals(2, actual.size());
        assertEquals(List.of(BattleResult.DEFENDER_VICTORY, BattleResult.DEFENDER_VICTORY),
                actual);
    }

    @Test
    public void test22_generateBattleResults_oneDefenderOneAttackerVictory_expectBothTypes() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(6, 2);
        List<Integer> attackerRolls = List.of(4, 4, 3);

        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        assertEquals(2, actual.size());
        assertEquals(List.of(BattleResult.DEFENDER_VICTORY, BattleResult.ATTACKER_VICTORY),
                actual);
    }

    @Test
    public void test23_generateBattleResults_attackerWinsOnce_expectOneAttackerVictory() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(3);
        List<Integer> attackerRolls = List.of(4);

        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        assertEquals(1, actual.size());
        assertEquals(List.of(BattleResult.ATTACKER_VICTORY), actual);
    }

    @Test
    public void test24_generateBattleResults_attackerWinsTwice_expectTwoAttackerVictories() {
        DieRollParser unitUnderTest = new DieRollParser();
        List<Integer> defenderRolls = List.of(3, 2);
        List<Integer> attackerRolls = List.of(5, 4);

        List<BattleResult> actual = unitUnderTest.generateBattleResults(
                defenderRolls, attackerRolls);

        assertEquals(2, actual.size());
        assertEquals(List.of(BattleResult.ATTACKER_VICTORY, BattleResult.ATTACKER_VICTORY),
                actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 7, 2})
    public void test25_rollDiceToDeterminePlayerOrder_invalidInput_expectException(
            int illegalInput) {
        DieRollParser unitUnderTest = new DieRollParser();

        String expectedMessage = "Valid amount of dice for player setup roll must be in the range [3, 6]";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.rollDiceToDeterminePlayerOrder(illegalInput));

        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    private static Stream<Arguments> playerOrderGenerator() {
        return Stream.of(
                Arguments.of(List.of(1, 2, 6)),
                Arguments.of(List.of(2, 6, 4, 1)),
                Arguments.of(List.of(5, 4, 3, 2, 6)),
                Arguments.of(List.of(6, 1, 2, 5, 4, 3))
        );
    }

    @ParameterizedTest
    @MethodSource("playerOrderGenerator")
    public void test26_rollDiceToDeterminePlayerOrder_expectNoDuplicates(
            List<Integer> expectedRolls) {
        Die setupDie = EasyMock.mock(Die.class);
        for (int i : expectedRolls) {
            EasyMock.expect(setupDie.rollSingleDie(EasyMock.anyObject(Random.class))).andReturn(i);
        }
        EasyMock.replay(setupDie);

        DieRollParser unitUnderTest = new DieRollParser();
        unitUnderTest.setSetupDie(setupDie);

        // the main point of this one is to see, if given no "set" dice values, we can
        // get the method to return a list with no duplicates.
        List<Integer> actual = unitUnderTest.rollDiceToDeterminePlayerOrder(expectedRolls.size());

        assertEquals(expectedRolls.size(), actual.size());
        assertEquals(expectedRolls.size(), Set.copyOf(actual).size());
        assertEquals(expectedRolls, actual);
        EasyMock.verify(setupDie);
    }

    @Test
    public void test27_rollDiceToDeterminePlayerOrder_withDuplicateRoll_expectNoDuplicates() {
        List<Integer> expectedRolls = new ArrayList<>(List.of(1, 2, 3, 3, 4));
        Die setupDie = EasyMock.mock(Die.class);
        for (int i : expectedRolls) {
            EasyMock.expect(setupDie.rollSingleDie(EasyMock.anyObject(Random.class))).andReturn(i);
        }
        EasyMock.replay(setupDie);

        DieRollParser unitUnderTest = new DieRollParser();
        unitUnderTest.setSetupDie(setupDie);

        // the main point of this one is to see, if given no "set" dice values, we can
        // get the method to return a list with no duplicates.
        int numberOfPlayers = 4;
        List<Integer> actual = unitUnderTest.rollDiceToDeterminePlayerOrder(numberOfPlayers);

        expectedRolls.remove(3);
        assertEquals(numberOfPlayers, actual.size());
        assertEquals(expectedRolls, actual);
        EasyMock.verify(setupDie);
    }

}
