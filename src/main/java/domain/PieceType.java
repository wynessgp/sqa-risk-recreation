package domain;

public enum PieceType {
    INFANTRY, 
    CAVALRY, 
    ARTILLERY;

    public String toString() {
        String name = this.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
