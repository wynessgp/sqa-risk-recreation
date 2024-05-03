package domain;

import datasource.StringsBundleLoader;

public enum TerritoryType {

    // Copying territories as found on the Wikipedia page:
    // https://en.wikipedia.org/wiki/Risk_(game)

    // North America
    ALASKA, 
    ALBERTA,
    CENTRAL_AMERICA,
    EASTERN_UNITED_STATES,
    GREENLAND,
    NORTHWEST_TERRITORY,
    ONTARIO,
    QUEBEC,
    WESTERN_UNITED_STATES,
    
    // South America
    ARGENTINA, 
    BRAZIL,
    PERU,
    VENEZUELA,

    // Europe
    GREAT_BRITAIN,
    ICELAND,
    NORTHERN_EUROPE,
    SCANDINAVIA,
    SOUTHERN_EUROPE,
    WESTERN_EUROPE,
    UKRAINE,

    // Africa
    CONGO,
    EAST_AFRICA,
    EGYPT,
    MADAGASCAR,
    NORTH_AFRICA,
    SOUTH_AFRICA,

    // Asia
    AFGHANISTAN,
    CHINA,
    INDIA,
    IRKUTSK,
    JAPAN,
    KAMCHATKA,
    MIDDLE_EAST,
    MONGOLIA,
    SIAM,
    SIBERIA,
    URAL,
    YAKUTSK,
    
    // Oceania
    EASTERN_AUSTRALIA,
    INDONESIA,
    NEW_GUINEA,
    WESTERN_AUSTRALIA;

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
