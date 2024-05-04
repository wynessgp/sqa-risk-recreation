package domain;

import datasource.StringsBundleLoader;

public enum GamePhase {
    SETUP,
    PLACEMENT,
    SCRAMBLE;

    public String toString() {
        return StringsBundleLoader.getBundle().getString("global." + this.name().toLowerCase());
    }

}
