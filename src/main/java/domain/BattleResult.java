package domain;

import datasource.StringsBundleLoader;

public enum BattleResult {
    DEFENDER_VICTORY,
    ATTACKER_VICTORY;

    public String toString() {
        String[] nameArray = this.name().split("_");
        StringBuilder name = new StringBuilder(nameArray[0].toLowerCase());
        for (int i = 1; i < nameArray.length; i++) {
            String s = nameArray[i];
            name.append(s.charAt(0)).append(s.substring(1).toLowerCase());
        }
        return StringsBundleLoader.getBundle().getString("global." + name);
    }

}
