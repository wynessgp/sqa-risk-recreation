package presentation;

public enum AttackResult {
    SUCCESS,
    NOT_ADJACENT,
    SOURCE_NOT_OWNED,
    DESTINATION_OWNED,
    WRONG_STATE,
    BAD_ATTACK_ARMIES,
    BAD_DEFEND_ARMIES,
    NOT_ENOUGH_ATTACKERS,
    NOT_ENOUGH_DEFENDERS;

    String toKey() {
        String[] parts = name().split("_");
        StringBuilder sb = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i].charAt(0));
            sb.append(parts[i].substring(1).toLowerCase());
        }
        return sb.toString();
    }

    static AttackResult parseError(String message) {
        return message.contains("adjacent") ? NOT_ADJACENT : message.contains("not owned") ? SOURCE_NOT_OWNED
                : message.contains("is owned") ? DESTINATION_OWNED : message.contains("phase")
                || message.contains("trade in") ? WRONG_STATE : message.contains("1, 3")
                ? BAD_ATTACK_ARMIES : message.contains("1, 2") ? BAD_DEFEND_ARMIES : message.contains("few armies")
                ? NOT_ENOUGH_ATTACKERS : NOT_ENOUGH_DEFENDERS;
    }
}
