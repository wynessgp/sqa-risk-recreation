package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TerritoryCardTest {
    private static Stream<Arguments> territoryGenerator() {
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        return territories.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test00_matchesTerritory_checkAllTrueInputs(TerritoryType territory) {
        TerritoryCard card = new TerritoryCard(territory, ArmyType.INFANTRY);
        assertTrue(card.matchesTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test01_matchesTerritory_checkAllFalseInputs(TerritoryType territoryOnCard) {
        TerritoryCard card = new TerritoryCard(territoryOnCard, ArmyType.INFANTRY);
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        for (TerritoryType territoryToCheck : territories) {
            if (territoryToCheck != territoryOnCard) {
                assertFalse(card.matchesTerritory(territoryToCheck));
            }
        }
    }

    private static Stream<Arguments> pieceGenerator() {
        Set<ArmyType> armies = Set.of(ArmyType.values());
        return armies.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("pieceGenerator")
    public void test03_matchesPieceType_checkAllTrueInputs(ArmyType piece) {
        TerritoryCard card = new TerritoryCard(TerritoryType.Alaska, piece);
        assertTrue(card.matchesPieceType(piece));
    }

    @ParameterizedTest
    @MethodSource("pieceGenerator")
    public void test04_matchesPieceType_checkAllFalseInputs(ArmyType pieceOnCard) {
        TerritoryCard card = new TerritoryCard(TerritoryType.Alaska, pieceOnCard);
        Set<ArmyType> pieces = Set.of(ArmyType.values());
        for (ArmyType pieceToCheck : pieces) {
            if (pieceToCheck != pieceOnCard) {
                assertFalse(card.matchesPieceType(pieceToCheck));
            }
        }
    }

    @Test
    public void test05_isWild_returnsFalse() {
        TerritoryCard card = new TerritoryCard(TerritoryType.Alaska, ArmyType.INFANTRY);
        assertFalse(card.isWild());
    }
}
