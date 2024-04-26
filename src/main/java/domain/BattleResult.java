package domain;

public enum BattleResult {
    DEFENDER_VICTORY,
    ATTACKER_VICTORY;

    public String toString() {
        String name = this.name().replace("_", " ");
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
