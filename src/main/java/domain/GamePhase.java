package domain;

public enum GamePhase {
    SETUP,
    PLACEMENT,
    SCRAMBLE,
    ATTACK;

    public String toString() {
        String name = this.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
