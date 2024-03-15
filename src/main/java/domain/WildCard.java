package domain;

public class WildCard implements Card {

    @Override
    public boolean matchesTerritory(TerritoryType territory) {
        return true; // that's it.
    }

    @Override
    public boolean matchesPieceType(PieceType pieceType) {
        return true; // easiest class I've ever written
    }

    @Override
    public boolean matchesContinent(Continent continent) {
        return true; // ...
    }

    @Override
    public boolean isWild() {
        return true;
    }

}
