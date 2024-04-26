package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PieceTypeTest {

    @Test
    public void test00_toString_withValidResult_returnsProperString() {
        PieceType piece = PieceType.INFANTRY;
        String expected = "Infantry";
        String result = piece.toString();
        assertEquals(expected, result);
    }

}
