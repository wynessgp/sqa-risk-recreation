package domain;

public enum PlayerColor {
    SETUP,
    BLACK,
    RED,
    YELLOW,
    BLUE,
    GREEN,
    PURPLE;

    public String toString() {
        String name = this.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
