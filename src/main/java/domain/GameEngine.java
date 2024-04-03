package domain;

import java.util.List;

public abstract class GameEngine {

    public void initializePlayersList(List<PlayerColor> playerOrder, int amountOfPlayers) {
        if (amountOfPlayers < 2 || amountOfPlayers > 6) {
            throw new IllegalArgumentException("amountOfPlayers is not within: [2, 6]");
        }
        if (amountOfPlayers != playerOrder.size()) {
            throw new IllegalArgumentException(
                    String.format("Size mismatch between playerOrder: %d and amountOfPlayers: %d",
                            playerOrder.size(), amountOfPlayers));
        }
    }
}
