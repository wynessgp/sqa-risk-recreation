package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test01_ownsTerritory_withOneOwnedMatchesInput_returnsTrue(TerritoryType territory) {
        Player player = new Player();
        player.setTerritories(Set.of(territory));
        assertTrue(player.ownsTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test02_ownsTerritory_ownsAllButInput_returnsFalse(TerritoryType territory) {
        Player player = new Player();
        Set<TerritoryType> territories = new HashSet<>(Set.of(TerritoryType.values()));
        territories.remove(territory);
        player.setTerritories(territories);
        assertFalse(player.ownsTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test03_ownsTerritory_ownsAll_returnsTrue(TerritoryType territory) {
        Player player = new Player();
        player.setTerritories(Set.of(TerritoryType.values()));
        assertTrue(player.ownsTerritory(territory));
    }

    private static Stream<Arguments> territoryTypeCombinationsGenerator() {
        Set<Set<TerritoryType>> territoriesNoDuplicates = new HashSet<>();
        for (TerritoryType startingTerritory : TerritoryType.values()) {
            for (TerritoryType endingTerritory : TerritoryType.values()) {
                if (endingTerritory != startingTerritory) {
                    Set<TerritoryType> territoryPair = new HashSet<>();
                    territoryPair.add(startingTerritory);
                    territoryPair.add(endingTerritory);
                    territoriesNoDuplicates.add(territoryPair);
                }
            }
        }
        Set<Arguments> territoryArguments = new HashSet<>();
        for (Set<TerritoryType> territoryPair : territoriesNoDuplicates) {
            Iterator<TerritoryType> iterator = territoryPair.iterator();
            TerritoryType startingTerritoryType = iterator.next();
            TerritoryType endingTerritoryType = iterator.next();
            territoryArguments.add(Arguments.of(startingTerritoryType, endingTerritoryType));
        }
        return territoryArguments.stream();
    }

    @ParameterizedTest
    @MethodSource("territoryTypeCombinationsGenerator")
    public void test04_ownsTerritory_withOneMatchAndOneMismatch_returnsTrue(TerritoryType first, TerritoryType second) {
        Player player = new Player();
        player.setTerritories(Set.of(first, second));
        assertTrue(player.ownsTerritory(first));
    }

    private static Stream<Arguments> generateCardsForPlayerAndFalseInput() {
        WildCard wildCardOne = new WildCard();
        WildCard wildCardTwo = new WildCard();

        TerritoryCard alaskaCard = new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY);
        TerritoryCard brazilCard = new TerritoryCard(TerritoryType.BRAZIL, PieceType.ARTILLERY);

        Set<Card> allCards = new HashSet<>();
        allCards.add(wildCardOne);
        int pieceTypeCount = 0;
        for (TerritoryType territoryType : TerritoryType.values()) {
            if (territoryType == TerritoryType.ALASKA) {
                allCards.add(alaskaCard);
            } else if (territoryType == TerritoryType.BRAZIL) {
                allCards.add(brazilCard);
            } else {
                PieceType currentPiece = PieceType.values()[pieceTypeCount / 14];
                allCards.add(new TerritoryCard(territoryType, currentPiece));
                pieceTypeCount++;
            }
        }
        allCards.add(wildCardTwo);

        Set<Arguments> toStream = new HashSet<>();

        Set<Card> playerSet1 = Set.of();
        Set<Card> playerSet2 = Set.of(wildCardOne);
        Set<Card> playerSet3 = Set.of(wildCardOne, brazilCard);

        Set<Card> inputSet1 = Set.of(wildCardOne);
        Set<Card> inputSet2 = Set.of(alaskaCard);
        Set<Card> inputSet3 = Set.of(wildCardOne, alaskaCard, brazilCard);

        toStream.add(Arguments.of(playerSet1, inputSet1));
        toStream.add(Arguments.of(playerSet1, inputSet2));
        toStream.add(Arguments.of(playerSet2, inputSet2));
        toStream.add(Arguments.of(playerSet3, inputSet3));

        toStream.add(Arguments.of(playerSet1, allCards));
        toStream.add(Arguments.of(playerSet2, allCards));
        toStream.add(Arguments.of(playerSet3, allCards));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateCardsForPlayerAndFalseInput")
    public void test05_ownsAllGivenCards_playerDoesNotOwnAllGivenCards_returnsFalse(
            Set<Card> cardsPlayerOwns, Set<Card> cardsToSeeIfPlayerOwns) {
        Player player = new Player();
        player.setOwnedCards(cardsPlayerOwns);
        assertFalse(player.ownsAllGivenCards(cardsToSeeIfPlayerOwns));
    }

    private static Stream<Arguments> generateCardsForPlayerAndTrueInput() {
        WildCard wildCardOne = new WildCard();
        WildCard wildCardTwo = new WildCard();

        TerritoryCard congoCard = new TerritoryCard(TerritoryType.CONGO, PieceType.INFANTRY);
        TerritoryCard alaskaCard = new TerritoryCard(TerritoryType.ALASKA, PieceType.CAVALRY);

        Set<Card> allCards = new HashSet<>();
        allCards.add(wildCardOne);
        int pieceTypeCount = 0;
        for (TerritoryType territoryType : TerritoryType.values()) {
            if (territoryType == TerritoryType.CONGO) {
                allCards.add(congoCard);
            } else if (territoryType == TerritoryType.ALASKA) {
                allCards.add(alaskaCard);
            } else {
                PieceType currentPiece = PieceType.values()[pieceTypeCount / 14];
                allCards.add(new TerritoryCard(territoryType, currentPiece));
                pieceTypeCount++;
            }
        }
        allCards.add(wildCardTwo);

        Set<Arguments> toStream = new HashSet<>();

        Set<Card> playerSet1 = Set.of();
        Set<Card> playerSet2 = Set.of(wildCardOne);
        Set<Card> playerSet3 = Set.of(wildCardOne, congoCard, alaskaCard);

        Set<Card> inputSet1 = Set.of();
        Set<Card> inputSet2 = Set.of(wildCardOne);
        Set<Card> inputSet3 = Set.of(congoCard, alaskaCard);

        toStream.add(Arguments.of(playerSet1, inputSet1));
        toStream.add(Arguments.of(playerSet2, inputSet2));
        toStream.add(Arguments.of(playerSet3, inputSet3));

        toStream.add(Arguments.of(allCards, inputSet1));
        toStream.add(Arguments.of(allCards, inputSet2));
        toStream.add(Arguments.of(allCards, allCards));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateCardsForPlayerAndTrueInput")
    public void test06_ownsAllGivenCards_playerOwnsSpecifiedCards_returnsTrue(
            Set<Card> cardsPlayerOwns, Set<Card> cardsToSeeIfPlayerOwns) {
        Player player = new Player();
        player.setOwnedCards(cardsPlayerOwns);
        assertTrue(player.ownsAllGivenCards(cardsToSeeIfPlayerOwns));
    }

    private static Stream<Arguments> generateRemoveCardCombosThatResultInEmptySet() {
        Set<Arguments> toStream = new HashSet<>();

        WildCard wildCardOne = new WildCard();

        TerritoryCard alaskaCard = new TerritoryCard(TerritoryType.ALASKA, PieceType.INFANTRY);
        TerritoryCard brazilCard = new TerritoryCard(TerritoryType.BRAZIL, PieceType.ARTILLERY);

        toStream.add(Arguments.of(Set.of(), Set.of()));
        toStream.add(Arguments.of(Set.of(wildCardOne, alaskaCard), Set.of()));
        toStream.add(Arguments.of(Set.of(wildCardOne, brazilCard), Set.of(brazilCard)));

        Set<Card> allCards = new HashSet<>();
        allCards.add(wildCardOne);
        int pieceTypeCount = 0;
        for (TerritoryType territoryType : TerritoryType.values()) {
            if (territoryType == TerritoryType.ALASKA) {
                allCards.add(alaskaCard);
            } else if (territoryType == TerritoryType.BRAZIL) {
                allCards.add(brazilCard);
            } else {
                PieceType currentPiece = PieceType.values()[pieceTypeCount / 14];
                allCards.add(new TerritoryCard(territoryType, currentPiece));
                pieceTypeCount++;
            }
        }
        WildCard wildCardTwo = new WildCard();
        allCards.add(wildCardTwo);

        toStream.add(Arguments.of(allCards, Set.of()));
        toStream.add(Arguments.of(allCards, Set.of(wildCardOne)));
        toStream.add(Arguments.of(allCards, Set.of(wildCardOne, brazilCard)));
        toStream.add(Arguments.of(allCards, allCards));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateRemoveCardCombosThatResultInEmptySet")
    public void test07_removeAllGivenCards_removingCardsResultsInEmptySet(
            Set<Card> cardsToRemove, Set<Card> cardsPlayerOwns) {
        Player player = new Player();
        player.setOwnedCards(cardsPlayerOwns);

        player.removeAllGivenCards(cardsToRemove);

        assertEquals(Set.of(), player.getOwnedCards());
    }

    private static Stream<Arguments> generateRemoveCardCombosAndNonEmptyResult() {
        Set<Arguments> toStream = new HashSet<>();

        WildCard wildCardOne = new WildCard();
        TerritoryCard brazilCard = new TerritoryCard(TerritoryType.BRAZIL, PieceType.ARTILLERY);
        TerritoryCard congoCard = new TerritoryCard(TerritoryType.CONGO, PieceType.INFANTRY);

        toStream.add(Arguments.of(Set.of(wildCardOne, brazilCard), Set.of(wildCardOne, brazilCard, congoCard),
                Set.of(congoCard)));
        toStream.add(Arguments.of(Set.of(congoCard), Set.of(congoCard, brazilCard), Set.of(brazilCard)));

        Set<Card> allCards = new HashSet<>();
        allCards.add(wildCardOne);
        int pieceTypeCount = 0;
        for (TerritoryType territoryType : TerritoryType.values()) {
            if (territoryType == TerritoryType.CONGO) {
                allCards.add(congoCard);
            } else if (territoryType == TerritoryType.BRAZIL) {
                allCards.add(brazilCard);
            } else {
                PieceType currentPiece = PieceType.values()[pieceTypeCount / 14];
                allCards.add(new TerritoryCard(territoryType, currentPiece));
                pieceTypeCount++;
            }
        }
        WildCard wildCardTwo = new WildCard();
        allCards.add(wildCardTwo);

        Set<Card> allCardsMinusSecondWildCard = new HashSet<>(allCards);
        allCardsMinusSecondWildCard.remove(wildCardTwo);

        toStream.add(Arguments.of(allCardsMinusSecondWildCard, allCards, Set.of(wildCardTwo)));

        return toStream.stream();
    }

    @ParameterizedTest
    @MethodSource("generateRemoveCardCombosAndNonEmptyResult")
    public void test08_removeAllGivenCards_removingCardsStillLeavesPlayerWithSome(
            Set<Card> cardsToRemove, Set<Card> cardsPlayerOwns, Set<Card> expectedOutput) {
        Player player = new Player();
        player.setOwnedCards(cardsPlayerOwns);

        player.removeAllGivenCards(cardsToRemove);

        assertEquals(expectedOutput, player.getOwnedCards());
    }

    private static Stream<Arguments> generateSetsOfTerritoriesPlayerOwnsAndTerritoriesNotInThem() {
        return Stream.of(
                Arguments.of(Set.of(), TerritoryType.ALASKA),
                Arguments.of(Set.of(), TerritoryType.BRAZIL),
                Arguments.of(Set.of(TerritoryType.ONTARIO), TerritoryType.ALASKA),
                Arguments.of(Set.of(TerritoryType.YAKUTSK, TerritoryType.URAL, TerritoryType.ALASKA),
                        TerritoryType.QUEBEC),
                Arguments.of(Set.of(TerritoryType.WESTERN_AUSTRALIA, TerritoryType.INDONESIA), TerritoryType.UKRAINE),
                Arguments.of(Set.of(TerritoryType.MIDDLE_EAST), TerritoryType.MONGOLIA)
        );
    }

    @ParameterizedTest
    @MethodSource("generateSetsOfTerritoriesPlayerOwnsAndTerritoriesNotInThem")
    public void test09_removeTerritoryFromCollection_territoryNotInCollection_expectSetToRemainSame(
            Set<TerritoryType> territoriesPlayerOwns, TerritoryType toRemove) {
        Player player = new Player();
        player.setTerritories(territoriesPlayerOwns);

        player.removeTerritoryFromCollection(toRemove);

        assertEquals(territoriesPlayerOwns, player.getTerritories());
    }
}
