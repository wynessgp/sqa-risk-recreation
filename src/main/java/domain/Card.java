package domain;

public interface Card {

    boolean matchesTerritory(TerritoryType territory);

    boolean matchesPieceType(PieceType pieceType);

    boolean isWild();

}
