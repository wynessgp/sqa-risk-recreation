package domain;

import java.util.List;

public abstract class GameEngine {

    public void initializePlayersList(List<PlayerColor> playerOrder, int amountOfPlayers) {
        throw new IllegalArgumentException("amountOfPlayers is not within: [2, 6]");
    }
}
