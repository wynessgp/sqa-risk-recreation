package domain;

class TerritoryCard implements Card {
    private final TerritoryType territory;
    private final PieceType piece;

    TerritoryCard(TerritoryType territory, PieceType piece) {
        this.territory = territory;
        this.piece = piece;
    }

    @Override
    public boolean matchesTerritory(TerritoryType territory) {
        return this.territory == territory;
    }

    @Override
    public boolean matchesPieceType(PieceType piece) {
        return this.piece == piece;
    }

    @Override
    public boolean isWild() {
        return false;
    }

}
