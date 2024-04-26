package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PlayerColorTest {

    @Test
    public void test00_toString_withValidColor_returnsProperString() {
        PlayerColor color = PlayerColor.SETUP;
        String expected = "Setup";
        String result = color.toString();
        assertEquals(expected, result);
    }
}
