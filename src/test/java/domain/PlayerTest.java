package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PlayerTest {

    private static Stream<Arguments> territoryTypeGenerator() {
        return Stream.of(Arrays.stream(TerritoryType.values()).map(Arguments::of).toArray(Arguments[]::new));
    }

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test00_ownsTerritory_withNoneOwned_returnsFalse(TerritoryType territory) {
        Player player = new Player();
        assertFalse(player.ownsTerritory(territory));
    }

}
