package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashSet;
import java.util.Set;

public class TradeInManagerTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 4})
    public void test00_startTrade_withWrongSizeCollection_throwsException(int size) {
        Set<Card> cards = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Card card = EasyMock.createMock(Card.class);
            cards.add(card);
            EasyMock.replay(card);
        }

        TradeInManager tradeIn = new TradeInManager();
        String expected = "Must trade in exactly three cards";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> tradeIn.startTrade(cards));
        assertEquals(expected, exception.getMessage());

        for (Card card : cards) {
            EasyMock.verify(card);
        }
    }
}
