package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DieRollParserTest {

    @Test
    public void test00_buildDiceLists_expectReturnsTrue() {
        // variable setup
        DieRollParser unitUnderTest = new DieRollParser();

        // perform the operation
        assertTrue(unitUnderTest.buildDiceLists());
    }
}
