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
        String[] split = this.name().split("_");
        StringBuilder sb = new StringBuilder(split[0].toLowerCase());
        for (int i = 1; i < split.length; i++) {
            sb.append(split[i].charAt(0)).append(split[i].substring(1).toLowerCase());
        }
        return StringsBundleLoader.getBundle().getString("global." + sb);
    }

}
