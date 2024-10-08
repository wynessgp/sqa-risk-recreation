package domain;

import datasource.StringsBundleLoader;

public enum PlayerColor {
    SETUP,
    BLACK,
    RED,
    YELLOW,
    BLUE,
    GREEN,
    PURPLE;

    public String toString() {
        return StringsBundleLoader.getBundle().getString("global." + getColorString());
    }

    public String getColorString() {
        return this.name().toLowerCase();
    }

}
