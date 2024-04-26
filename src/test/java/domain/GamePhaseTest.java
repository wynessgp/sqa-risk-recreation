package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GamePhaseTest {

    @Test
    public void test00_toString_withValidResult_returnsProperString() {
        GamePhase phase = GamePhase.SETUP;
        String expected = "Setup";
        String result = phase.toString();
        assertEquals(expected, result);
    }
}
