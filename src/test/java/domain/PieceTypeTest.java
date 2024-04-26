package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PieceTypeTest {

    private static Stream<Arguments> pieceTypeGenerator() {
        return Stream.of(
                Arguments.of(PieceType.INFANTRY, "Infantry"),
                Arguments.of(PieceType.CAVALRY, "Cavalry"),
                Arguments.of(PieceType.ARTILLERY, "Artillery")
        );
    }

    @ParameterizedTest
    @MethodSource("pieceTypeGenerator")
    public void test00_toString_withValidResult_returnsProperString(PieceType piece, String expected) {
        String result = piece.toString();
        assertEquals(expected, result);
    }

}
