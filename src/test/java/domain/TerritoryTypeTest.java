package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TerritoryTypeTest {

    @Test
    public void test00_toString_withValidResult_returnsProperString() {
        TerritoryType territory = TerritoryType.ALASKA;
        String expected = "Alaska";
        String result = territory.toString();
        assertEquals(expected, result);
    }

}
