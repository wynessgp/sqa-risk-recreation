package domain;

public enum GamePhase {
    SETUP,
    PLACEMENT,
    SCRAMBLE;

    public String toString() {
        String name = this.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
