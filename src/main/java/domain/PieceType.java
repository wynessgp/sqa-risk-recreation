package domain;

import datasource.StringsBundleLoader;

public enum PieceType {
    INFANTRY, 
    CAVALRY, 
    ARTILLERY;

    public String toString() {
        return StringsBundleLoader.getBundle().getString("global." + this.name().toLowerCase());
    }

}
