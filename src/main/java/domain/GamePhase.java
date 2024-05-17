package domain;

import datasource.StringsBundleLoader;

public enum GamePhase {
    SETUP,
    PLACEMENT,
    SCRAMBLE,
    ATTACK,
    GAME_OVER,
    FORTIFY;

    public String toString() {
        return StringsBundleLoader.getBundle().getString("global." + this.name().toLowerCase());
    }

}
