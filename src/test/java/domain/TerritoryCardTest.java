package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TerritoryCardTest {
    private static Stream<Arguments> territoryGenerator() {
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        return territories.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test00_matchesTerritory_checkAllTrueInputs(TerritoryType territory) {
        Card card = new TerritoryCard(territory, PieceType.INFANTRY);
        assertTrue(card.matchesTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test01_matchesTerritory_checkAllFalseInputs(TerritoryType territoryOnCard) {
        Card card = new TerritoryCard(territoryOnCard, PieceType.INFANTRY);
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        for (TerritoryType territoryToCheck : territories) {
            if (territoryToCheck != territoryOnCard) {
                assertFalse(card.matchesTerritory(territoryToCheck));
            }
        }
    }

    private static Stream<Arguments> pieceGenerator() {
        Set<PieceType> armies = Set.of(PieceType.values());
        return armies.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("pieceGenerator")
    public void test03_matchesPieceType_checkAllTrueInputs(PieceType piece) {
        Card card = new TerritoryCard(TerritoryType.ALASKA, piece);
        assertTrue(card.matchesPieceType(piece));
    }

    @ParameterizedTest
    @MethodSource("pieceGenerator")
    public void test04_matchesPieceType_checkAllFalseInputs(PieceType pieceOnCard) {
        Card card = new TerritoryCard(TerritoryType.ALASKA, pieceOnCard);
        Set<PieceType> pieces = Set.of(PieceType.values());
        for (PieceType pieceToCheck : pieces) {
            if (pieceToCheck != pieceOnCard) {
                assertFalse(card.matchesPieceType(pieceToCheck));
            }
        }
    }

    @Test
    public void test05_isWild_returnsFalse() {
        Card card = new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY);
        assertFalse(card.isWild());
    }
}
