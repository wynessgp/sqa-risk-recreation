package domain;

public class WildCard implements Card {

    @Override
    public boolean matchesTerritory(TerritoryType territory) {
        return false;
    }

    @Override
    public boolean matchesPieceType(PieceType pieceType) {
        return true;
    }

    @Override
    public boolean isWild() {
        return true;
    }

}
