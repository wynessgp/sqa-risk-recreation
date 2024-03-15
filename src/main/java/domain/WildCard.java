package domain;

public class WildCard implements Card {

    @Override
    public boolean matchesTerritory(TerritoryType territory) {
        return true; // that's it.
    }

}
