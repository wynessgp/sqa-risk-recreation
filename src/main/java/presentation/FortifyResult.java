package presentation;

public enum FortifyResult {
    SUCCESS,
    NOT_ADJACENT,
    NOT_OWNED,
    NOT_ENOUGH_ARMIES,
    WRONG_PHASE;

    String toKey() {
        String[] parts = name().split("_");
        StringBuilder sb = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i].charAt(0));
            sb.append(parts[i].substring(1).toLowerCase());
        }
        return sb.toString();
    }

    static FortifyResult parseError(String message) {
        return message.contains("adjacent") ? NOT_ADJACENT : message.contains("not owned") ? NOT_OWNED
                : message.contains("enough armies") ? NOT_ENOUGH_ARMIES : WRONG_PHASE;
    }
}
