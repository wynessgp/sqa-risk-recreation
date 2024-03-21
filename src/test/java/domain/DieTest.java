package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.easymock.EasyMock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DieTest {
    
    @Test
    public void test00_nullPointerRandomizer_expectNullPointerException() {
        // variable setup
        Die unitUnderTest = new Die();

        // perform the operation
        String expectedMessage = "Randomizer object is null, cannot roll Die!";
        Exception exception = assertThrows(NullPointerException.class,
                                           () -> unitUnderTest.rollSingleDie(null));
        
        // assert
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 6})
    public void test01_validRandomPointer_expectBVASuggestedValues(int forcedRollForMock) {
        // variable setup: note that JDK11 doesn't have the lower bounded nextInt,
        // so we have to offset the max and then re-add in the minimum later.
        int maximumPossibleRoll = 6;
        int minimumPossibleRoll = 1;

        // Record
        Random random = EasyMock.mock(Random.class);
        // this line is a bit weird for the 6 case, as we'll be limited to 5 as the max in rand.nextInt, 
        // but we'll expect it to return 6 (since the die is technically capable of it)
        // Note that we also need to return forcedRoll - 1, as we add the lower bound due to JDK11's restrictions.
        EasyMock.expect(random.nextInt((maximumPossibleRoll - minimumPossibleRoll) + 1))
                .andReturn(forcedRollForMock - 1);

        // Replay
        EasyMock.replay(random);

        // Regular JUnit testing code
        Die unitUnderTest = new Die(maximumPossibleRoll, minimumPossibleRoll);
        Integer actualResult = unitUnderTest.rollSingleDie(random);

        // Verify
        EasyMock.verify(random);

        // Assert like normal
        assertEquals(forcedRollForMock, actualResult);
    }
}
