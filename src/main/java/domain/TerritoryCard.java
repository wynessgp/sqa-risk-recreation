package domain;

public class TerritoryCard {
    private final TerritoryType territory;
    private final ArmyType piece;

    public TerritoryCard(TerritoryType territory, ArmyType piece) {
        this.territory = territory;
        this.piece = piece;
    }

    public boolean matchesTerritory(TerritoryType territory) {
        return this.territory == territory;
    }

    public boolean matchesPieceType(ArmyType piece) {
        return this.piece == piece;
    }

    public boolean isWild() {
        return false;
    }
}
