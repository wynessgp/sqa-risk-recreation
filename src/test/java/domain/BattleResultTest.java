package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BattleResultTest {

    @Test
    public void test00_toString_withValidResult_returnsProperString() {
        BattleResult battleResult = BattleResult.DEFENDER_VICTORY;
        String expected = "Defender victory";
        String result = battleResult.toString();
        assertEquals(expected, result);
    }
}
