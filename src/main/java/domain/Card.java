package domain;

import java.util.function.BooleanSupplier;

public interface Card {
    public boolean matchesTerritory(TerritoryType territory);
    public boolean matchesPieceType(PieceType infantry);
    public boolean matchesContinent(Continent africa);
}
