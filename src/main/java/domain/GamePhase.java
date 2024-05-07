package domain;

import datasource.StringsBundleLoader;

public enum GamePhase {
    SETUP,
    PLACEMENT,
    SCRAMBLE,
    ATTACK;

    public String toString() {
        return StringsBundleLoader.getBundle().getString("global." + this.name().toLowerCase());
    }

}
