package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class DieTest {
    
    @Test
    public void test00_nullPointerRandomizer_expectNullPointerException() {
        // variable setup
        Die unitUnderTest = new Die();

        // perform the operation
        String expectedMessage = "Randomizer object is null, cannot roll Die!";
        Exception exception = assertThrows(NullPointerException.class,
                                           () -> {unitUnderTest.rollSingleDie(null);} );
        
        // assert
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
