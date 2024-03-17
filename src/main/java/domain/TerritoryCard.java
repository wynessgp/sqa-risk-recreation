package domain;

public class TerritoryCard {
    private final TerritoryType territory;
    private final PieceType piece;

    public TerritoryCard(TerritoryType territory, PieceType piece) {
        this.territory = territory;
        this.piece = piece;
    }

    public boolean matchesTerritory(TerritoryType territory) {
        return this.territory == territory;
    }

    public boolean matchesPieceType(PieceType piece) {
        return this.piece == piece;
    }

    public boolean isWild() {
        return false;
    }
}
