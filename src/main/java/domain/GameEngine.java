package domain;

import java.util.List;

public abstract class GameEngine {

    public void initializePlayersList(List<PlayerColor> playerOrder, int amountOfPlayers) {
        if (amountOfPlayers == 2 && playerOrder.isEmpty()) {
            throw new IllegalArgumentException("Size mismatch between playerOrder: 0 and amountOfPlayers: 2");
        }
        throw new IllegalArgumentException("amountOfPlayers is not within: [2, 6]");
    }
}
