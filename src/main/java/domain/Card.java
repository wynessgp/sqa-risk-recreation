package domain;

public interface Card {
    public boolean matchesTerritory(TerritoryType territory);
    public boolean matchesPieceType(PieceType pieceType);
    public boolean isWild();
}
